# Java Websocket Server and Client

## For the server
<pre><code>
gradle clean server:build
cp server/build/libs/server.war <Jetty webapps directory>
</code></pre>

## For the client

<pre><code>
gradle client:fatJar
java -jar client/build/libs/client-all.jar
</code></pre>
