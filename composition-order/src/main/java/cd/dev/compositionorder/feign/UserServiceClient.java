package cd.dev.compositionorder.feign;

import cd.dev.compositionorder.dto.external.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-ms", url = "${microservices.user-ms.url}")
public interface UserServiceClient {

    @GetMapping("/api/users/{id}")
    UserResponseDto getUserById(@PathVariable Long id);

    @GetMapping("/api/users")
    List<UserResponseDto> getAllUsers();
}
