package com.bside405.perfume.project.user;

import lombok.Setter;

@Setter
public class UserDTO {
    private String name;
    private String email;

    public UserDTO(String name, String email) {
            this.name = name;
            this.email = email;
    }
}
