package com.newzy.backend.domain.user.service;

public interface AuthService {
    String handleLoginOrSignup(String code);

    String generateLoginUrl();
}
