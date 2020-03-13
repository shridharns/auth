package com.shridhar.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {

    private LoginType loginType;

    private LoginData loginData;

    private String clientId;

}
