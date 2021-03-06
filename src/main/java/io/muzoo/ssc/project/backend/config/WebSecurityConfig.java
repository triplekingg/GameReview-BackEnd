
package io.muzoo.ssc.project.backend.config;

import io.muzoo.ssc.project.backend.SimpleResponseDTO;
import io.muzoo.ssc.project.backend.auth.OurUserDetailsService;
import io.muzoo.ssc.project.backend.util.AjaxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.script.ScriptContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private OurUserDetailsService ourUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //configuring security for REST backend APIs
        // disable csrf
        http.csrf().disable();
        //Permit root and /api/login and /api/logout
        http.authorizeRequests()
                .antMatchers("/", "/api/editdetails", "/api/login", "/api/logout", "/api/whoami", "/api/signup", "/api/review", "/api/review/fortnite", "/api/review/siege", "/api/review/fifa", "/api/review/nba", "/api/review/horizon").permitAll();
        //permit all OPTIONS requests
        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll();

        //Handle error output
        http.exceptionHandling()
                .authenticationEntryPoint(new JsonHttp403ForbiddenEntryPoint());

        // set every other path to require authentication
        http.authorizeRequests().antMatchers("/**/").authenticated();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    public UserDetailsService userDetailsService() {
        return ourUserDetailsService;
    }

    class JsonHttp403ForbiddenEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request,
                             HttpServletResponse response,
                             AuthenticationException authException) throws IOException, ServletException {
            // output JSON message
            String ajaxJson = AjaxUtils.convertToString(
                    SimpleResponseDTO
                            .builder()
                            .success(false)
                            .message("Forbidden")
                            .build()
            );
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(ajaxJson);
        }
    }

}