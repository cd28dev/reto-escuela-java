package cd.dev.userms.service;


import cd.dev.userms.model.dto.UserCreateRequestDto;
import cd.dev.userms.model.dto.UserResponseDto;
import cd.dev.userms.model.dto.UserUpdateRequestDto;

import java.util.List;

public interface UserService {

    List<UserResponseDto> listAll();
    UserResponseDto findById(Long id);
    UserResponseDto save(UserCreateRequestDto request);
    UserResponseDto update(Long id, UserUpdateRequestDto request);
    void deleteById(Long id);
}