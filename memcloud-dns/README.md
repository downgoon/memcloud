## how to build & run & test

	mvn clean package -Dmaven.test.skip=true -Pop
	mvn jetty:run -Djetty.port=10009
	curl -x 10.213.57.166:10009 "http://memcloud.io" -i
