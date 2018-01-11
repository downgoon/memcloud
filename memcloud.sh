#! /bin/bash
######################################################################################
###	version 1.0 memcloud.sh
######################################################################################

curdir=$(pwd)
isdebug=1

inner_network=`ifconfig | grep -P 'inet addr:(10.|192.)' | head -1 | awk '{ printf "%s", substr($2,6) }'`
usage="Usage: memcloud.sh [local_ip:]<local_port> <peer_ip:repc_port>  \n
example: memcloud.sh 11211 ${inner_network}:11212\n
**NOTE** \n\t<local_port> MUST not equal to <repc_port>\n for more info \n
https://github.com/downgoon/memcloud/wiki/Quick-Start#how-to-start-memcloud"
if [ $# -lt 2 ]; then
   echo -e  $usage
   exit 1
fi

#local_addr and remote_addr
la=$1
ra=$2

if echo $la | grep -P '^(\d+\.){3}\d+:\d{1,8}$' > /dev/null ; then 
  local_ip=`echo $la | awk -F':' '{ printf "%s", $1}'`
  local_port=`echo $la | awk -F':' '{ printf "%s", $2}'`
else
  echo $la | grep -P '^\d{1,8}$' > /dev/null
  if [ $? -eq 0 ]; then
      local_ip=`ifconfig | grep -P 'inet addr:(10.|192.)' | head -1 | awk '{ printf "%s", substr($2,6) }'`
      local_port=$la
     else 
       echo "local addr format error: [local_ip:]<local_port>"
       exit 3
  fi
fi

echo $ra | grep  -P '^(\d+\.){3}\d+:\d{1,8}$' > /dev/null
if [ $? -ne 0 ]; then
   echo "remote addr format error:<peer_ip:repc_port>"
   exit 4 
fi

peer_ip=`echo -n $ra | awk -F':' '{ printf "%s", $1}'`
repc_port=`echo -n $ra | awk -F':' '{ printf "%s", $2}'`

if [ $local_port -eq $repc_port ]; then
   echo "Bad Arguments: <local_port> MUST not equal to <repc_port>"
   exit 5
fi

if [ isdebug ]; then
   echo "local addr is $local_ip:$local_port and remote addr is $peer_ip:$repc_port"
fi

arg_mem=1024
arg_conn=256
arg_user=root

file_pid=/tmp/memcloud_${local_port}_${peer_ip}_${repc_port}.pid
if [ $# -ge 3 ]; then
   app_flag=$3
   file_pid=/tmp/memcloud_${app_flag}_${local_port}_${peer_ip}_${repc_port}.pid
fi

if [ isdebug ]; then
   echo "/usr/local/bin/memcached -d -p ${local_port} -m ${arg_mem} -x ${peer_ip} -X ${repc_port} -u ${arg_user} -l ${local_ip}  -c ${arg_conn} -P ${file_pid}"
   echo "/usr/local/bin/memcached -d -p ${local_port} -m ${arg_mem} -x ${peer_ip} -X ${repc_port} -u ${arg_user} -l ${local_ip}  -c ${arg_conn} -P ${file_pid} -v >> ./memdebug.log  2>&1"
fi

/usr/local/bin/memcached -d -p ${local_port} -m ${arg_mem} -x ${peer_ip} -X ${repc_port} -u ${arg_user} -l ${local_ip}  -c ${arg_conn} -P ${file_pid} 

##############################################################################
### append mem-instance into mem-dns though HTTP API 
##############################################################################

cmd="/usr/local/bin/memcached -d -p ${local_port} -m ${arg_mem} -x ${peer_ip} -X ${repc_port} -u ${arg_user} -l ${local_ip}  -c ${arg_conn} -P ${file_pid}"

pageRoot=$curdir
pageName=${local_port}_${peer_ip}_${repc_port}

/usr/bin/curl http://10.10.83.177/memcloud/mem-create.xml --data cmd="${cmd}" --silent --connect-timeout 30 --dump-header ${pageRoot}/${pageName}_new.head --output ${pageRoot}/${pageName}_new.body

#CHECK: HTTP STATUS CODE
code=`grep HTTP/1. ${pageRoot}/${pageName}_new.head | tr -d '\n\r' | awk {'print $2'}`
if [ $code -ne 200 ]
then
   echo "Error HTTP Code $code on $pageName"
   exit -502
else
   echo "append mem-instance into mem-dns : ${cmd}"
fi
cd $curdir
