package com.example.demo.user.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    @NotBlank
    @Pattern(regexp = "^[A-Za-z]{3,12}$")
    private String nome;

    @NotNull
    private LocalDate dataDeNascimento;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^.{7,21}$")
    private String senha;

    private String pais;
}
