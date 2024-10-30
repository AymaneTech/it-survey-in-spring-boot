package com.wora.state_of_dev.owner.application.service;

import com.wora.state_of_dev.common.domain.exception.EntityNotFoundException;
import com.wora.state_of_dev.owner.application.dto.OwnerRequestDto;
import com.wora.state_of_dev.owner.application.dto.OwnerResponseDto;
import com.wora.state_of_dev.owner.application.mapper.OwnerMapper;
import com.wora.state_of_dev.owner.domain.Owner;
import com.wora.state_of_dev.owner.domain.OwnerId;
import com.wora.state_of_dev.owner.domain.OwnerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    public List<OwnerResponseDto> findAll() {
        return repository.findAll()
                .stream().map(mapper::toResponseDto)
                .toList();
    }

    @Override
    public OwnerResponseDto findById(OwnerId id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("owner", id.value()));
    }

    @Override
    public OwnerResponseDto create(OwnerRequestDto dto) {
        Owner savedOwner = repository.save(mapper.toEntity(dto));
        return mapper.toResponseDto(savedOwner);
    }

    @Override
    public OwnerResponseDto update(OwnerId id, OwnerRequestDto dto) {
        Owner owner = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("owner", id.value()));

        owner.setName(dto.name());
        return mapper.toResponseDto(owner);
    }

    @Override
    public void delete(OwnerId id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("owner", id);

        repository.deleteById(id);
    }
}
