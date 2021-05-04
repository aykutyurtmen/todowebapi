package com.daimontech.dsapi.security.services;

import com.daimontech.dsapi.model.DiscountUser;
import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.repository.DiscountUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountUserServiceImpl implements DiscountUserService {

    @Autowired
    DiscountUserRepository discountUserRepository;

    public DiscountUser findById(Long discountId){
        return discountUserRepository.findById(discountId).get();
    }

    public boolean existsById(Long discountId){
        return discountUserRepository.existsById(discountId);
    }

    public List<DiscountUser> getAllByUser(User user){
        return discountUserRepository.getAllByUser(user);
    }

    public List<DiscountUser> getAllDiscounts(){
        return discountUserRepository.findAllByDiscountIsNotNull();
    }

    public Boolean delete(DiscountUser discountUser) {
        try {
            discountUserRepository.delete(discountUser);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public Boolean addNewDiscount(DiscountUser discountUser){
        try{
            discountUserRepository.save(discountUser);
            return true;
        } catch(Exception e){
            return false;
        }
    }
}
