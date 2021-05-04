package com.daimontech.dsapi.repository;

import com.daimontech.dsapi.model.DiscountUser;
import com.daimontech.dsapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountUserRepository extends JpaRepository<DiscountUser, Long> {

    boolean existsById(Long discountId);
    Optional<DiscountUser> findById(Long discountId);
    List<DiscountUser> getAllByUser(User user);
    List<DiscountUser> findAllByDiscountIsNotNull();
}
