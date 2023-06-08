package com.mor.backend;

import com.mor.backend.config.AppProperties;
import com.mor.backend.services.SlackService;
import com.mor.backend.util.ImageUpload;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.Resource;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Demo API", version = "1.0", description = "Demo Info"))
@SecurityScheme(name = "demo", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@EnableConfigurationProperties(AppProperties.class)
public class BackendApplication implements CommandLineRunner {
    @Resource
    ImageUpload storageService;
    @Autowired
    SlackService slackService;
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... arg) throws Exception {
//        slackService.sendMessage("C05A40B52VA","test");
        storageService.init();

    }

}
