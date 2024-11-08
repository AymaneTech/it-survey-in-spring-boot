package com.wora.state_of_dev.user.infrastructure.seeder;

import com.wora.state_of_dev.user.domain.entity.Authority;
import com.wora.state_of_dev.user.domain.entity.Role;
import com.wora.state_of_dev.user.domain.repository.AuthorityRepository;
import com.wora.state_of_dev.user.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserSeeder {
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    @Bean
    @Order(1)
    public CommandLineRunner saveAuthorities() {
        return args -> {
            System.out.println("--- Authorities Command Line Runner ---");
            if (authorityRepository.count() != 0) return;

            System.out.println("--- Inserting the authorities ---");
            authorityRepository.saveAll(getDefaultAuthorities());
        };
    }

    @Bean
    @Order(2)
    public CommandLineRunner saveRoles() {
        return args -> {
            System.out.println("--- Roles Command Line Runner ---");
            if (roleRepository.count() != 0) return;

            System.out.println("--- Inserting roles with their authorities ---");
            List<Authority> authorities = authorityRepository.findAll();
            roleRepository.saveAll(List.of(
                    new Role("ADMIN", authorities),
                    new Role("USER", authorities),
                    new Role("OWNER", authorities)
            ));
        };
    }

    private List<Authority> getDefaultAuthorities() {
        return List.of(
                // Survey
                new Authority("CREATE_SURVEY", "survey:create"),
                new Authority("READ_SURVEY", "survey:read"),
                new Authority("UPDATE_SURVEY", "survey:update"),
                new Authority("DELETE_SURVEY", "survey:delete"),

                // Survey Edition
                new Authority("CREATE_SURVEY_EDITION", "surveyEdition:create"),
                new Authority("READ_SURVEY_EDITION", "surveyEdition:read"),
                new Authority("UPDATE_SURVEY_EDITION", "surveyEdition:update"),
                new Authority("DELETE_SURVEY_EDITION", "surveyEdition:delete"),

                // Question
                new Authority("CREATE_QUESTION", "question:create"),
                new Authority("READ_QUESTION", "question:read"),
                new Authority("UPDATE_QUESTION", "question:update"),
                new Authority("DELETE_QUESTION", "question:delete"),

                // Answer
                new Authority("CREATE_ANSWER", "answer:create"),
                new Authority("READ_ANSWER", "answer:read"),
                new Authority("UPDATE_ANSWER", "answer:update"),
                new Authority("DELETE_ANSWER", "answer:delete"),

                // Chapter
                new Authority("CREATE_CHAPTER", "chapter:create"),
                new Authority("READ_CHAPTER", "chapter:read"),
                new Authority("UPDATE_CHAPTER", "chapter:update"),
                new Authority("DELETE_CHAPTER", "chapter:delete"),

                // Owner
                new Authority("CREATE_OWNER", "owner:create"),
                new Authority("READ_OWNER", "owner:read"),
                new Authority("UPDATE_OWNER", "owner:update"),
                new Authority("DELETE_OWNER", "owner:delete"),

                // User
                new Authority("CREATE_USER", "user:create"),
                new Authority("READ_USER", "user:read"),
                new Authority("UPDATE_USER", "user:update"),
                new Authority("DELETE_USER", "user:delete"),

                // Role
                new Authority("CREATE_ROLE", "role:create"),
                new Authority("READ_ROLE", "role:read"),
                new Authority("UPDATE_ROLE", "role:update"),
                new Authority("DELETE_ROLE", "role:delete"),

                // Authority
                new Authority("CREATE_AUTHORITY", "authority:create"),
                new Authority("READ_AUTHORITY", "authority:read"),
                new Authority("UPDATE_AUTHORITY", "authority:update"),
                new Authority("DELETE_AUTHORITY", "authority:delete")
        );
    }
}
