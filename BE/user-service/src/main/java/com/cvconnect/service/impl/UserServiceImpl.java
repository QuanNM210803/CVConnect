package com.cvconnect.service.impl;

import com.cvconnect.repository.UserRepository;
import com.cvconnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

}
