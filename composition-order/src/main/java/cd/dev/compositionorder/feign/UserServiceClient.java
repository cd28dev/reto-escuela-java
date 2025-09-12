package cd.dev.compositionorder.feign;

import cd.dev.compositionorder.dto.external.UserResponseDto;
import cd.dev.compositionorder.model.dto.external.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${services.user-service.url}")
public interface UserServiceClient {

    @GetMapping("/api/users/{id}")
    UserResponseDto findById(@PathVariable("id") Long id);
}