package com.glenn.myblog.dto;

import com.glenn.myblog.domain.entity.User;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    public static final String NAME_LENGTH_ERROR = "이름은 2자 이상 10자 미만으로 작성해주세요.";
    public static final String NAME_FORMAT_ERROR = "이름에 숫자 및 특수 문자는 포함될 수 없습니다.";
    public static final String NAME_BLANK_ERROR = "이름을 작성해주세요.";
    public static final String PASSWORD_FORMAT_ERROR = "비밀번호는 8자 이상의 소문자, 대문자, 숫자, 특수문자의 조합이어야 합니다.";
    public static final String PASSWORD_BLANK_ERROR = "패스워드를 작성해주세요.";
    public static final String EMAIL_FORMAT_ERROR = "이메일의 양식이 틀렸습니다.";
    public static final String EMAIL_BLANK_ERROR = "이메일을 작성해주세요.";
    private static final String NAME_REGULAR_EXPRESSION = "[a-zA-Z가-힣]{2,10}";
    private static final String PASSWORD_REGULAR_EXPRESSION = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}";

    @NotBlank(message = NAME_BLANK_ERROR)
    @Size(min = 2, max = 8, message = NAME_LENGTH_ERROR)
    @Pattern(regexp = NAME_REGULAR_EXPRESSION, message = NAME_FORMAT_ERROR)
    private String name;
    @NotBlank(message = PASSWORD_BLANK_ERROR)
    @Pattern(regexp = PASSWORD_REGULAR_EXPRESSION, message = PASSWORD_FORMAT_ERROR)
    private String password;
    @NotBlank(message = EMAIL_BLANK_ERROR)
    @Email(message = EMAIL_FORMAT_ERROR)
    private String email;

    public static UserDto of(User user) {
        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User toEntity(String encodedPassword) {
        return User.builder()
                .name(name)
                .email(email)
                .password(encodedPassword)
                .build();
    }
}
