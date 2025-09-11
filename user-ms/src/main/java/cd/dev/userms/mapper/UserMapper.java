package cd.dev.userms.mapper;


import cd.dev.userms.model.dto.UserCreateRequestDto;
import cd.dev.userms.model.dto.UserResponseDto;
import cd.dev.userms.model.dto.UserUpdateRequestDto;
import cd.dev.userms.model.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toResponseDto(User user);
    List<UserResponseDto> toResponseDtoList(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "pedidos", ignore = true)
    User toEntity(UserCreateRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UserUpdateRequestDto dto, @MappingTarget User user);
}