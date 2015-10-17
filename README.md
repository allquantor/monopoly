THIS IS A FORK OF: https://github.com/sbilinski/scalatra-mongodb-seed


To start with HAPROXY:
	1) haproxy -f /PATH_TO_CONFIG/haproxy.cfg
 	2) sbt docker
 	2) docker run -d -e APP_PORT=8080 -p 8080:8080 com.haw/monopoly:v0.1-SNAPSHOT


Use:
	http://DOCKER_IP:8080/dice

