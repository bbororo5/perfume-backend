package com.bside405.perfume.project.user;

import lombok.Setter;

@Setter
public class UserDto {
    private String name;
    private String email;

    public UserDto(String name, String email) {
            this.name = name;
            this.email = email;
    }
}
