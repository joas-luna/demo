package com.example.demo.models.dto;

public record PetFormDTO(
        String nome,
        String tipo,
        Integer idade,
        Long donoId) {
}