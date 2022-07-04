package animalhospital.config;

import animalhospital.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@EnableWebSecurity
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfig {

    private final MemberService memberService;



    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
               /* .antMatchers("/admin/**").hasRole("ADMIN") */
                .antMatchers("/member/info").hasRole("MEMBER")
                .antMatchers("/board/write").hasRole("MEMBER")
                .antMatchers("/**").permitAll()
                .antMatchers("member/login").permitAll()
                .and()
               /* .formLogin()
                .loginProcessingUrl("/member/adminlogincontroller")
                .usernameParameter("mid")
                .passwordParameter("mpassword")
                .defaultSuccessUrl("/")
                .failureUrl("/member/login/error")
                .and() */
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(("/member/logout") ) )
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .and()
                .csrf()
                .ignoringAntMatchers("/member/login")
                .ignoringAntMatchers("/member/adminlogincontroller")
                .ignoringAntMatchers("/board/write")
                .ignoringAntMatchers("/board/blist")
                .ignoringAntMatchers("/member/delete")
                .ignoringAntMatchers("/board/getnotice")
                .ignoringAntMatchers("/admin/noticesave")
                .ignoringAntMatchers("/admin/updatenotice")
                .ignoringAntMatchers("/admin/deletenotice")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/error")
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(memberService);

        return http.build();
    }





}