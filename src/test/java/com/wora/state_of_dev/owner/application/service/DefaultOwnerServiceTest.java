package com.wora.state_of_dev.owner.application.service;

import com.wora.state_of_dev.common.domain.exception.EntityNotFoundException;
import com.wora.state_of_dev.owner.application.dto.OwnerRequestDto;
import com.wora.state_of_dev.owner.application.dto.OwnerResponseDto;
import com.wora.state_of_dev.owner.application.mapper.OwnerMapper;
import com.wora.state_of_dev.owner.domain.Owner;
import com.wora.state_of_dev.owner.domain.OwnerId;
import com.wora.state_of_dev.owner.domain.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.*;


@DisplayName("default owner tests")
@ExtendWith(MockitoExtension.class)
class DefaultOwnerServiceTest {

    @Mock
    private OwnerRepository repository;

    @Mock
    private OwnerMapper mapper;

    @InjectMocks
    private DefaultOwnerService sut;

    private Owner owner;

    @BeforeEach
    void setup() {
        owner = new Owner("mr owner").setId(new OwnerId(1L));
    }

    @Nested
    class FindAllTests {
        @Test
        void given_ownersExists_whenFindAll_shouldReturnOwnersList() {
            Owner owner1 = new Owner("mr owner 1").setId(new OwnerId(2L));
            List<Owner> expected = List.of(owner, owner1);

            given(repository.findAll()).willReturn(expected);
            given(mapper.toResponseDto(any(Owner.class))).willAnswer(invocation -> {
                Owner arg = invocation.getArgument(0);
                return new OwnerResponseDto(arg.getId().value(), arg.getName());
            });

            List<OwnerResponseDto> actual = sut.findAll();

            assertThat(actual.size()).isEqualTo(2);
            assertThat(actual).isNotNull();
            verify(repository).findAll();
            verify(mapper, times(2)).toResponseDto(any(Owner.class));
        }

        @Test
        void given_ownersDoesNotExists_whenFindAll_shouldReturnEmptyList() {
            given(repository.findAll()).willReturn(List.of());

            List<OwnerResponseDto> actual = sut.findAll();

            assertThat(actual.size()).isEqualTo(0);
            verify(repository).findAll();
            verify(mapper, never()).toResponseDto(any(Owner.class));
        }
    }

    @Nested
    class FindById {
        @Test
        void given_ownerIdNotExists_whenFindById_shouldThrowEntityNotFoundException() {
            given(repository.findById(any(OwnerId.class))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.findById(new OwnerId(3L)))
                    .withMessageContaining("owner with id 3 not found");
        }

        @Test
        void given_ExistsOwner_whenFindById_shouldReturnExistingId() {
            given(repository.findById(new OwnerId(1L))).willReturn(Optional.of(owner));
            given(mapper.toResponseDto(any(Owner.class)))
                    .willReturn(new OwnerResponseDto(owner.getId().value(), owner.getName()));

            OwnerResponseDto actual = sut.findById(DefaultOwnerServiceTest.this.owner.getId());

            assertThat(actual).isNotNull();
            assertThat(owner.getName()).isEqualTo(actual.name());
            verify(repository).findById(any(OwnerId.class));
        }
    }

    @Nested
    class CreateTests {
        @Test
        void given_ownerRequestDto_whenCreate_shouldCreateAndReturnOwner() {
            OwnerRequestDto expected = new OwnerRequestDto("mr owner");

            given(mapper.toEntity(expected)).willReturn(owner);
            given(repository.save(eq(owner))).willReturn(owner);
            given(mapper.toResponseDto(eq(owner)))
                    .willReturn(new OwnerResponseDto(owner.getId().value(), owner.getName()));

            OwnerResponseDto actual = sut.create(expected);

            assertThat(actual).isNotNull();
            assertThat(actual.name()).isEqualTo(expected.name());
            verify(repository).save(any(Owner.class));
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void given_ownerDoesNotExists_whenUpdate_shouldThrowEntityNotFoundException() {
            given(repository.findById(any(OwnerId.class))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.update(new OwnerId(3L), new OwnerRequestDto("not existing owner")))
                    .withMessageContaining("owner with id 3 not found");
        }

        @Test
        void given_ownerExists_whenUpdate_shouldUpdateAndReturnOwner() {
            given(repository.findById(any(OwnerId.class))).willReturn(Optional.of(owner));
            given(mapper.toResponseDto(any(Owner.class)))
                    .willAnswer(invocation -> {
                        Owner arg = invocation.getArgument(0);
                        return new OwnerResponseDto(arg.getId().value(), arg.getName());
                    });

            OwnerResponseDto actual = sut.update(owner.getId(), new OwnerRequestDto("new name"));

            assertThat(actual.name()).isEqualTo("new name");
            verify(repository).findById(any(OwnerId.class));
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void given_ownerDoesNotExists_whenDelete_shouldThrowEntityNotFoundException() {
            given(repository.existsById(any(OwnerId.class))).willReturn(false);

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> sut.delete(new OwnerId(2L)))
                    .withMessageContaining("owner with id 2 not found");
        }

        @Test
        void given_ownerIdExists_whenDelete_shouldDeleteOwner() {
            given(repository.existsById(any(OwnerId.class))).willReturn(true);

            sut.delete(new OwnerId(3L));

            verify(repository).existsById(any(OwnerId.class));
            verify(repository).deleteById(any(OwnerId.class));
        }
    }
}