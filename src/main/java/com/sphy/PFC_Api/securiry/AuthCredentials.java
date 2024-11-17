package com.sphy.PFC_Api.securiry;

import lombok.Data;

@Data
public class AuthCredentials {
    private String username;
    private String email;
    private String password;
}
