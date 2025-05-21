package com.spectramd.focus.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.config.CorsRegistration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableConfigurationProperties
public class StaffServiceApplication {

    @Autowired
    private Environment env;
    
    public static void main(String[] args) {
        SpringApplication.run(StaffServiceApplication.class, args);
    }
    
    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer(){
            public void addCorsMappings(CorsRegistry registry){
                String urls = env.getProperty("cors.urls");
                CorsRegistration reg = registry.addMapping("/staff/**");
                for(String url: urls.split(",")){
                    reg.allowedOrigins(url.trim()); 
                }
            }
        };
    }
}
