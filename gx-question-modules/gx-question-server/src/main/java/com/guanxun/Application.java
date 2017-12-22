package com.guanxun;

import org.apache.log4j.*;
import org.mybatis.spring.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.*;
import springfox.documentation.swagger2.annotations.*;

/**
 * Swagger访问地址：http://localhost:8080/swagger-ui.html
 */

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan
@EnableSwagger2
@MapperScan("com.guanxun.mapper")
public class Application{
    private static Logger logger = Logger.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info("SpringBoot Start Success");
    }

}
