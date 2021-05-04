package com.daimontech.dsapi.controller;

import com.daimontech.dsapi.message.request.*;
import com.daimontech.dsapi.message.response.DiscountGetByUserResponse;
import com.daimontech.dsapi.message.response.JwtResponse;
import com.daimontech.dsapi.model.*;
import com.daimontech.dsapi.model.enums.Status;
import com.daimontech.dsapi.repository.RoleRepository;
import com.daimontech.dsapi.repository.UserRepository;
import com.daimontech.dsapi.security.jwt.JwtProvider;
import com.daimontech.dsapi.security.services.DiscountUserServiceImpl;
import com.daimontech.dsapi.security.services.UserDetailsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Api(value="Auth islemleri")
public class AuthRestAPIs {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    DiscountUserServiceImpl discountService;

    @PostMapping("/signin")
    @ApiOperation(value = "Signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        if(!userRepository.existsByUsername(loginRequest.getUsername())){
            return new ResponseEntity<String>("Fail -> User could not be found.",
                    HttpStatus.BAD_REQUEST);
        }

        /*if(!(userRepository.findStatusByUsername(loginRequest.getUsername()) == Status.ACTIVE)){
            System.out.println("test");
            return new ResponseEntity<String>("Fail -> User is not ACTIVE!",
                    HttpStatus.BAD_REQUEST);
        }*/

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        boolean ifDelete = userDetailsService.controlIfTokenExistsinDb(jwt, loginRequest.getUsername());
        Optional<User> user = Optional.empty();
        if(ifDelete){
            ActiveUser activeUser = new ActiveUser();
            activeUser.setUsername(loginRequest.getUsername());
            activeUser.setToken(jwt);
            userDetailsService.saveActiveUser(activeUser);
            user = userRepository.findByUsername(loginRequest.getUsername());
        } else {
            user = userRepository.findByUsername(loginRequest.getUsername());
        }
        System.out.println("saving Token into DB");
        return ResponseEntity.ok(new JwtResponse(jwt, user.get()));
    }

    @PostMapping("/signup")
    @ApiOperation(value = "Signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<String>("Fail -> Phone Number already exists!",
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<String>("Fail -> Email already exists!",
                    HttpStatus.BAD_REQUEST);
        }


        // Creating user's account
        signUpRequest.setPassword(encoder.encode(signUpRequest.getPassword()));

        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(signUpRequest, User.class);

        Set<Role> roles = new HashSet<>();


        System.out.println(user.getUsername());
        User userInfo = userRepository.save(user);

        return ResponseEntity.ok().body(userInfo);
    }

    @DeleteMapping("/signout")
    @ApiOperation(value = "SignOut")
    public ResponseEntity<String> signOut(@Valid @RequestParam String username){
        userDetailsService.deleteByUsername(username);
        return ResponseEntity.ok().body("User signed out successfully!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/newdiscount")
    @ApiOperation(value = "New Discount")
    public ResponseEntity<String> newDiscount(@Valid @RequestBody DiscountUserAddRequest discountUserAddRequest) {
        if (userRepository.existsByUsername(discountUserAddRequest.getUsername())) {
            DiscountUser discountUser = new DiscountUser();
            Optional<User> user = userRepository.findByUsername(discountUserAddRequest.getUsername());
            discountUser.setDiscount(discountUserAddRequest.getDiscount());
            discountUser.setUser(user.get());
            if (discountService.addNewDiscount(discountUser))
                return ResponseEntity.ok().body("Discount created for user successfully!");
        }
        return ResponseEntity.badRequest().body("Fail -> Discount could not be created for user!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deletediscount")
    @ApiOperation(value = "Delete Discount")
    public ResponseEntity<String> deleteDiscount(@Valid @RequestBody DiscountUserDeleteRequest discountUserDeleteRequest) {
        if (discountService.existsById(discountUserDeleteRequest.getDiscountId())) {
            DiscountUser discountUser = discountService.findById(discountUserDeleteRequest.getDiscountId());
            if (discountService.delete(discountUser))
                return ResponseEntity.ok().body("Discount deleted successfully!");
        }
        return new ResponseEntity<String>("Fail -> Discount could not be deleted!",
                HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deletealldiscount")
    @ApiOperation(value = "Delete All Discount")
    public ResponseEntity<String> deleteAllDiscount(@Valid @RequestBody DiscountUserDeleteAllRequest discountUserDeleteAllRequest) {
        try {
            Optional<User> user = userRepository.findByUsername(discountUserDeleteAllRequest.getUsername());
            List<DiscountUser> discountUserList = discountService.getAllByUser(user.get());
            for (DiscountUser discountUser :
                    discountUserList) {
                discountService.delete(discountUser);
            }
            return ResponseEntity.ok().body("All Discounts deleted successfully!");
        } catch (Exception e) {
            return new ResponseEntity<String>("Fail -> All Discounts could not be deleted!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @GetMapping("/getalldiscounts/{username}")
    @ApiOperation(value = "All Discounts")
    public ResponseEntity<List<DiscountGetByUserResponse>> getAllDiscountsByUserName(@Valid @PathVariable(value = "username") String username) {
        Optional<User> user = userRepository.findByUsername(username);
        List<DiscountUser> discountUsers = discountService.getAllByUser(user.get());

        List<DiscountGetByUserResponse> discountGetByUserResponses = new ArrayList<>();
        for (DiscountUser discountUser:
                discountUsers) {
            DiscountGetByUserResponse discountGetByUserResponse = new DiscountGetByUserResponse();
            discountGetByUserResponse.setId(discountUser.getId());
            discountGetByUserResponse.setDiscount(discountUser.getDiscount());
            discountGetByUserResponse.setUsername(discountUser.getUser().getUsername());
            discountGetByUserResponses.add(discountGetByUserResponse);
        }
        return ResponseEntity.ok().body(discountGetByUserResponses);

    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @GetMapping("/getalldiscounts")
    @ApiOperation(value = "All Colors")
    public ResponseEntity<List<DiscountGetByUserResponse>> getAllDiscounts() {
        List<DiscountUser> discountUsers = discountService.getAllDiscounts();
        List<DiscountGetByUserResponse> discountGetByUserResponses = new ArrayList<>();
        for (DiscountUser discountUser:
                discountUsers) {
            DiscountGetByUserResponse discountGetByUserResponse = new DiscountGetByUserResponse();
            discountGetByUserResponse.setDiscount(discountUser.getDiscount());
            discountGetByUserResponse.setUsername(discountUser.getUser().getUsername());
            discountGetByUserResponses.add(discountGetByUserResponse);
        }
        return ResponseEntity.ok().body(discountGetByUserResponses);

    }
}