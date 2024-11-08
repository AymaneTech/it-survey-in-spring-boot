package com.wora.stateOfDev.owner.infrastructure.web;

import com.wora.stateOfDev.owner.application.dto.OwnerRequestDto;
import com.wora.stateOfDev.owner.application.dto.OwnerResponseDto;
import com.wora.stateOfDev.owner.application.service.OwnerService;
import com.wora.stateOfDev.owner.domain.OwnerId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/owners")
@RequiredArgsConstructor
class OwnerController {
    private final OwnerService service;

    @GetMapping
    public ResponseEntity<List<OwnerResponseDto>> findAll() {
        List<OwnerResponseDto> owners = service.findAll();
        return ResponseEntity.ok(owners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerResponseDto> findById(@PathVariable Long id){
        OwnerResponseDto owner = service.findById(new OwnerId(id));
        return ResponseEntity.ok(owner);
    }

    @PostMapping
    public ResponseEntity<OwnerResponseDto> create(@RequestBody @Valid OwnerRequestDto dto) {
        OwnerResponseDto owner = service.create(dto);
        return new ResponseEntity<>(owner, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OwnerResponseDto> update(@PathVariable Long id, @RequestBody @Valid OwnerRequestDto dto) {
        OwnerResponseDto owner = service.update(new OwnerId(id), dto);
        return ResponseEntity.ok(owner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(new OwnerId(id));
        return ResponseEntity.noContent().build();
    }
}
