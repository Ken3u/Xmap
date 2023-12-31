package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.PersonDetails;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Scope(value = "prototype")
@Transactional(readOnly = true)
public class PersonDetailsService implements UserDetailsService {

    private final UserRepo userRepo;
    private final Logger logger = LoggerFactory.getLogger(PersonDetailsService.class);

    @Autowired
    public PersonDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.atInfo().log("loadUserByUsername email={}", email);
        Optional<User> user = userRepo.findByEmail(email);

        if (user.isEmpty()) {
            logger.atInfo().log("loadUserByUsername user not found email={}", email);
            throw new UsernameNotFoundException("User not found");
        }
        logger.atInfo().log("loadUserByUsername returned success " +
                        "user: id={} name={} email={} password={} birthday={} regDate={} phone={}",
                user.get().getId(),
                user.get().getName(),
                user.get().getEmail(),
                user.get().getPassword(),
                user.get().getBirthday(),
                user.get().getRegDate(),
                user.get().getPhoneNumber()
                );
        return new PersonDetails(user.get());
    }
}
