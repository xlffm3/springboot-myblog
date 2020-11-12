package com.glenn.myblog.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    private Long id;
    private String name;
    private String email;
    private String password;
}
