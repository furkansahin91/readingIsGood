package com.getir;

import com.getir.security.JWTAuthorizationFilter;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.ws.rs.HttpMethod;


@SpringBootApplication
public class ReadingisgoodApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReadingisgoodApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Books API").version("1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

    @EnableWebSecurity
    @Configuration
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable().headers().disable()
                    .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/books/login").permitAll()
                    .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/h2-console/**").permitAll()
                    .anyRequest().authenticated();
        }

        @Override
        public void configure(WebSecurity web)  {
            web.ignoring().antMatchers("/v3/api-docs",
                    "/swagger-ui.html",
                    "/swagger-ui/**");
        }
    }

}
