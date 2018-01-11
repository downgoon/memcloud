#! /bin/bash

##################################################################################
### version 1.0 memcloud_install.sh
##################################################################################

#exit value mapping (you can get this value by executing "echo $?" after this script executed)
#0 SUCC
#1 EXIST (Needless to install memcached)
#2 ERROR 

if [ -e /usr/local/bin/memcached ]; then
   echo "/usr/local/bin/memcached exist"
   exit 1
fi
##################################################################################
#CONFIGURATIONS

url_libevent=https://github.com/downloads/libevent/libevent/libevent-2.0.19-stable.tar.gz
url_repcached=http://mdounin.ru/files/repcached-2.3-1.4.5.patch.gz
#url_memcached=http://memcached.googlecode.com/files/memcached-1.4.5.tar.gz
url_memcached=https://github.com/downgoon/memcloud/files/626324/memcached-1.4.5.tar.gz
path_software=/opt/memcloud/

debug=true

##################################################################################

if [ ! -L ~/memcloud ]; then
   /bin/ln -s $path_software ~/memcloud
fi


curdir=$(pwd)
#make directory for $path_software
if [ ! -e $path_software ]; then
  /bin/mkdir -p $path_software   
fi
if [ ! -e $path_software ]; then
   echo "ERROR: install path $path_software create fail, please change another path by editing var named 'path_software' in CONFIGURATIONS of this script"
   exit 2 
fi

#Download libevent, repcached and memcached if they are not found in the specified path
file_libevent=`echo $url_libevent | grep -P -o 'libevent-.*$'`
file_repcached=`echo $url_repcached | grep -P -o 'repcached-.*$'`
file_memcached=`echo $url_memcached | grep -P -o 'memcached-.*$'`

unzipdir_libevent=`echo $file_libevent | awk -F'.tar' '{ printf "%s", $1 }'`
unzipdir_memcached=`echo $file_memcached | awk -F'.tar' '{ printf "%s", $1 }'`

if [ debug ]; then
   echo "path_software:["$path_software"]"
   echo "libevent version: "$file_libevent
   echo "repcached version: "$file_repcached
   echo "memcached version: "$file_memcached
   echo "unzipdir_libevent:["$unzipdir_libevent"]"
   echo "["$path_software$unzipdir_libevent"]"
fi

ls $path_software | grep libevent
if [ $? -ne 0 ]; then
  /usr/bin/wget $url_libevent -P $path_software 
fi


ls $path_software | grep memcached
if [ $? -ne 0 ]; then
   /usr/bin/wget $url_memcached -P $path_software
fi


if [ ! -e $path_software$file_repcached ]; then 
  /usr/bin/wget $url_repcached -P $path_software
fi


#unzip libevent and memcached (with repcached patch)
/bin/tar zxvf $path_software$file_libevent -C $path_software
/bin/tar zxvf $path_software$file_memcached -C $path_software
cp -r $path_software$file_repcached $path_software$unzipdir_memcached
cd $path_software$unzipdir_memcached

# unzip some.patch.gz and then unpatch some.patch

if [ -e $path_software$file_repcached ]; then
   echo "unzip $file_repcached ..."
   gzip -d ${file_repcached} 
fi

echo "unpatch ${file_repcached%%.gz*} ..."
/usr/bin/patch --force -p1 -i ${file_repcached%%.gz*}

#install libevent
cd $path_software$unzipdir_libevent
./configure --prefix=/usr/local && make && make install

#install memcached with repcached patch
cd $path_software$unzipdir_memcached
./configure --prefix=/usr/local --with-libevent=/usr/local --enable-replication && make && make install

#create the appropriate symlink for libevent
#otherwise you will get 'memcached: error while loading shared libraries: libevent-2.0.so.5: cannot open shared object file: No such file or directory'
#while you execute 'memcached -d -m 100 -u root -l 10.10.83.177 -p 11211 -c 256 -P /tmp/memcached.pid'
#HELP REFER: http://www.nigeldunn.com/2011/12/11/libevent-2-0-so-5-cannot-open-shared-object-file-no-such-file-or-directory/

if [ -e /usr/local/lib/libevent-2.0.so.5 ]; then
   ln -s /usr/local/lib/libevent-2.0.so.5 /usr/lib/libevent-2.0.so.5
   ln -s /usr/local/lib/libevent-2.0.so.5 /usr/lib64/libevent-2.0.so.5
fi

cd $curdir

inner_ip=`ifconfig | grep -P 'inet addr:(10.|192.)' | head -1 | awk '{ printf "%s", substr($2,6) }'`
if [ -e /usr/local/bin/memcached ]; 
then
   echo "memcloud installed ok: /usr/local/bin/memcached"
   echo "/usr/local/bin/memcached -d -m 100 -u root -l $inner_ip -p 11211 -c 256 -P /tmp/memcached.pid"
   exit 0
else 
   exit 2 
fi

