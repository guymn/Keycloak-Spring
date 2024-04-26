package com.qrebl.users.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
