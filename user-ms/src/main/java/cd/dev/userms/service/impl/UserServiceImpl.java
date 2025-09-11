package cd.dev.userms.service.impl;

import cd.dev.userms.exception.CustomBadRequestException;
import cd.dev.userms.exception.NotFoundException;
import cd.dev.userms.mapper.UserMapper;
import cd.dev.userms.model.dto.UserCreateRequestDto;
import cd.dev.userms.model.dto.UserResponseDto;
import cd.dev.userms.model.dto.UserUpdateRequestDto;
import cd.dev.userms.model.entity.User;
import cd.dev.userms.repository.UserRepository;
import cd.dev.userms.service.UserService;
import org.springframework.data.crossstore.ChangeSetPersister;
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
            throw new CustomBadRequestException("El ID no puede ser null", 2000);
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException("El ID no puede ser null", 2000));
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto save(UserCreateRequestDto request) {
        if (request == null) {
            throw new CustomBadRequestException("Los datos del usuario no pueden ser null", 2001);
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
            throw new CustomBadRequestException("El ID no puede ser null", 2000);
        }
        if (request == null) {
            throw new CustomBadRequestException("Los datos de actualización no pueden ser null", 2002);
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
            throw new CustomBadRequestException("El ID no puede ser null", 2000);
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));

        userRepository.delete(user);
    }
}