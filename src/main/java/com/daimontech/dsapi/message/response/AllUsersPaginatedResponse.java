package com.daimontech.dsapi.message.response;

import com.daimontech.dsapi.model.Role;
import com.daimontech.dsapi.model.enums.SaleType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class AllUsersPaginatedResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    @Pattern(regexp ="^\\+(?:[0-9] ?){6,14}[0-9]$")
    private String username;

    @Size(max = 60)
    @Email
    private String email;

    @Size(min = 3, max = 50)
    private String country;

    @Size(min = 3, max = 50)
    private String city;

    @Size(min = 3, max = 50)
    private String companyName;

    @Enumerated(EnumType.STRING)
    private SaleType saleType;

    @Size(min = 3, max = 50)
    private String photoOne;

    @Size(min = 3, max = 50)
    private String photoTwo;

    private Set<Role> roles;

}
