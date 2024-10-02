package com.newzy.backend.domain.user.service;

public interface AuthService {
    final int EXPIRATION_TIME = 3600;
    String handleLoginOrSignup(String code);

    String generateLoginUrl();
}
