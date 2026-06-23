package com.example.demo.services;

import com.example.demo.models.dto.ConsultaDTO;
import com.example.demo.models.dto.ConsultaFormDTO;
import com.example.demo.models.entities.Consulta;
import com.example.demo.models.entities.Pet;
import com.example.demo.models.entities.Veterinario;
import com.example.demo.repositories.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.dto.ConsultaResumoDTO;

@Service
@RequiredArgsConstructor
public class ConsultaService {
    private final ConsultaRepository consultaRepository;
    private final VeterinarioService veterinarioService;
    private final PetService petService;

    public ConsultaDTO cria(ConsultaFormDTO dto) {
        Veterinario veterinario = veterinarioService.encontrarPorId(dto.veterinarioId());
        Pet pet = petService.encontrarPorId(dto.petId());
        LocalDateTime dataHora = LocalDateTime.parse(dto.data());

        validaDuplicidade(veterinario.getId(), pet.getId(), dataHora, null);

        Consulta consulta = new Consulta(veterinario, pet, dataHora);
        return consultaRepository.save(consulta).toDTO();
    }

    public List<ConsultaDTO> listarTodas() {
        return consultaRepository.findAll().stream().map(Consulta::toDTO).toList();
    }

    public ConsultaDTO buscarPorId(Long id) {
        return consultaRepository.findById(id)
                .map(Consulta::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta de id " + id + " nao encontrada"));
    }

    public ConsultaDTO atualizar(Long id, ConsultaFormDTO dto) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta de id " + id + " nao encontrada"));

        if (dto.veterinarioId() != null) {
            consulta.setVeterinario(veterinarioService.encontrarPorId(dto.veterinarioId()));
        }
        if (dto.petId() != null) {
            consulta.setPet(petService.encontrarPorId(dto.petId()));
        }
        if (dto.data() != null) {
            consulta.setDataHora(LocalDateTime.parse(dto.data()));
        }

        validaDuplicidade(
                consulta.getVeterinario().getId(),
                consulta.getPet().getId(),
                consulta.getDataHora(),
                consulta.getId());

        return consultaRepository.save(consulta).toDTO();
    }

    public void deletar(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta de id " + id + " nao encontrada"));
        consultaRepository.delete(consulta);
    }

    private void validaDuplicidade(Long veterinarioId, Long petId, LocalDateTime dataHora, Long idAtual) {
        boolean existe = (idAtual == null)
                ? consultaRepository.existsByVeterinarioIdAndPetIdAndDataHora(veterinarioId, petId, dataHora)
                : consultaRepository.existsByVeterinarioIdAndPetIdAndDataHoraAndIdNot(veterinarioId, petId, dataHora,
                        idAtual);

        if (existe) {
            throw new IllegalStateException("Ja existe consulta para este veterinario, pet e data/hora");
        }
    }

    public List<ConsultaDTO> listarPorPet(Long petId) {
        List<Consulta> consultas = consultaRepository.findByPetId(petId);
        return consultas.stream().map(Consulta::toDTO).toList();
    }

    public ConsultaResumoDTO obterResumo() {
        List<Consulta> consultas = consultaRepository.findAll();
        //FAIL FAST
        if (consultas.isEmpty()) {
            throw new ResourceNotFoundException("Nao ha consultas");
        }
        int totalConsultas = consultas.size();
        int consultasCachorros = 0;
        int consultasGatos = 0;
        int somaIdades = 0;
        Set<Pet> petsProcessados = new HashSet<>();

        for (Consulta consulta : consultas) {
            Pet pet = consulta.getPet();
            if (pet != null) {
                String tipo = pet.getTipo();
                if (tipo != null) {
                    if (tipo.equalsIgnoreCase("Cachorro") || tipo.equalsIgnoreCase("Cão")) {
                        consultasCachorros++;
                    } else if (tipo.equalsIgnoreCase("Gato")) {
                        consultasGatos++;
                    }
                }

                if (pet.getIdade() != null && !petsProcessados.contains(pet)) {
                    somaIdades += pet.getIdade();
                    petsProcessados.add(pet);
                }
            }
        }

        double idadeMedia = 0.0;
        if (!petsProcessados.isEmpty()) {
            idadeMedia = (double) somaIdades / petsProcessados.size();
        }

        return new ConsultaResumoDTO(totalConsultas, consultasCachorros, consultasGatos, idadeMedia);
    }
}
