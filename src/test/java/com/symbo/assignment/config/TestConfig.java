package com.symbo.assignment.config;

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@ComponentScan(basePackages = {"com.symbo.assignment"})
public class TestConfig {

    Mongo getMongo(){
        Fongo fongo = new Fongo("dummy");
        return fongo.getMongo();
    }

    @Bean
    MongoTemplate getMongoTemplate(){
        MongoTemplate template = new MongoTemplate(getMongo(),"test-db");
        return template;
    }
    @Bean
    EmailValidator getEmailValidator(){
        return new EmailValidator();
    }

}
