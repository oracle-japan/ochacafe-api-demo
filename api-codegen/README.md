# api-codegen

OpenAPI Spec から Java Client ソースを生成します。

## 使い方

### ソースの生成

```sh
mvn clean generate-sources
```

target/generated-sources/swagger ディレクトリにファイル一式生成されます。

### 生成されたソースのテスト

target/generated-sources/swagger ディレクトリに移動して

```sh
mvn test
```

を実行すると、とりあえずtestフェースまで実行されますが、テスト自体はスキップされます。

```text
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Concurrency config is parallel='methods', perCoreThreadCount=true, threadCount=2, useUnlimitedThreads=false
Running io.swagger.client.api.CountryApiTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 1, Time elapsed: 0 sec

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 1
```

io.swagger.client.api.CountryApiTest クラスに @Ignore アノテーションがついているためです。  
@Ignore をコメントアウトすればテストコード自体は実行されますが、それ自体は何かをassertするものでもなく、引数がnullになっていたりするのでExceptionが発生します。このクラスにテスト処理を追加したクラス(test_source/CountryApiTest.java)を上書きコピーしてテストを実行してみます。

```text
$ cd target/generated-sources/swagger
$ cp ../../../test_source/CountryApiTest.java src/test/java/io/swagger/client/api
$ mvn test
[INFO] Scanning for projects...
(省略...)
[INFO] --- maven-surefire-plugin:2.12:test (default-test) @ swagger-java-client ---
[INFO] Surefire report directory: /home/opc/work/api-codegen/target/generated-sources/swagger/target/surefire-reports

-------------------------------------------------------
 T E S T S
-------------------------------------------------------

-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Concurrency config is parallel='methods', perCoreThreadCount=true, threadCount=2, useUnlimitedThreads=false
Running io.swagger.client.api.CountryApiTest
>>> Test api.getCountry(1)
class Country {
    countryId: 1
    countryName: United States of America
}
>>> Test api.getCountries()
[0] class Country {
    countryId: 1
    countryName: United States of America
}
[1] class Country {
    countryId: 81
    countryName: Japan
}
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.693 sec

Results :

Tests run: 2, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  4.064 s
[INFO] Finished at: 2019-04-24T03:42:11Z
[INFO] ------------------------------------------------------------------------
```
