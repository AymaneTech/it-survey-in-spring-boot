package com.wora.stateOfDev.owner.application.service;

import com.wora.stateOfDev.common.domain.exception.EntityNotFoundException;
import com.wora.stateOfDev.owner.application.dto.OwnerRequestDto;
import com.wora.stateOfDev.owner.application.dto.OwnerResponseDto;
import com.wora.stateOfDev.owner.application.mapper.OwnerMapper;
import com.wora.stateOfDev.owner.domain.Owner;
import com.wora.stateOfDev.owner.domain.OwnerId;
import com.wora.stateOfDev.owner.domain.OwnerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
class DefaultOwnerService implements OwnerService {
    private final OwnerRepository repository;
    private final OwnerMapper mapper;


    @Override
    @Cacheable(value = "owners")
    public List<OwnerResponseDto> findAll() {
        return repository.findAll()
                .stream().map(mapper::toResponseDto)
                .toList();
    }

    @Override
    @Cacheable(value = "owners", key = "#id.value()")
    public OwnerResponseDto findById(OwnerId id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("owner", id.value()));
    }

    @Override
    @CachePut(value = "owners", key = "#result.id()")
    public OwnerResponseDto create(OwnerRequestDto dto) {
        Owner savedOwner = repository.save(mapper.toEntity(dto));
        return mapper.toResponseDto(savedOwner);
    }

    @Override
    @CachePut(value = "owners", key = "#id.value()")
    public OwnerResponseDto update(OwnerId id, OwnerRequestDto dto) {
        Owner owner = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("owner", id.value()));

        owner.setName(dto.name());
        return mapper.toResponseDto(owner);
    }

    @Override
    @CacheEvict(value = "owners", allEntries = true)
    public void delete(OwnerId id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("owner", id.value());

        repository.deleteById(id);
    }
}
