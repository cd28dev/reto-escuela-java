package com.nttdata.dockerized.postgresql.service.impl;

import com.nttdata.dockerized.postgresql.exception.BadRequestException;
import com.nttdata.dockerized.postgresql.exception.NotFoundException;
import com.nttdata.dockerized.postgresql.mapper.UserMapper;
import com.nttdata.dockerized.postgresql.model.dto.UserCreateRequestDto;
import com.nttdata.dockerized.postgresql.model.dto.UserResponseDto;
import com.nttdata.dockerized.postgresql.model.dto.UserUpdateRequestDto;
import com.nttdata.dockerized.postgresql.model.entity.User;
import com.nttdata.dockerized.postgresql.repository.UserRepository;
import com.nttdata.dockerized.postgresql.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserResponseDto> listAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return userMapper.toResponseDtoList(users);
    }

    @Override
    public UserResponseDto findById(Long id) {
        if (id == null) {
            throw new BadRequestException("El ID no puede ser null");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto save(UserCreateRequestDto request) {
        if (request == null) {
            throw new BadRequestException("Los datos del usuario no pueden ser null");
        }

        User user = userMapper.toEntity(request);
        user.setFechaRegistro(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        return userMapper.toResponseDto(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDto update(Long id, UserUpdateRequestDto request) {
        if (id == null) {
            throw new BadRequestException("El ID no puede ser null");
        }
        if (request == null) {
            throw new BadRequestException("Los datos de actualización no pueden ser null");
        }

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));

        userMapper.updateEntityFromDto(request, existingUser);
        User updatedUser = userRepository.save(existingUser);

        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (id == null) {
            throw new BadRequestException("El ID no puede ser null");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));

        userRepository.delete(user);
    }
}