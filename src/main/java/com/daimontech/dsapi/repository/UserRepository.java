package com.daimontech.dsapi.repository;

import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    @Query(value = "SELECT status FROM users u WHERE u.username = ?", nativeQuery = true)
    Status findStatusByUsername(String username);
    List<User> findAllByUsernameIsNotNull();
    Optional<User> findById(Long userID);

    @Query(value = "SELECT * FROM users WHERE LOWER(name) LIKE LOWER(concat('%', ?1,'%')) OR LOWER(city) LIKE LOWER(concat('%', ?1,'%')) OR LOWER(email) LIKE LOWER(concat('%', ?1,'%')) " +
            "OR username LIKE %?1% OR LOWER(company_name) LIKE LOWER(concat('%', ?1,'%')) OR LOWER(country) LIKE LOWER(concat('%', ?1,'%')) OR LOWER(sale_type) LIKE LOWER(concat('%', ?1,'%'))", nativeQuery = true)
    Page<User> findAll(String value, Pageable pageable);
}