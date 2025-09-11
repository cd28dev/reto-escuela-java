package cd.dev.userms.controller;

import cd.dev.userms.model.dto.UserCreateRequestDto;
import cd.dev.userms.model.dto.UserResponseDto;
import cd.dev.userms.model.dto.UserUpdateRequestDto;
import cd.dev.userms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.listAll();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    public UserResponseDto save(@RequestBody UserCreateRequestDto userCreateRequestDto) {
        return userService.save(userCreateRequestDto);
    }

    @PutMapping("/{id}")
    public UserResponseDto update(@PathVariable Long id, @RequestBody UserUpdateRequestDto request) {
        return userService.update(id,request);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
