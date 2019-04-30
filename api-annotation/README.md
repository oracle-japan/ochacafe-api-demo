# api-annotation

JAX-RSリソースに Swagger Core のアノテーションをつけ、サーバー上でOpenAPI v3 Specを公開します。

## 使い方

```sh
mvn clean package
```

### コンパイル時に OpenAPI Spec を生成

pom.xmlには swagger-maven-plugin を組み込んでいますので、Mavenを実行すると target/openapi ディレクトリに oepnapi.json と openapi.yaml が出力されます。

### サーバー上で OpenAPI Spec を提供

targetディレクトリに api-annotaion.warファイルができますので、JAX-RS対応サーバーにデプロイして下さい。  
デプロイしたサーバー上で (context root)/openapi にアクセスすると OpenAPI v3 Spec (json形式)が返されます。yaml形式で取り出したい場合は、(context root)/openapi/openapi.yaml のURLでGETしてください。  
  
### 注意点

Swagger Coreライブラリを使ってサーバーから OpenAPI Specを提供する場合、アノテーションにダブルバイトを使用しているとブラウザで文字化けが発生します。このデモでは、これを回避するために、Swagger Coreライブラリが提供しているサーブレット(io.swagger.v3.jaxrs2.integration.OpenApiServlet)を修正したもの(oracle.demo.servlet.OpenApiServlet)をweb.xmlに設定しています。