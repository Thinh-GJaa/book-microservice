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

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {

    @NotBlank(message = "Password is required")
    @ValidPassword
    String password;

    @NotBlank(message = "Password is required")
    @ValidPassword
    String newPassword;

    @NotBlank(message = "Password is required")
    @ValidPassword
    String confirmNewPassword;

}
