package com.book.identityservice.dto.request;

import com.book.identityservice.validator.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request for resetting password")
public class ResetPasswordRequest {

    @Schema(description = "User email", example = "user@gmail.com")
    @Email
    @NotBlank(message = "Email is required")
    String email;

    @Schema(description = "Reset token", example = "5f6f2a1b-d9a4-4d4c-aeb7-f09366e91f75")
    @NotBlank(message = "Token is required")
    String token;

    @Schema(description = "New password", example = "NewPass@123")
    @NotBlank(message = "New password is required")
    @ValidPassword
    String newPassword;

    @Schema(description = "Confirm new password", example = "NewPass@123")
    @NotBlank(message = "Confirm password is required")
    @ValidPassword
    String confirmPassword;

}
