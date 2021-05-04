package com.daimontech.dsapi.utilities.error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ErrorMessagesEn{

    @Autowired
    BaseError baseError;

    public void setErrorMap() {
        baseError.errorMap.put(baseError.getexistSex(), "Fail -> Sex Is Exist");
        baseError.errorMap.put(baseError.getSexUnsaved(), "Fail -> Sex Is Not Saved!");
        baseError.errorMap.put(baseError.getSexSaved(), "Sex Added");
        baseError.errorMap.put(baseError.getUnknownError(), "Unknown Error");
        baseError.errorMap.put(baseError.getCategoryCannotSaved(), "Fail -> Category cannot be saved.");
    }
}
