package com.example.demo.user.mapper;

import com.example.demo.user.dto.user.response.UserResponseDTO;
import com.example.demo.user.entity.User;
import com.example.demo.user.dto.user.request.UserRequestDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        componentModel = "spring"
)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequestDTO userRequestDTO);

    List<User> toEntities(List<UserRequestDTO> usersRequestsDTOs);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void setUser(UserRequestDTO userRequestDTO, @MappingTarget User usuarioAtualizado);

    @BeanMapping(ignoreUnmappedSourceProperties = {"senha", "id"})
    UserResponseDTO toResponseDTO(User user);

    List<UserResponseDTO> toResponsesDTOs(List<User> users);
}
