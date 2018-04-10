# Spring Security Study

2018 소프트웨어 캡스톤 디자인 Spring Security Study

1. Spring Security에서 Redis에 세션을 저장
2. Spring Security Remember me를 이용한 자동 로그인 구현



## 기본개념

**소제목**

내용

## 시작하기

모든 소스코드는 IntelliJ + Window10 + JAVA 8 환경에서 작성되었습니다.

### MAVEN을 이용한 의존성 프로젝트 추가

이 프로젝트에서는 아래 같은 **의존성 프로젝트**가 포함되어있습니다. 

**pom.xml** 파일에 아래와 같이 **의존성 프로젝트**를 추가해 주세요.

```
   <dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.session</groupId>
        <artifactId>spring-session-core</artifactId>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>redis.clients</groupId>
        <artifactId>jedis</artifactId>
        <version>2.9.0</version>
    </dependency>
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>jstl</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.tomcat.embed</groupId>
        <artifactId>tomcat-embed-jasper</artifactId>
        <version>8.5.20</version>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.2.3</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.session</groupId>
        <artifactId>spring-session</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.session</groupId>
        <artifactId>spring-session-data-redis</artifactId>
        <version>2.0.2.RELEASE</version>
    </dependency>
</dependencies>
```
## 소스 코드

**RedisConfig.java**

@EnableRedisHttpSession 어노테이션 설정으로 세션을 redis에 저장합니다.

```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by ds on 2018-03-26.
 */

/**
 * Reids의 설정 파일
 */

@Configuration
@EnableRedisRepositories
@EnableRedisHttpSession
public class RedisConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    /**
     * Jedis는 자바에서 Redis을 사용하기 위한 클라이언트
     * @return redis 설정 객체
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        return jedisPoolConfig;
    }

    /**
     * application.properties 파일의 redis 설정을 토대로 redis커넥션 팩토리 객체 생성
     * @param jedisPoolConfig
     * @return
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
        jedisConnectionFactory.setHostName(redisHost);
        jedisConnectionFactory.setPort(redisPort);
        jedisConnectionFactory.setPassword(redisPassword);
        jedisConnectionFactory.setUsePool(true);
        return jedisConnectionFactory;
    }

    /**
     * serializer 의 각종 설정
     * tomcat context 로 설정한 쿠키 기능들도 여기서 설정
     * @return
     */
    @Bean
    public CookieSerializer cookieSerializer()
    {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        return serializer;
    }
}
```

**WebSecurityConfig.java**

세션을 redis에 저장하기 위해선 sessionRegistry을 사용해야 합니다.

```
import net.skhu.demo.repository.TokenRepositoryImpl;
import net.skhu.demo.serviceImpl.AuthProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.web.cors.CorsUtils;


/**
 * Created by ds on 2018-04-09.
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final static String REMEMBER_ME_KEY = "remember-me";

    @Autowired
    private AuthProviderImpl authProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private FindByIndexNameSessionRepository sessionRepository;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //preFlight 요청은 인증처리를 안하겠다.
                // preFlight 요청은 authrization 헤더가 줄 이유가 없으므로 401응답을 하면 안된다.
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/login-processing", "/login", "/error").permitAll()
                .antMatchers("/**").authenticated();

        http.csrf().disable();

        http.formLogin()
                .loginPage("/")
                .loginPage("/login")
                .loginProcessingUrl("/login-processing")
                .failureUrl("/error")
                .defaultSuccessUrl("/main", true)
                .usernameParameter("id")
                .passwordParameter("pw");

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", REMEMBER_ME_KEY);

        http.sessionManagement()
                //세션 허용개수 : 1개
                .maximumSessions(1)
                //이미 로그인 중일 경우 로그인이 안된다.
                //false일 경우 기존 사용자의 세션이 종료된다.
                .maxSessionsPreventsLogin(false)
                //중복 로그인이 발생했을 경우 이동할 주소(원인을 알려줄 주소)
                .expiredUrl("/error")
                //만료된 세션 전략?
                //.expiredSessionStrategy()
                //세션 레지스트리?
                .sessionRegistry(sessionRegistry());

        http.rememberMe()
                .tokenValiditySeconds(6048000)
                .key(REMEMBER_ME_KEY)
                .rememberMeServices(persistentTokenBasedRememberMeServices())
                .rememberMeCookieName(REMEMBER_ME_KEY);

        //http.authenticationProvider(authProvider);
    }

    @Bean
    public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices(){
        PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices =
                new PersistentTokenBasedRememberMeServices(REMEMBER_ME_KEY, userDetailsService, persistentTokenRepository());
        return persistentTokenBasedRememberMeServices;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        TokenRepositoryImpl tokenRepositoryImpl = new TokenRepositoryImpl();
        return tokenRepositoryImpl;
    }

    @Configuration
    public static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Autowired
        UserDetailsService userDetailsService;

        @Bean
        PasswordEncoder passwordEncoder() {
            // 스프링에서 제공하는 기본 암호 인코더
            // return new BCryptPasswordEncoder();
            // 커스텀 인코더를 사용하고있다.
            return new MyPasswordEncoder();
        }

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        }
    }

    // 암호 인코더 커스텀 설정
    public static class MyPasswordEncoder implements PasswordEncoder {
        @Override
        public String encode(CharSequence rawPassword) {
            //sha-2 / sha-3 같은 해시를 접목시킬 수 있다.
            return rawPassword.toString();
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            // rawPassword 현재 들어온 값 | encodedPassword 매칭되는 계정에 있는 값
            return encodedPassword.equals(encode(rawPassword));
        }
    }

    @Bean
    SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
    }
}
```

**TokenRepositoryImpl.java**

내용

```
import net.skhu.demo.domain.TOKEN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

/**
 * Created by ds on 2018-04-10.
 */

/**
 * PersistentTokenRepository을 구현
 */
@Transactional
public class TokenRepositoryImpl  implements PersistentTokenRepository {

    @Autowired
    TokenRepository tokenRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void createNewToken(PersistentRememberMeToken persistentRememberMeToken) {
        logger.info("createNewToken");
        TOKEN newToken = new TOKEN();
        newToken.setUsername(persistentRememberMeToken.getUsername());
        newToken.setToken(persistentRememberMeToken.getTokenValue());
        newToken.setSeries(persistentRememberMeToken.getSeries());
        newToken.setLastUsed(persistentRememberMeToken.getDate());
        logger.info(newToken.toString());
        tokenRepository.save(newToken);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        logger.info("updateToken");
        Optional<TOKEN> token = tokenRepository.findById(series);
        if(token.isPresent()) {
            TOKEN updateToken = token.get();
            updateToken.setToken(tokenValue);
            updateToken.setLastUsed(lastUsed);
            updateToken.setSeries(series);
            tokenRepository.save(updateToken);
        }
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String series) {
        logger.info("persistentToken");
        Optional<TOKEN> token = tokenRepository.findBySeries(series);
        if(token.isPresent()) {
            PersistentRememberMeToken persistentRememberMeToken =
                    new PersistentRememberMeToken(token.get().getUsername(), series, token.get().getToken(), token.get().getLastUsed());
            return persistentRememberMeToken;
        }else {
            logger.info("persistentToken failed");
        }
        return null;
    }

    @Override
    public void removeUserTokens(String username) {
        logger.info("removeToken");
        Optional<TOKEN> token = tokenRepository.findById(username);
        logger.info(token.get().toString());
        if(token.isPresent()) tokenRepository.delete(token.get());
    }
}
```

**UserDetailsServiceImpl.java**

remember me를 사용하기 위해선 AuthProvider가 아닌 UserDetailsService를 구현해 사용해 합니다.

```
import net.skhu.demo.domain.*;
import net.skhu.demo.model.UserDetails;
import net.skhu.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by ds on 2018-04-10.
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<USER> user = userRepository.findById(s);
        if(!user.isPresent()) {
            logger.info("loginFail");
            throw new UsernameNotFoundException("login Fail");
        }
        return new UserDetails(user.get());
    }
}
```

**login.jsp** 

<input id = "remember-me" name ="remember-me" type = "checkbox"/>자동 로그인

이 부분의 'remember-me' 값이 securityConfig의 remember-me 값과 일치해야 합니다.

```
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
</head>
<body>
    <form method="post" action="/login-processing">
        <label>
            <input type="text" class="form-control" placeholder="id" name="id" autofocus="autofocus">
        </label>
        <label>
            <input type="password" class="form-control" placeholder="password" name="pw">
        </label>
        <input id = "remember-me" name ="remember-me" type = "checkbox"/>자동 로그인
        <button type="submit">Login</button>
    </form>
</body>
</html>

```

## 실행하기

window 10 환경

- `jdk8` 과 `maven` 을 설치합니다.
- `JAVA_JOME` 환경변수 설정을 합니다.
- `Path`에 `maven` 환경변수 설정을 합니다.spring boot 앱 실행

```
mvn spring-boot:run
```

- 중지하려면, 키보드에서 `Crtl + C`를 누릅니다.
- `application.properties` 파일이 필요합니다.

AWS EC2 Ubuntu 환경

- `jdk8` 과 `maven` 을 설치합니다.

백 그라운드 spring boot 앱 실행

```
nohup mvn spring-boot:run&
```

- 중지하려면,  `netstat -tnlp` 명령어를 통해 프로세스를 kill 하십시오.
- `application.properties` 파일이 필요합니다.

## 사용된 도구

* [Spring-boot](https://projects.spring.io/spring-boot/) - Spring-boot Web Framework
* [Maven](https://maven.apache.org/) - 의존성 관리 프로그램
* [IntelliJ IDEA](https://www.jetbrains.com/idea/) - IDEA
* [lombok](https://projectlombok.org/) - IDEA
* [MySql](https://www.mysql.com/) - 데이터베이스
* [AWS RDS](https://aws.amazon.com/ko/rds/getting-started/) - 클라우드 환경 데이터베이스 관리 시스템
* [Redis](https://github.com/MicrosoftArchive/redis/releases) - 데이터베이스
* [bootstrapk](http://bootstrapk.com/) - CSS 도우미

## 저자

* **배다슬** - [bghgu](https://github.com/bghgu)


[기여자 목록](https://github.com/bghgu/Spring-WebSocket-Study/contributors)을 확인하여 이 프로젝트에 참가하신 분들을 보실 수 있습니다.

## 감사 인사

* http://zgundam.tistory.com/53
* https://github.com/kdevkr/spring-demo-security

---


