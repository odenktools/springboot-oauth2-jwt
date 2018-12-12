Springboot 2.0.x oauth2 JWT
============================

Sample Spring Boot 2.0.x OAuth2 JWT Authorization Server (JPA, Hibernate, PostgreSQL, Dockerize).

If you feel happy give a star to this repository.

## Features

* OAuth2.0 Authorization Server
* OAuth2.0 Resource Server
* [Liquibase](https://www.liquibase.org/)
* [Gradle 4.10.2](https://gradle.org/install/)
* Docker
* JWT Token RSA
* Sample File Upload (Using Oauth2 Resource Server)

## Running On Docker

For running on Docker instance, it's really simple.

* Clone this repo
* Run ```docker-compose up -d```

## Testing Apps

Login server-server

```bash
curl --request POST \
  --user server-server:server-server \
  --url http://localhost:8090/oauth/token \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --data grant_type=client_credentials
```

the result will be like

```json
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2VfaWQiXSwic2NvcGUiOlsicmVhZCIsIndyaXRlIiwidHJ1c3QiXSwiZXhwIjoxNTQ0NjI4MTQ1LCJqdGkiOiI1NDVjMWM3MC0xNDMxLTQxZTItYTg4MS03MWIyZDNhNmNkYTQiLCJjbGllbnRfaWQiOiJzZXJ2ZXItc2VydmVyIn0.EheboSEeO-_bvhYjBrX0bdZ0pDBCzHg02hKTBCVQuqOF6jcz7ZSMonC-i9ErHmmlx9ZDKW2EChzfltxhpcYcV0jZwR7eQSWT9CVUjl8jtkYrWTqf8qOy3R_t9sC7KeJSmLOiYSZ0FyBfiP6Fa65UjDvL5K1cxzYP75AjPBNwIdHr5vP4NGdI0XWOWNHrm4lKqXcBRiD9iS-dMRc5zkMfD4Mtp8clrMbIROlQOH3u9g6XudhB-LkwzjzQdyH2kQrTqbIPOGchgfluhcR0db95KptM0BVT-KFaNY-YxpCKPJRTGIoqa8ogS-8T3vZlZvKWsxj4Rpn1zLiRcblav0dO6w",
    "token_type": "bearer",
    "expires_in": 450,
    "scope": "read write trust",
    "jti": "545c1c70-1431-41e2-a881-71b2d3a6cda4"
}
```

Login customer (end user for your apps)

```bash
curl --request POST \
  --user android-client:android-client \
  --url http://localhost:8090/oauth/token \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --data 'username=customer_one&grant_type=password&password=customer_one'
```

the result will be like

```json
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2VfaWQiXSwidXNlcl9uYW1lIjoiY3VzdG9tZXJfb25lIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIiwidHJ1c3QiXSwiZXhwIjoxNTQ0NjMxNTU1LCJhdXRob3JpdGllcyI6WyJST0xFX01PQklMRSIsIlVQREFURV9DT01QQU5ZIiwiUkVBRF9DT01QQU5ZIiwiUkVBRF9BUElLRVkiXSwianRpIjoiNzQ1OGY3YzctYWNhOC00ZGVlLWJmMDAtOGI0YTJkYWU1YTgyIiwiY2xpZW50X2lkIjoiYW5kcm9pZC1jbGllbnQifQ.NsUtA-VnfVkFFEYnsypv8_fSXT3b0JBWBxXAo_5DHOZ9MEk3PUtNcIkIjsZm1kF6G9j8fCTENSq5-EMKChfywvkhiyvmKxX7j52WKqes8SWU2e2vrDCHfs3xXQTH8x4ISdMTMz88hyt2n8fMIPCn6CtNBdv2FUZsRdfV2JBSLEteV55R4GVs0x7dWc--OyQqvySWsZ0W2YtBh2kSXG1OsIwAXbnPpGRPckBlef7QuG2XEr7CfKQZ5rIy5OqFyI-HvR8q-eA_ZNn70GeAJiVY7Op5FMHMUa70aB5sdiTK2jp_dcoEsLE3WYS4d66maUqnRFbEBNWL6h4T-BjosiVWeQ",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2VfaWQiXSwidXNlcl9uYW1lIjoiY3VzdG9tZXJfb25lIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIiwidHJ1c3QiXSwiYXRpIjoiNzQ1OGY3YzctYWNhOC00ZGVlLWJmMDAtOGI0YTJkYWU1YTgyIiwiZXhwIjoxNTQ0NjMxNTU1LCJhdXRob3JpdGllcyI6WyJST0xFX01PQklMRSIsIlVQREFURV9DT01QQU5ZIiwiUkVBRF9DT01QQU5ZIiwiUkVBRF9BUElLRVkiXSwianRpIjoiODdiN2JkY2YtNWFmOC00ZGEwLWJiM2MtNGZlZjkwNzRmYzBhIiwiY2xpZW50X2lkIjoiYW5kcm9pZC1jbGllbnQifQ.JXBdkfD5HTxlMYf1hm8H3QKQ9dhhAMoH1gbl_Lv3SALXOyOVuc3N_OvM5YEBbWMwXfJlHPV07uGnMovFwINV2OPGUkaqzHIC-aCQr-__n8rDYmJRmU-48PiiOdV-nZgIwTUTZEkz1DxI4S4ZvZci59VNOUUyq8oGRUmKmDBTtqW4UGGaElX5xmAecE-qbXpj77VyWv4uOJoToO53_-hPfBO31fRXiEEO0lzFEOq_yeNg1M6NFekqeGqPXQZgOgzbnJcv4EIXueB9phDDD6zLab3iAmgDQdKZI8boVLDHek5PfdJd10A2RjMvjepMq5SD7W1dCXn_3lZRqoGbBR8PVQ",
    "expires_in": 499,
    "scope": "read write trust",
    "jti": "7458f7c7-aca8-4dee-bf00-8b4a2dae5a82"
}
```

Customer Check Oauth2 Token

```bash
curl --request POST \
  --user android-client:android-client \
  --url http://localhost:8090/oauth/check_token \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --data token={{YOUR_ACCESS_TOKEN}}
```

the result will be like

```json
{
    "aud": [
        "resource_id"
    ],
    "user_name": "customer_one",
    "scope": [
        "read",
        "write",
        "trust"
    ],
    "active": true,
    "exp": 1544631555,
    "authorities": [
        "ROLE_MOBILE",
        "UPDATE_COMPANY",
        "READ_COMPANY",
        "READ_APIKEY"
    ],
    "jti": "7458f7c7-aca8-4dee-bf00-8b4a2dae5a82",
    "client_id": "android-client"
}
```

Customer Refresh Oauth2 Token

```bash
curl --request POST \
  --url http://localhost:8090/oauth/token \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --data 'grant_type=refresh_token&token={{YOUR_ACCESS_TOKEN}}&refresh_token={{YOUR_REFRESH_TOKEN}}'
```

Access Customer Profile

```bash
curl --request GET \
  --url http://localhost:8091/api/v1/customer/me \
  --header 'Authorization: Bearer {{YOUR_ACCESS_TOKEN}}' \
```

the result will be like

```json
{
    "messages": "Welcome again ``customer_one``. And Happy nice day!"
}
```

File Upload

```bash
curl --request POST \
  --url http://localhost:8091/api/v1/file/upload \
  --header 'Authorization: Bearer {{YOUR_ACCESS_TOKEN}}' \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --header 'content-type: multipart/form-data' \
  --form 'file=@/opt/pictures/your-images.jpg'
```

## Build Application from source

First clone this repo. To build the sources you need to have [Gradle 4x](https://gradle.org/install/) installed.

After the clone, create database

- For Authorization Server

```bash
su - postgres
createdb auth_server
```

- For Resource Server

```bash
su - postgres
createdb file_server
```

After create database, build the entire project.

For Authorization Server

```bash
cd authorization_server
gradle build
```

For Resource Server

```bash
cd resource_server
gradle build
```

Generate RSA KeyStore

```bash
keytool -genkey -alias jwt -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore jwt.p12 -keypass odenktools123 -validity 3650
```

```bash
keytool -list -rfc --keystore jwt.p12 | openssl x509 -inform pem -pubkey
```

## Oauth2 user credential

User : server-server

Password : server-server

User : android-client

Password : android-client

User : external-server

Password : external-server


## Customer credential

User : customer_one

Password : customer_one

## Todos

- [] Admin UI for manage users and customers credentials

## References

[Official Oauth2 Sample Code](https://oauth.net/code/)

[Oauth Playground](https://developers.google.com/oauthplayground/)

[Spring Oauth2 Docs](https://projects.spring.io/spring-security-oauth/docs/oauth2.html)

[Secure Server To Server](https://dzone.com/articles/build-a-spring-boot-app-with-secure-server-to-serv)

[Tutorial Oauth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)

[Okta](https://developer.okta.com/blog/2018/04/02/client-creds-with-spring-boot)

# LICENSE

MIT License

Copyright (c) 2018 odenktools

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
