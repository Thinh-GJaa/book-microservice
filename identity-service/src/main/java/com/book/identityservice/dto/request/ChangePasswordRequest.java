package com.book.identityservice.dto.request;

import com.book.identityservice.enums.Gender;
import com.book.identityservice.validator.ValidDob;
import com.book.identityservice.validator.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request for changing password")
public class ChangePasswordRequest {

    @Schema(description = "Current password", example = "OldPass@123")
    @NotBlank(message = "Password is required")
    @ValidPassword
    String password;

    @Schema(description = "New password", example = "NewPass@123")
    @NotBlank(message = "Password is required")
    @ValidPassword
    String newPassword;

    @Schema(description = "Confirm new password", example = "NewPass@123")
    @NotBlank(message = "Password is required")
    @ValidPassword
    String confirmNewPassword;

}
