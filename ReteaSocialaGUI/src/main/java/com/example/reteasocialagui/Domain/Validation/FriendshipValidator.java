package com.example.reteasocialagui.Domain.Validation;
import com.example.reteasocialagui.Domain.Friendship;
import com.example.reteasocialagui.Domain.Validation.Validator;
import com.example.reteasocialagui.Domain.User;

public class FriendshipValidator implements Validator<Friendship> {

    @Override
    public void validate(Friendship entity) throws ValidationException {
        String msg = "";
        if(entity.getUser1() == entity.getUser2()) {
            msg += "Prietenia nu se poate stabili intre doi utilizatori identici! ";
        }
        if(!msg.equals(""))
            throw new ValidationException(msg);

    }
}