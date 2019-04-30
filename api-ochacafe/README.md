# api-ochacafe

デモの基本形となるJAX-RSリソース、ExcpetionMapper、Filter など

## 使い方

```sh
mvn clean package
```

targetディレクトリに api-ochacafe.warファイルができますので、JAX-RS対応サーバーにデプロイして下さい。

## 解説

### ソースの説明

oracle.demo.CountryResource にAPIが定義されています。パスによって、適用されるFilterが違います

| PATH | @CORS | @Auth |
|--|:--:|:--:|
| (context-root)/api/v1/country | | |
| (context-root)/api/v1/country/{countryId} | | |
| (context-root)/api/v1/country/cors/{countryId} | x |  |
| (context-root)/api/v1/country/auth/{countryId} | x | x |
<br/>  

| annotation | mapped filter | description |  
|--|--|--|
| @Auth | oracle.demo.filter.BasicAuthFilter | ベーシック認証を行います - oracle/Welcome1 のみ許可します |  
| @CORS | oracle.demo.filter.CORSFilter | CORS レスポンスヘッダを追加します |
<br/>

ExceptionMaapperは以下の通りです。

| exception mapper | Exception | action |
|--|--|--|
| oracle.demo.mapper.CountryNotFoundException | oracle.demo.CountryResource.CountryNotFoundException | return 401 |
<br/>  

### Filterをテストする
ブラウザで filtertest/filtertest.html を開いて下さい。  

### WebLogic Serverのデフォルト状態でのBasic認証処理の理解、ならびにデフォルト動作の変更方法

[リソースが非セキュアな場合のBASIC認証の理解][1] にあるように、
WebLogic Serverバージョン9.2以降では、ターゲット・リソースにおいてアクセス制御が有効になっていない場合でも、
HTTP BASIC認証を使用するクライアント・リクエストはWebLogic Server認証を通過しなければなりません。
したがって、ユーザーおよびパスワードの情報がWebLogic Serverに保持されている必要があります。
バックエンドのWebサービスを使用してクライアントを認証する場合は、ユーザー情報をWebLogic Serverに保持する必要はありませんが、
デフォルトの認証実施が有効になっている場合は、先にWebLogic Server認証に成功していないと、Webサービス独自の認証を実行することできません。
WebLogic Serverによる認証実施をバイパスしてバックエンドのWebサービスだけが認証をおこなうようにするには、
config.xmlを修正して、enforce-valid-basic-auth-credentialsフラグをfalseにします。  
  
**今回の@Authフィルタのデモは、まさにこのケースです。**  
  
config.xmlはエディタで修正することも可能ですが、WLSTを使って安全にconfig.xmlを書き換える方法をお勧めします。  
以下に、Oracle Java Cloud Service(JCS)で稼働するWebLogic Serverのconfig.xmlを修正する方法を紹介します。

(STEP 1) JCSのWebLogic Admin Serverが稼働するインスタンスにsshでログインする

(STEP 2) WLSTを起動する

```sh
sudo su - oracle
cd $MIDDLEWARE_HOME/oracle_common/common/bin
./wlst.sh
```

(STEP 3) WLSTコマンドを使ってconfig.xmlを変更する(enforce-valid-basic-auth-credentialsフラグをfalseにする)

```sh
connect('{{admin username}}','{{admin pasword}}','t3s://{{jcs local ip address}}:7002')
edit()
startEdit()
cd('SecurityConfiguration/{{jcs domain name}}')
set('EnforceValidBasicAuthCredentials','false')
save()
activate()
disconnect()
exit()
```

* {{}} は、各々の環境に応じた値に置き換えて下さい  

(STEP 4) sshをログアウトし、JCSコンソールからJCSを再起動する

[1]:https://docs.oracle.com/cd/E92951_01/wls/SCPRG/thin_client.htm#GUID-1ED88231-C764-425B-94AD-A960260ACCA8


