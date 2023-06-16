package com.warleydev.warleycatalog.dto;

import com.warleydev.warleycatalog.services.validation.UserInsertValid;

@UserInsertValid
public class UserInsertDTO extends UserDTO{

    private String password;

    public UserInsertDTO(){
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
