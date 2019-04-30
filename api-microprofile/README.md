# api-microprofile

JAX-RSリソースに MicroProfile OpenAPIアノテーションをつけ、サーバー上でOpenAPI v3 Specを公開します。

## 使い方

```sh
mvn clean package
```

targetディレクトリに api-microprofile.warファイルができますので、MicroProfile OpenAPIに対応するサーバーにデプロイして下さい。  
デプロイしたサーバー上で (context root)/openapi にアクセスして下さい。

例: Payara Micro でテストする場合 (事前に payara-micro-5.191.jar をダウンロードして下さい)

Payara Mcro を起動

```sh
java -jar payara-micro-5.191.jar --deploy target/api-microprofile.war --nocluster
```

curlを使って OpenAPI Spec を表示させる

```sh
curl -s http://localhost:8080/openapi
```

Windowsの場合は、UTF-8 -> Shift-JIS に変換しないと文字化けするので、curl コマンドの最後に " | nkf -s " をつけると日本語が正しく表示されます。
