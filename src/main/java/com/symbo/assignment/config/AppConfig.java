package com.symbo.assignment.config;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    EmailValidator getEmailValidator(){
        return new EmailValidator();
    }
}
