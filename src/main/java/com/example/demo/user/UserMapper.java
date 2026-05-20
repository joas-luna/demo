package com.example.demo.user;

import com.example.demo.user.model.UserEntity;
import com.example.demo.user.model.dto.requests.UserRequestDTO;
import org.mapstruct.*;


@Mapper(
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        componentModel = "spring"
)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    UserEntity toUserEntity(UserRequestDTO userRequestDTO);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void setUser(UserRequestDTO userRequestDTO, @MappingTarget UserEntity usuarioAtualizado);
}
