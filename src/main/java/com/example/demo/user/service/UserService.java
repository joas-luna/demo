package com.example.demo.user.service;

import com.example.demo.user.dto.user.response.UserResponseDTO;
import com.example.demo.user.entity.User;
import com.example.demo.user.dto.user.request.UserRequestDTO;
import com.example.demo.user.mapper.UserMapper;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public ResponseEntity<?> getAll() {
        List<User> allUsers = userRepository.findAll();

        if(allUsers.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<UserResponseDTO> allUsersDTOs = userMapper.toResponsesDTOs(allUsers);

        return ResponseEntity.status(200).body(allUsersDTOs);
    }

    public ResponseEntity<?> getById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()) {
            return ResponseEntity.status(200).body(user.get());
        }

        return ResponseEntity.status(404).build();
    }

    public ResponseEntity<?> post(List<UserRequestDTO> usersRequestsDTOs) {
        List<User> newUsers = userMapper.toEntities(usersRequestsDTOs);

        List<User> savedUsers = userRepository.saveAll(newUsers);

        List<UserResponseDTO> createdUsers = userMapper.toResponsesDTOs(savedUsers);

        return ResponseEntity.status(201).body(createdUsers);
    }

    public ResponseEntity<?> put(Long id, UserRequestDTO userRequestDTO) {
        if(userRepository.findById(id).isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        User usuarioAtualizado = userMapper.toEntity(userRequestDTO);

        usuarioAtualizado.setId(id);

        userRepository.save(usuarioAtualizado);

        return getById(id);
    }

    public ResponseEntity<?> patch(Long id, UserRequestDTO userRequestDTO) {
        Optional<User> usuario = userRepository.findById(id);

        if(usuario.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        User usuarioAtualizado = usuario.get();

        userMapper.setUser(userRequestDTO ,usuarioAtualizado);

        userRepository.save(usuarioAtualizado);

        return getById(id);
    }

    public ResponseEntity<?> deleteById(Long id) {
        if(userRepository.findById(id).isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        userRepository.deleteById(id);

        if(userRepository.findById(id).isPresent()) {
            return ResponseEntity.status(500).build();
        }

        return ResponseEntity.status(204).build();
    }

    public ResponseEntity<?> deleteAll() {
        List<User> allUsers = userRepository.findAll();

        if(allUsers.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        userRepository.deleteAll();

        if(!userRepository.findAll().isEmpty()) {
            return ResponseEntity.status(500).build();
        }

        return ResponseEntity.status(204).build();
    }
}
