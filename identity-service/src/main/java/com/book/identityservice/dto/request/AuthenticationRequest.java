package com.book.identityservice.dto.request;

import com.book.identityservice.validator.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {

    @NotBlank(message = "Username is required")
    String username;

    @ValidPassword
    String password;
}
