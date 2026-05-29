package com.example.demo.repositories;

import com.example.demo.models.entities.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
	boolean existsByVeterinarioIdAndPetIdAndDataHora(Long veterinarioId, Long petId, java.time.LocalDateTime dataHora);

	boolean existsByVeterinarioIdAndPetIdAndDataHoraAndIdNot(
			Long veterinarioId,
			Long petId,
			java.time.LocalDateTime dataHora,
			Long id
	);
}
