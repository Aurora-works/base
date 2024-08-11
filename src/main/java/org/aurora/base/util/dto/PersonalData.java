package org.aurora.base.util.dto;

public record PersonalData(
        Long id,
        String username,
        String nickname,
        String sex,
        String email,
        String mobilePhoneNumber,
        String description,
        String oldPassword,
        String newPassword) {
}
