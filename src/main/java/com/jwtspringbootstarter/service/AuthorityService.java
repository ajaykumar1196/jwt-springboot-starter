package com.jwtspringbootstarter.service;

import com.jwtspringbootstarter.model.Authority;
import com.jwtspringbootstarter.repository.AuthorityRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {
    private final AuthorityRepository authorityRepository;


    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public Authority findByAuthority(String authority){
        return authorityRepository.findByAuthority(authority);
    }

    public Authority findById(Long id){
        return authorityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Authority not found with id: " + id));
    }
}
