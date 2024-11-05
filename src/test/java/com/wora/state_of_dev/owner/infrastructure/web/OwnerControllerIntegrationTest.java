package com.wora.state_of_dev.owner.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wora.state_of_dev.common.application.validation.validator.UniqueFieldValidator;
import com.wora.state_of_dev.common.config.TestConfig;
import com.wora.state_of_dev.common.infrastructure.web.GlobalExceptionHandler;
import com.wora.state_of_dev.owner.application.dto.OwnerRequestDto;
import com.wora.state_of_dev.owner.application.dto.OwnerResponseDto;
import com.wora.state_of_dev.owner.application.service.OwnerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.wora.state_of_dev.common.infrastructure.web.GlobalExceptionHandler.ENTITY_NOT_FOUND_MESSAGE;
import static com.wora.state_of_dev.common.infrastructure.web.GlobalExceptionHandler.VALIDATION_FAILED_MESSAGE;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import({GlobalExceptionHandler.class})
@ActiveProfiles("test")
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class OwnerControllerIntegrationTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final OwnerService ownerService;

    private OwnerResponseDto createdOwner;

    @BeforeEach
    void setUp() {
        OwnerRequestDto validRequestDto = new OwnerRequestDto("aymane el Maini");
        createdOwner = ownerService.create(validRequestDto);
    }

    @Test
    @Rollback
    void givenOwnersExist_whenFindAll_shouldReturnListOfOwners() throws Exception {
        mockMvc.perform(get("/api/v1/owners"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(createdOwner.id()))
                .andExpect(jsonPath("$[0].name").value(createdOwner.name()));
    }

    @Nested
    class FindByIdTests {
        @Test
        @Rollback
        void givenOwnerIdDoesNotExist_whenFindById_shouldReturnNotFound() throws Exception {
            Long id = 1000L;
            mockMvc.perform(get("/api/v1/owners/{id}", id))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }

        @Test
        @Rollback
        void givenOwnerIdExists_whenFindById_shouldReturnFoundOwner() throws Exception {
            mockMvc.perform(get("/api/v1/owners/{id}", createdOwner.id()))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class CreateTests {
        @Test
        @Rollback
        void givenCorrectOwnerRequest_whenCreate_shouldReturnCreatedOwner() throws Exception {
            OwnerRequestDto newOwner = new OwnerRequestDto("yahya el maini");

            mockMvc.perform(post("/api/v1/owners")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(newOwner)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value(newOwner.name()));
        }

        @Test
        @Rollback
        void givenInvalidRequest_whenCreate_shouldReturnBadRequest() throws Exception {
            OwnerRequestDto newOwner = new OwnerRequestDto("");
            mockMvc.perform(post("/api/v1/owners")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(newOwner)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value(VALIDATION_FAILED_MESSAGE))
                    .andExpect(jsonPath("$.errors.name").value("must not be blank"));
        }
    }

    @Nested
    class UpdateTests {
        @Test
        @Rollback
        void givenCorrectOwnerRequestAndExistentId_whenUpdate_shouldReturnUpdatedOwner() throws Exception {
            OwnerRequestDto updateRequest = new OwnerRequestDto("new name motherfucker");

            mockMvc.perform(put("/api/v1/owners/{id}", createdOwner.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value(updateRequest.name()))
                    .andExpect(jsonPath("$.id").value(createdOwner.id()));
        }

        @Test
        @Rollback
        void givenCorrectOwnerRequestAndNotExistentId_whenUpdate_shouldReturnNotFound() throws Exception {
            OwnerRequestDto updateRequest = new OwnerRequestDto("new name motherfucker");

            mockMvc.perform(put("/api/v1/owners/{id}", 444L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }

        @Test
        @Rollback
        void givenInvalidRequest_whenUpdate_shouldReturnBadRequest() throws Exception {
            OwnerRequestDto updateRequest = new OwnerRequestDto("");
            mockMvc.perform(put("/api/v1/owners/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value(VALIDATION_FAILED_MESSAGE))
                    .andExpect(jsonPath("$.errors.name").value("must not be blank"));
        }
    }

    @Nested
    class DeleteTests {

        @Test
        @Rollback
        void givenExistentId_whenDelete_shouldDeleteOwnerAndReturnNoContent() throws Exception {
            System.out.println(ownerService.findAll());
            mockMvc.perform(delete("/api/v1/owners/{id}", createdOwner.id()))
                    .andExpect(status().isNoContent());
        }

        @Test
        @Rollback
        void givenNoExistentId_whenDelete_shouldReturnNotFound() throws Exception {
            mockMvc.perform(delete("/api/v1/owners/{id}", 30303L))
                    .andExpect(status().isNotFound());
        }
    }
}
