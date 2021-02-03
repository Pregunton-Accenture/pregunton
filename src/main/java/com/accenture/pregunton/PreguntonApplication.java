package com.accenture.pregunton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@PropertySource({
                    "classpath:application.properties",
                    "classpath:database.properties"
                })
@EntityScan("com.accenture.model")
public class PreguntonApplication {

  public static void main(String[] args) {
    SpringApplication.run(PreguntonApplication.class, args);
  }

}
