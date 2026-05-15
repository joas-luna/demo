package com.example.demo.repositories;

import com.example.demo.models.entities.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    Optional<Endereco> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioId(Long usuarioId);
}

