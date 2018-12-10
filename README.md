# Springboot 2.0.x oauth2 JWT

Spring Boot 2.0.x OAuth2 JWT Authorization Server with Database (JPA, Hibernate, PostgreSQL)

```bash
keytool -genkey -alias jwt -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore jwt.p12 -keypass odenktools123 -validity 3650
```

```bash
keytool -list -rfc --keystore jwt.jks | openssl x509 -inform pem -pubkey
```

[Oauth Playground](https://developers.google.com/oauthplayground/)

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
