## Build & Docker

To change the output jar, add this to your pom.xml (inside the "build" tag):

```xml
<build>
    <finalName>app</finalName>
</build>

```


Then:

1. Have Dockerfile ready
2. Have Docker Compose file ready
3. Make sure Docker daemon is running
4. Build the image: `docker build -t rate-limiter .`
5. Run the image manually, to make sure container and port mapping are ok. Command: `docker run -p 9000:9000 rate-limiter`
6. Run the Docker Compose, so you can start all containers/services at once. Command: `docker compose up --build`