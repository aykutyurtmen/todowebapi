package com.daimontech.dsapi.utilities.error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ErrorMessagesTr {

    @Autowired
    BaseError baseError;

    public void setErrorMap() {
        baseError.errorMap.put(baseError.getexistSex(), "Fail -> Cinsiyet zaten kayitli!");
        baseError.errorMap.put(baseError.getSexUnsaved(), "Fail -> Cinsiyet kaydedilemedi!");
        baseError.errorMap.put(baseError.getSexSaved(), "Cinsiyet Ekllendi");
        baseError.errorMap.put(baseError.getUnknownError(), "bilinmeyen hata");
        baseError.errorMap.put(baseError.getCategoryCannotSaved(), "Fail -> Kategori kaydedilemedi.");
    }
}

