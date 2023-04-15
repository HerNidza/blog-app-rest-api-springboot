package com.nikola.blog;

import com.nikola.blog.entity.Role;
import com.nikola.blog.entity.User;
import com.nikola.blog.repository.RoleRepository;
import com.nikola.blog.repository.UserRepository;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.hibernate.cfg.Environment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(title = "Spring Boot Blog App REST APIs",
                description = "Spring Boot Blog App REST APIs Documentation",
                version = "v1.0",
                contact = @Contact(name = "Nikola",email = "nikola.stankov13@mgail.com"),
                license = @License(name = "Nikola's Licence")
        ),
        externalDocs = @ExternalDocumentation(
                description = "Spring Boot Blog App Documentation",
                url = "https://github.com/HerNidza/blog-project-spring-boot-rest-api"
        )
)
public class BlogApplication  implements CommandLineRunner{
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {

            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);

            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
    }
}
