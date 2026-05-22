package com.example.demo.user;

import com.example.demo.user.model.dto.responses.UserResponseDTO;
import com.example.demo.user.model.entities.UserEntity;
import com.example.demo.user.model.dto.requests.UserRequestDTO;
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
        List<UserEntity> allUsers = userRepository.findAll();

        if(allUsers.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<UserResponseDTO> allUsersDTOs = userMapper.toUserResponseDTOList(allUsers);

        return ResponseEntity.status(200).body(allUsersDTOs);
    }

    public ResponseEntity<?> getById(Long id) {
        Optional<UserEntity> user = userRepository.findById(id);

        if(user.isPresent()) {
            return ResponseEntity.status(200).body(user.get());
        }

        return ResponseEntity.status(404).build();
    }

    public ResponseEntity<?> post(UserRequestDTO userRequestDTO) {
        UserEntity novoUsuario = userMapper.toUserEntity(userRequestDTO);

        userRepository.save(novoUsuario);

        return getById(novoUsuario.getId());
    }

    public ResponseEntity<?> put(Long id, UserRequestDTO userRequestDTO) {
        if(userRepository.findById(id).isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        UserEntity usuarioAtualizado = userMapper.toUserEntity(userRequestDTO);

        usuarioAtualizado.setId(id);

        userRepository.save(usuarioAtualizado);

        return getById(id);
    }

    public ResponseEntity<?> patch(Long id, UserRequestDTO userRequestDTO) {
        Optional<UserEntity> usuario = userRepository.findById(id);

        if(usuario.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        UserEntity usuarioAtualizado = usuario.get();

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
        List<UserEntity> allUsers = userRepository.findAll();

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
