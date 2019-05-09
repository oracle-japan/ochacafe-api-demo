# api-helidon

JAX-RSソースをhelidonで動かします。

## 使い方

### ローカルで動かす

```text
$ mvn clean package exec:java
(省略)
情報: Server started on http://localhost:8080 (and all other host addresses) in 778 milliseconds.
```

```text
$ curl localhost:8080/api/v1/country
[{"countryId":1,"countryName":"USA"},{"countryId":81,"countryName":"Japan"}]
```

### Dockerfileを使ってDocker イメージを作成する

```text
$ mvn clean package

$ docker build . -f target/Dockerfile -t api-helidon:latest

Sending build context to Docker daemon  17.52MB
(省略)
Successfully built 7ccf95e6a74e
Successfully tagged api-helidon:latest

$ docker run --rm -d --name api -p8888:8080 api-helidon:latest
ba95caa85f40ec73959f8fc69c92f9b50e83e103b22e90f8841c5872f465a7ee

$ curl http://localhost:8888/api/v1/country
[{"countryId":1,"countryName":"USA"},{"countryId":81,"countryName":"Japan"}]

$ docker stop api
api
```

### Jibを使ってMavenから直接Docker RepositoryにDockerイメージを登録する

local repositoryにimageを作るパターン

```text
$ mvn -f pom-jib.xml package
[INFO] Scanning for projects...
(省略)
[INFO] --- jib-maven-plugin:1.0.2:dockerBuild (default) @ api-helidon ---
[INFO] 
[INFO] Containerizing application to Docker daemon as api-helidon-jib, api-helidon-jib:0.0.1-SNAPSHOT...
[INFO] The base image requires auth. Trying again for openjdk:8-jre-alpine...
[INFO] 
[INFO] Container entrypoint set to [java, -server, -Djava.awt.headless=true, -XX:+UnlockExperimentalVMOptions, -XX:+UseCGroupMemoryLimitForHeap
, -XX:InitialRAMFraction=2, -XX:MinRAMFraction=2, -XX:MaxRAMFraction=2, -XX:+UseG1GC, -cp, /app/resources:/app/classes:/app/libs/*, io.helidon.microprofile.server.Main]
[INFO] 
[INFO] Built image to Docker daemon as api-helidon-jib, api-helidon-jib:0.0.1-SNAPSHOT
[INFO] Executing tasks:
[INFO] [==============================] 100.0% complete
(省略)

$ docker image ls api-helidon-jib
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
api-helidon-jib     0.0.1-SNAPSHOT      04b2b0d55428        49 years ago        102MB
api-helidon-jib     latest              04b2b0d55428        49 years ago        102MB
```

Oracle Cloud Infrastructure Registry(OCIR)にpushするパターン (docker.repo に remote repositoryのパスを指定して jib:build)

```text
$ mvn -f pom-jib.xml -Ddocker.repo=xxx.ocir.io/{{tenant}}/{{repository}}/api-helidon-jib compile jib:build
[INFO] Scanning for projects...
(省略)
[INFO] --- jib-maven-plugin:1.0.2:build (default-cli) @ api-helidon ---
[INFO] 
[INFO] Containerizing application to xxx.ocir.io/{{tenant}}/{{repository}}/api-helidon-jib, xxx.ocir.io/{{tenant}}/{{repository}}/api-helidon-jib:0.0.1-SNAPSHOT...
[INFO] The base image requires auth. Trying again for openjdk:8-jre-alpine...
[INFO] 
[INFO] Container entrypoint set to [java, -server, -Djava.awt.headless=true, -XX:+UnlockExperimentalVMOptions, -XX:+UseCGroupMemoryLimitForHeap
, -XX:InitialRAMFraction=2, -XX:MinRAMFraction=2, -XX:MaxRAMFraction=2, -XX:+UseG1GC, -cp, /app/resources:/app/classes:/app/libs/*, io.helidon.microprofile.server.Main]
[INFO] 
[INFO] Built and pushed image as iad.ocir.io/orasejapan/tkotegaw/api-helidon-jib, iad.ocir.io/orasejapan/tkotegaw/api-helidon-jib:0.0.1-SNAPSHO
TINFO] 
[INFO] Executing tasks:
[INFO] [==============================] 100.0% complete
(省略)
```

* OCIRのURLについては、[FAQ](https://cloud.oracle.com/containers/faq#ocir) を参照

#### Jibを使ってHelidonプロジェクトのイメージを作成する時の注意点

Jibは、classes, resources, libs の3つのディレクトリを作って(内部的にはそれぞれがDockerイメージのレイヤーになっているらしい - これによって変更の局所化ができる!)、これにclasspathを通します。ところがHelidonはCDIを使っているため、この状況ではbeans.xmlがclassesのクラスパスに効かず、正常に起動しません。これを回避するために、maven-resources-plugin を使って classes/META-INFにもbeans.xmlをコピーしています。

### その他の方法

+ GitHubにpush -> Oracle Container Pipelines (Werker) にwebhookがかかって build & deploy
+ Oracle Developer Cloud Service (DevCS) にpush -> Jobが起動されて build & deploy
