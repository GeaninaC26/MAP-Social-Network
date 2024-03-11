package com.example.reteasocialagui.Domain.Validation;

import com.example.reteasocialagui.Domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {

        String msg = "";
        String nume = entity.getNume();
        String oras = entity.getOras();
        boolean resultNume = nume.matches("[a-zA-Z ]+");
        boolean resultOras = oras.matches("[a-zA-Z ]+");
        if(nume.equals("") || nume.equals(" "))
            msg += "Numele nu poate fi nul! ";
        if(!resultNume)
            msg += "Numele poate contine doar litere! ";
        if(!resultOras)
            msg += "Orasul poate contine doar litere! ";
        if(oras.equals("") || oras.equals(" "))
            msg += "Orasul nu poate fi nul! ";
        if(entity.getVarsta() < 0)
            msg += "Varsta nu poate fi negativa! ";
        if(!msg.equals(""))
            throw new ValidationException(msg);

    }
}
