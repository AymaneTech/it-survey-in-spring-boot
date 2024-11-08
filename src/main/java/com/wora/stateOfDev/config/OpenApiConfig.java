package com.wora.stateOfDev.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI defineOpenApi(){
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        Contact contact = new Contact();
        contact.setName("Aymane El Maini");
        contact.setEmail("elmainiaymane03@gmail.com");
        contact.setUrl("https://aymaneelmaini.vercel.app");

        Info info = new Info()
                .title("It survey management system API")
                .version("1.0")
                .description("the api exposes endpoints to manage and play it surveys")
                .contact(contact);

        return new OpenAPI().info(info).servers(List.of(server));

    }

}
