package com.opotromatic;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.awt.*;
import java.net.URI;

@EnableJpaRepositories("com.opotromatic.repositories")
@EntityScan("com.opotromatic.entities")
@SpringBootApplication
public class QuestionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuestionsApplication.class, args);
    }

//    @PostConstruct
//    public void openBrowser(){
//        try{
//            Desktop.getDesktop().browse(new URI("http://localhost:8081"));
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }

}
