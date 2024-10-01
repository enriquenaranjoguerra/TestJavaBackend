package com.example.Test2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.example.Test2")
@EntityScan("com.example.Test2")
@SpringBootApplication
public class BooksApplication {

/*    public static void main(String[] args) {
        SpringApplication.run(BooksApplication.class, args);
    }*/

}
