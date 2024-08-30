package org.aurora.base.common.dto;

import jakarta.validation.constraints.Email;
import org.aurora.base.common.validation.Mobile;

public record PersonalData(
        Long id,
        String username,
        String nickname,
        String sex,
        @Email
        String email,
        @Mobile
        String mobilePhoneNumber,
        String description,
        String oldPassword,
        String newPassword) {
}
