package com.nttdata.dockerized.postgresql.service;

import com.nttdata.dockerized.postgresql.exception.CustomBadRequestException;
import com.nttdata.dockerized.postgresql.exception.NotFoundException;
import com.nttdata.dockerized.postgresql.mapper.UserMapper;
import com.nttdata.dockerized.postgresql.model.dto.*;
import com.nttdata.dockerized.postgresql.model.entity.User;
import com.nttdata.dockerized.postgresql.repository.UserRepository;
import com.nttdata.dockerized.postgresql.service.impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Carlos");
        user.setFechaRegistro(LocalDateTime.now());

        userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setName("Carlos");
    }

    @Test
    void listarUsuarios() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(userMapper.toResponseDtoList(anyList())).thenReturn(Arrays.asList(userResponseDto));

        List<UserResponseDto> result = userService.listAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Carlos", result.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void buscarUsuarioPorId() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userService.findById(1L);

        assertNotNull(result);
        assertEquals("Carlos", result.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void buscarUsuarioNoExiste() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.findById(1L));

        assertEquals("Usuario no encontrado con ID: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void guardarUsuario() {
        UserCreateRequestDto request = new UserCreateRequestDto();
        request.setName("Carlos");

        when(userMapper.toEntity(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userService.save(request);

        assertNotNull(result);
        assertEquals("Carlos", result.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void guardarUsuarioNull() {
        CustomBadRequestException exception = assertThrows(CustomBadRequestException.class,
                () -> userService.save(null));

        assertEquals(2001, exception.getErrorCode());
    }

    @Test
    void actualizarUsuario() {
        UserUpdateRequestDto request = new UserUpdateRequestDto();
        request.setName("Carlos Updated");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userService.update(1L, request);

        assertNotNull(result);
        assertEquals("Carlos", result.getName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void eliminarUsuario() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.deleteById(1L));
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void eliminarUsuarioNoExiste() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.deleteById(1L));

        assertEquals("Usuario no encontrado con ID: 1", exception.getMessage());
    }
}

