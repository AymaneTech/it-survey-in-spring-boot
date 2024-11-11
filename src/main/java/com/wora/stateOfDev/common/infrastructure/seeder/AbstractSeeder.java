package com.wora.stateOfDev.common.infrastructure.seeder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public abstract class AbstractSeeder<Entity, Repository extends JpaRepository<Entity, Long>> implements CommandLineRunner {
    private final ObjectMapper objectMapper;
    private final Repository repository;
    private final String fileName;
    private final Class<Entity> entityType;

    public AbstractSeeder(Repository repository, String fileName, ObjectMapper objectMapper, Class<Entity> entityType) {
        this.repository = repository;
        this.fileName = fileName;
        this.objectMapper = objectMapper;
        this.entityType = entityType;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/" + fileName + ".json")) {
                TypeReference<List<Entity>> typeReference = new DefaultTypeReference();
                List<Entity> entities = objectMapper.readValue(inputStream, typeReference);
                repository.saveAll(entities);
            } catch (Exception e) {
                throw new RuntimeException("Error while running seeders to save " + entityType.getSimpleName());
            }
        }
    }

    class DefaultTypeReference extends TypeReference<List<Entity>> {
        @Override
        public Type getType() {
            return objectMapper.getTypeFactory().constructCollectionType(List.class, entityType);
        }
    }
}
