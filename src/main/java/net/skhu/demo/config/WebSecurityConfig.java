package net.skhu.demo.config;

import net.skhu.demo.serviceImpl.AuthProviderImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;


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
    private FindByIndexNameSessionRepository sessionRepository;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/login", "/error").permitAll()
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
                .maxSessionsPreventsLogin(true)
                //중복 로그인이 발생했을 경우 이동할 주소(원인을 알려줄 주소)
                .expiredUrl("/error")
                //만료된 세션 전략?
                //.expiredSessionStrategy()
                //세션 레지스트리?
                .sessionRegistry(sessionRegistry());

        http.rememberMe()
                //토큰 저장기간 1주일
                .tokenValiditySeconds(6048000)
                .key(REMEMBER_ME_KEY)
                .rememberMeParameter(REMEMBER_ME_KEY)
                .rememberMeCookieName(REMEMBER_ME_KEY);


        http.authenticationProvider(authProvider);
    }

    @Bean
    SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
    }

    /*@Bean
    public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(REMEMBER_ME_KEY, userDetailsService);
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setTokenValiditySeconds(60 * 60 * 24 * 31);
        rememberMeServices.setCookieName(Constants.SPRING_REMEMBER_ME_COOKIE);
        return rememberMeServices;
    }*/
}
