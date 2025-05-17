package org.backend.volunteeringbackend.Services;


import jakarta.transaction.Transactional;
import org.backend.volunteeringbackend.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    public UserService() {}
}
