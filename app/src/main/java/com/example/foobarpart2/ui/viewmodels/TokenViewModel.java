package com.example.foobarpart2.ui.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.foobarpart2.repository.TokenRepository;

public class TokenViewModel extends ViewModel {
    private TokenRepository tokenRepository;

    public TokenViewModel() {
        this.tokenRepository = new TokenRepository();
    }

    public String getToken() {
        return tokenRepository.get();
    }
}
