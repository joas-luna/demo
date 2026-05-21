package com.example.demo.user.model.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    @NotBlank
    @Pattern(regexp = "^[A-Za-z]{3,12}$")
    private String nome;

    @NotBlank
    private Date dataDeNascimento;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^.{7,21}$")
    private String senha;

    private String pais;
}
