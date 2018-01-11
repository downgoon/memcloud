# memcloud
a distributed cluster for memcached

## install and start memcloud

install with a bash script

``` bash
[root@10.213.42.153 memcloud]#./memcloud-install.sh
```

start memcloud

> ``memcloud.sh [local_addr:]<local_port> <peer_addr>:<replica_port>``

for example

``` bash
memcloud.sh 11211 10.213.42.154:11212
```

for more details about **install and start**, please read wiki [Quick-Start](https://github.com/downgoon/memcloud/wiki/Quick-Start)
