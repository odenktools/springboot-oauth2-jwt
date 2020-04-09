Springboot 2.0.x Resource Server
=================================

#### Running Application (Using Gradle)

```bash
gradle bootRun
```

#### Running as a Packaged Application

Build Jar

```bash
mvn package
```

Or Build Jar without test

```bash
mvn package -Dmaven.test.skip=true
```


Execute Jar Package

```bash
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8091,suspend=n -jar target/resourceserver-1.0.0.jar
```

OR

```bash
mvn spring-boot:run
```

#### Build docker container

```bash
docker build --tag odenktools/oauth2-resource-server:1.0.0 --build-arg JAR_FILE=target/resourceserver-1.0.0.jar .
docker tag odenktools/oauth2-resource-server:1.0.0 odenktools/oauth2-resource-server:latest
```

#### Publish to Cloud

```bash
docker push odenktools/oauth2-resource-server:1.0.0
docker push odenktools/oauth2-resource-server:latest
```

# LICENSE

MIT License

Copyright (c) 2018 - 2020 odenktools

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
