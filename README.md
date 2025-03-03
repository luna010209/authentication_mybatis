# Set up for project

## [File gradle](build.gradle)

```java
dependencies {
	...
  // Add jwt dependency.
	implementation 'io.jsonwebtoken:jjwt-api:0.11.5'
	runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.11.5'
	runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.11.5'
  ...
}
```

## [Set up file application.yaml](src\main\resources\application.yaml)

### Connect with MySQL

```java
  datasource:
    url: jdbc:mysql://<ip address>:<port>/<db>
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: <username>
    password: <password>
```

### Set up to login with naver, kakao
```java
security:
    oauth2:
      client:
        provider:
          naver:
            authorization-uri: https://nid.naver.com/oauth2.0/authorize
            token-uri: https://nid.naver.com/oauth2.0/token
            user-info-uri: https://openapi.naver.com/v1/nid/me
            user-name-attribute: response

          kakao:
            authorization-uri: https://kauth.kakao.com/oauth/authorize
            token-uri: https://kauth.kakao.com/oauth/token
            user-info-uri: https://kapi.kakao.com/v2/user/me
            user-name-attribute: id

        registration:
          naver:
            client-id: <client-id>
            client-secret: <client-secret>
            redirect-uri: http://127.0.0.1:8080/login/oauth2/code/naver
            authorization-grant-type: authorization_code
            client-authentication-method: client_secret_post
            client-name: Naver
            scope:
              - name
              - email
              - username
              - phone

          kakao:
            client-id: <client-id>
            redirect-uri: http://127.0.0.1:8080/login/oauth2/code/kakao
            authorization-grant-type: authorization_code
            client-authentication-method: client_secret_post
            scope:
              - account_email
              - phone_number
              - name
              - profile_nickname
```

### Mapper with xml file
```java
mybatis:
  mapper-locations: classpath:mapper/*.xml
```