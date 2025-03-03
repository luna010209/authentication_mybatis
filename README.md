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

## [Configuration](src\main\java\com\example\authentication_mybatis\config) and [Handler exception](src\main\java\com\example\authentication_mybatis\exception)

### [Security configuration](src\main\java\com\example\authentication_mybatis\config\SecurityConfig.java)
```java
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->{
                    auth.requestMatchers("...").authenticated();
                    auth.anyRequest().permitAll();
                });
        return http.build();
    }
```
### [Password configuration](src\main\java\com\example\authentication_mybatis\config\PasswordConfig.java)
```java
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
```

### [Handler exception](src\main\java\com\example\authentication_mybatis\exception\ExceptionController.java)
```java
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> customExceptionHandler(CustomException e){
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }
```

## Create user account

### [Dto](src\main\java\com\example\authentication_mybatis\user\dto)
```java
public class UserRequest {
    private String username;
    private String password;
    private String pwConfirm;
    private String email;
    private String name;
}

public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String name;
}
```
### [Mapper](src\main\java\com\example\authentication_mybatis\user\mapper\UserMapper.java) with xml file

```java
@Mapper
public interface UserMapper {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void createUser(UserDto dto);
}
```
File [user.xml](src\main\resources\mapper\user.xml)
```java
<mapper namespace="com.example.authentication_mybatis.user.mapper.UserMapper">

    <select id="existsByUsername" resultType="boolean" parameterType="string">
        SELECT COUNT(*) FROM user_account u WHERE u.username=#{username};
    </select>

    <select id="existsByEmail" resultType="boolean" parameterType="string">
        SELECT COUNT(*) FROM user_account u WHERE u.email = #{email};
    </select>

    <insert id="createUser" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO user_account(username, email, name)
        VALUES (#{username}, #{email}, #{name})
    </insert>

</mapper>
```

### [User Service](src\main\java\com\example\authentication_mybatis\user\service\UserService.java)
```java
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    public UserDto newUser(UserRequest request){
        if (userMapper.existsByUsername(request.getUsername()))
            throw new CustomException(HttpStatus.BAD_REQUEST, "Username already exists!");
        else if (userMapper.existsByEmail(request.getEmail()))
            throw new CustomException(HttpStatus.BAD_REQUEST, "Email already exists!");
        else if (!request.getPassword().equals(request.getPwConfirm()))
            throw new CustomException(HttpStatus.CONFLICT, "Password and password confirm do not match!");
        UserDto dto = UserDto.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .name(request.getName())
                .build();
        userMapper.createUser(dto);
        return dto;
    }
}
```

### [User Controller](src\main\java\com\example\authentication_mybatis\user\UserController.java)
```java
@PostMapping
    public UserDto newUser(@RequestBody UserRequest request){
        return userService.newUser(request);
    }
```