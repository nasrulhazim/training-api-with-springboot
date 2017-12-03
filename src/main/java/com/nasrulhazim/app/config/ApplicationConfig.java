package com.nasrulhazim.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class ApplicationConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BasicAuthenticationPoint basicAuthenticationPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Disable CSRF - this should for an API only
        http.csrf()
                .disable();

        // permit access to landing page
        http.authorizeRequests()
                .antMatchers("/")
                .permitAll()
                .anyRequest()
                .authenticated();

        // Purpose of the BasicAuthenticationEntryPoint class is to set
        // the "WWW-Authenticate" header to the response.
        // So, web browsers will display a dialog to enter usename and password
        // based on basic authentication mechanism(WWW-Authenticate header)
        http.httpBasic()
                .authenticationEntryPoint(basicAuthenticationPoint);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // added username, password, and userole for the in-memory user
        auth.inMemoryAuthentication()
                .withUser("nasrul")
                .password("password")
                .roles("USER");
    }

}
