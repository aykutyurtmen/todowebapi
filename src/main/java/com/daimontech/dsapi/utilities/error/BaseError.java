package com.daimontech.dsapi.utilities.error;

import com.daimontech.dsapi.utilities.constant.ErrorConstant;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BaseError {

    //getter setter defines
    public String getExistUser() {
        return ErrorConstant.existUser;
    }

    public String getexistSex() {
        return ErrorConstant.existSex;
    }

    public String getSexUnsaved() {
        return ErrorConstant.SexUnsaved;
    }

    public String getSexSaved() {
        return ErrorConstant.SexSaved;
    }

    public String getCategoryCannotSaved() {
        return ErrorConstant.CategoryCannotSaved;
    }

    public String getUnknownError() {
        return ErrorConstant.unknownError;
    }

    //set map by error messages_en
    public Map<String, String> errorMap = new HashMap<String, String>();
}
