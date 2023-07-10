package com.warleydev.warleycatalog.dto;

import com.warleydev.warleycatalog.services.validation.UserInsertValid;
import jakarta.validation.constraints.NotBlank;

@UserInsertValid
public class UserInsertDTO extends UserDTO{

    @NotBlank(message = "Este campo não pode estar vazio.")
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
