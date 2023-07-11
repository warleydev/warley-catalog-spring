package com.warleydev.warleycatalog.dto;

import com.warleydev.warleycatalog.services.validation.UserInsertValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@UserInsertValid
public class UserInsertDTO extends UserDTO{

    @NotBlank(message = "Este campo não pode estar vazio.")
    @Size(min = 8, message = "A senha deve possuir no mínimo 8 caracteres")
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
