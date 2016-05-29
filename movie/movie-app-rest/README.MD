Movie JSON:
{
"title":"Three Idiots",
"director":"Rajkumar Hirani",
"releaseYear":"2011",
"genre":"comedy"
}

{
"title":"dummy",
"director":"dummy",
"releaseYear":"2011",
"genre":"dummy"
}

POST http://localhost:8092/movie-app/api/movies
GET http://localhost:8092/movie-app/api/movies
PUT http://localhost:8092/movie-app/api/movies/1
GET http://localhost:8092/movie-app/api/movies/1
DELETE http://localhost:8092/movie-app/api/movies/1

mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=9000"

(ip.addr==127.0.0.1 || ip.addr == 192.168.1.100) && http && tcp.port > 6000