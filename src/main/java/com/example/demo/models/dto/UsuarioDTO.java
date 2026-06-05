package com.example.demo.models.dto;

import java.util.List;

public record UsuarioDTO(
                String nome,
                String email,
                String senha,
                String pais,
                EnderecoDTO endereco,
                List<PetDTO> pets) {
}
