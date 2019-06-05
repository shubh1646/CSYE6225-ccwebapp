package com.neu.webapp.dependencies;

import com.neu.webapp.validators.UserValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class BeanFactory {
    @Bean
    public BCryptPasswordEncoder pwdEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserValidator userValidator(){
        return new UserValidator();
    }

    @Bean
    public BasicAuthenticationEntryPoint authenticationEntryPoint() {
        return new BasicAuthenticationEntryPoint() {
            @Override
            public void afterPropertiesSet() throws Exception {
                setRealmName("webapp");
                super.afterPropertiesSet();
            }

            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                response.addHeader("WWW-Authenticate", "Basic realm = "+getRealmName());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println("401 - UNAUTHORIZED");
            }
        };
    }
}
