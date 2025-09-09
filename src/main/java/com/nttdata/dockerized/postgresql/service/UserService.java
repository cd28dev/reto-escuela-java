package com.nttdata.dockerized.postgresql.service;

import com.nttdata.dockerized.postgresql.model.dto.UserCreateRequestDto;
import com.nttdata.dockerized.postgresql.model.dto.UserResponseDto;
import com.nttdata.dockerized.postgresql.model.dto.UserUpdateRequestDto;

import java.util.List;

public interface UserService {

    List<UserResponseDto> listAll();
    UserResponseDto findById(Long id);
    UserResponseDto save(UserCreateRequestDto request);
    UserResponseDto update(Long id, UserUpdateRequestDto request);
    void deleteById(Long id);
}
