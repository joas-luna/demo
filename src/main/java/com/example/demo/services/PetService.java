package com.example.demo.services;

import com.example.demo.models.dto.PetFormDTO;
import com.example.demo.models.dto.PetResponseDTO;
import com.example.demo.models.entities.Pet;
import com.example.demo.models.entities.Usuario;
import com.example.demo.repositories.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import com.example.demo.exceptions.ResourceNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final UsuarioService usuarioService;

    public PetResponseDTO criar(PetFormDTO dto) {
        Usuario dono = usuarioService.encontraUsuarioPorId(dto.donoId());
        Pet pet = new Pet();
        pet.setNome(dto.nome());
        pet.setTipo(dto.tipo());
        pet.setIdade(dto.idade());
        pet.setDono(dono);
        Pet salvo = petRepository.save(pet);
        log.info("Pet id {} criado para usuário id {}", salvo.getId(), dto.donoId());
        return PetResponseDTO.from(salvo);
    }

    public List<PetResponseDTO> listarPets(Long donoId) {
        List<Pet> pets = (donoId == null)
                ? petRepository.findAll()
                : petRepository.findByDonoId(donoId);
        return pets.stream().map(PetResponseDTO::from).toList();
    }

    public PetResponseDTO buscarPorId(Long id) {
        Pet pet = encontrarPorId(id);
        return PetResponseDTO.from(pet);
    }

    public Pet encontrarPorId(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet de id " + id + " não encontrado"));
    }

    public PetResponseDTO atualizar(Long id, PetFormDTO dto) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet de id " + id + " não encontrado"));
        if (dto.nome() != null) pet.setNome(dto.nome());
        if (dto.tipo() != null) pet.setTipo(dto.tipo());
        if (dto.idade() != null) pet.setIdade(dto.idade());
        if (dto.donoId() != null && !dto.donoId().equals(pet.getDono().getId())) {
            pet.setDono(usuarioService.encontraUsuarioPorId(dto.donoId()));
        }
        log.info("Pet id {} atualizado", id);
        return PetResponseDTO.from(petRepository.save(pet));
    }

    public void deletar(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet de id " + id + " não encontrado"));
        petRepository.delete(pet);
        log.info("Pet id {} deletado", id);
    }
}
