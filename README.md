## Build & Docker

To change the output jar, add this to your pom.xml (inside the "build" tag):

```xml
<build>
    <finalName>app</finalName>
</build>

```


Then:

Every time you make a change to the project, you need to build into a jar, like so 
(Make sure to  use the --build flag for Docker Compose; if you use just click on Docker Compose from the IDE,
it's likely you won't see the updated project, because Docker will has cached the image. With --build, you force 
to rebuild the image.):

`
./mvnw clean package -DskipTests

docker compose up --build
`

1. Have Dockerfile ready
2. Have Docker Compose file ready
3. Make sure Docker daemon is running
4. Build the image: `docker build -t rate-limiter .`
5. Run the image manually, to make sure container and port mapping are ok. Command: `docker run -p 9000:9000 rate-limiter`
6. Run the Docker Compose, so you can start all containers/services at once. Command: `docker compose up --build`