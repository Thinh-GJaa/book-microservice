package com.book.identityservice.dto.request;

import com.book.identityservice.validator.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request for user login")
public class LoginRequest {

    @Schema(description = "Username", example = "user@gmail.com")
    @NotBlank(message = "Username is required")
    String username;

    @Schema(description = "Password", example = "Abc@123456")
    @ValidPassword
    @NotBlank(message = "Password is required")
    String password;
}
