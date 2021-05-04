package com.daimontech.dsapi.security.services;


import com.daimontech.dsapi.model.ActiveUser;
import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.repository.ActiveUserRepository;
import com.daimontech.dsapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActiveUserRepository activeUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
    	
        User user = userRepository.findByUsername(username)
                	.orElseThrow(() -> 
                        new UsernameNotFoundException("User Not Found with -> username or email : " + username)
        );

        return UserPrinciple.build(user);
    }

    @Transactional
    public boolean controlIfTokenExistsinDb(String token,
                                         String username){
        System.out.println("token is: " + token);
        System.out.println("username is: " + username);

        if(!activeUserRepository.existsByUsername(username)){
            ActiveUser activeUser = new ActiveUser();
            activeUser.setUsername(username);
            activeUser.setToken(token);
            activeUserRepository.save(activeUser);
            return false;
        } else{
            activeUserRepository.deleteActiveUserByUsername(username);
            return true;
        }
    }

    @Transactional
    public void saveActiveUser(ActiveUser activeUser){
        activeUserRepository.save(activeUser);
    }

    @Transactional
    public void deleteByUsername(String username){
        activeUserRepository.deleteActiveUserByUsername(username);
    }
    @Transactional
    public void deleteByToken(String token){
        activeUserRepository.deleteActiveUserByToken(token);
    }

}