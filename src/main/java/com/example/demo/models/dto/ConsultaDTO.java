package com.example.demo.models.dto;

public record ConsultaDTO(Long id,
                          VeterinarioDTO veterinario,
                          PetDTO pet,
                          String data) {
}
