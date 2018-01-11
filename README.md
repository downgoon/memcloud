# memcloud

[![Join the chat at https://gitter.im/memcloud/Lobby](https://badges.gitter.im/memcloud/Lobby.svg)](https://gitter.im/memcloud/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

a distributed cluster for memcached with bi-directional replication. it was designed and developed on 2012, so some of technologies used in this project are not so fashion. But its reliability and manageability have been seen in production environments for many years.


## architecture

``memcloud`` is a distributed cluster for memcached with **bi-directional replication**. it consists of **four** components including ``mem-dns``, ``mem-agent``, ``mem-client`` and ``mem-alert``.

by the way, a more lite version ([memcloud-0.1.0 automan](https://github.com/downgoon/memcloud/releases/tag/0.1.0)) is recommended if you just only want a bi-directional replicated memcached in case of data corruption.

### main working flow

![memcloud architecture](https://cloud.githubusercontent.com/assets/23731186/25747647/ca53c252-31da-11e7-894c-ef2c1000678f.png)


the ``mem-agent`` here mainly refers to a daemon of memcached instance with bi-directional replication (replication-testing is [here](https://github.com/downgoon/memcloud/wiki/Quick-Start#replication-testing)) to its peer memcached.


### mem-agent installation and registration

![mem-agent ](https://cloud.githubusercontent.com/assets/23731186/21416744/4738747e-c850-11e6-9c47-816f6dbaefe6.jpg)

actually the ``mem-agent`` is a software package which contains at least two bash scripts named ``memcloud_install.sh`` and ``memcloud.sh``. ``memcloud_install.sh`` is responsible for automatical installation and easy registration of memcached instance with bi-directional replication, and the other for starting memcached daemon.

### mem-alert insight

![mem-alert](https://cloud.githubusercontent.com/assets/23731186/21413464/5f480b92-c832-11e6-8c53-fa254bfb5607.jpg)

----

## how to start

the order of starting

1. start ``mem-dns``
2. start ``mem-agent``
3. start ``mem-alert``
4. start ``mem-client``

### start ``mem-dns``

download [memcloud-dns-0.2.0.tar.gz](https://github.com/downgoon/memcloud/releases/download/0.2.0/memcloud-dns-0.2.0.tar.gz), unpack it and ``start-memdns.sh``.

```
$ wget https://github.com/downgoon/memcloud/releases/download/0.2.0/memcloud-dns-0.2.0.tar.gz
$ tar zxvf memcloud-dns-0.2.0.tar.gz
$ cd memcloud-dns-0.2.0
$ start-memdns.sh
```

**NOTE**
>Before ``start-memdns.sh``, please make sure its dependencies including mysqld and mongodb in the configuration are ready.

### start ``mem-agent``

download [memcloud-agent-0.2.0.tar.gz](https://github.com/downgoon/memcloud/releases/download/0.2.0/memcloud-agent-0.2.0.tar.gz), unpack it and ``memcloud-install.sh``

``` bash
$ wget https://github.com/downgoon/memcloud/releases/download/0.2.0/memcloud-agent-0.2.0.tar.gz
$ tar zxvf memcloud-agent-0.2.0.tar.gz
$ cd memcloud-agent-0.2.0
$ memcloud_install.sh
```

after installation, you can start it according to the chapter named [Quick-Start#how-to-start-memcloud](https://github.com/downgoon/memcloud/wiki/Quick-Start#how-to-start-memcloud)

### start ``mem-alert``

```
$ wget https://github.com/downgoon/memcloud/releases/download/0.2.0/memcloud-alert-0.2.0.tar.gz
$ tar zxvf memcloud-alert-0.2.0.tar.gz
$ cd memcloud-alert-0.2.0
$ bin/memalert start
```

**NOTE**
>Before ``bin/memalert start``, please make sure its dependency mongodb in the configuration is ready.

### start ``mem-client``

memcloud client sample code is as follows:


``` java

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.memcloud.client.MemCloudClient;

public class MemCloudClientDemo {

	private static final Logger LOG = LoggerFactory.getLogger(MemCloudClientDemo.class);

	public static void main(String[] args) throws Exception {

		System.setProperty("memcloud.memdns", "your-memcloud-dns-host.example.com");

    // appid allocated by mem-dns
		String appid = "10021";
		MemCloudClient mcc = MemCloudClient.buildDefault(appid);

		// set
		int i = 0;
		while (i < 10) {
			mcc.getMemcachedClient().set("k" + i, 60 * 5, "v" + i);
			i++;
		}

		// get

		i = 0;
		while (i < 10) {
			String value = mcc.getMemcachedClient().get("k" + i);
			LOG.info("value from memcloud: {}", value);
			i++;
		}
	}
}
```

add the dependency into pom.xml

``` xml
<dependency>
	<groupId>io.memcloud</groupId>
	<artifactId>memcloud-client</artifactId>
	<version>0.2.0</version>
</dependency>
```

----

## Some Snapshots

### mem-dns sign-in

![mem-dns sign-in](https://cloud.githubusercontent.com/assets/23731186/25752319/7be947bc-31ea-11e7-9144-62b09004cf8d.png)

### create an application

![create an application](https://cloud.githubusercontent.com/assets/23731186/25752940/6ac2c2d6-31ec-11e7-8c54-7beeb785c5e3.png)

### asking for memcached allocated to the given application

![apply for memcached](https://cloud.githubusercontent.com/assets/23731186/25752580/4d889570-31eb-11e7-93e6-52319f67c62c.png)

### application listing

![application listing](https://cloud.githubusercontent.com/assets/23731186/25753129/05417e88-31ed-11e7-8bd2-4014bcccedff.png)

### authorize a requisition

![authorize a requisition](https://cloud.githubusercontent.com/assets/23731186/25753308/90fbde28-31ed-11e7-83a5-ee0706496545.png)

### health monitor

![health monitor](https://cloud.githubusercontent.com/assets/23731186/25753737/47fd61fe-31ef-11e7-979e-aed8ac32f2c6.png)

### bi-directional replication

![bi-directional replication](https://cloud.githubusercontent.com/assets/23731186/20823430/e023ec12-b88e-11e6-856e-5ae301046c0c.png)

----

## how to build

``` bash
$ git clone https://github.com/downgoon/memcloud.git
$ cd memcloud
$ mvn clean package
```

----
