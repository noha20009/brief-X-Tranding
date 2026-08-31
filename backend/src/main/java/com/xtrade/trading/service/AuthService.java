package com.xtrade.trading.service;

import com.xtrade.trading.dto.AuthResponse;
import com.xtrade.trading.dto.LoginRequest;
import com.xtrade.trading.dto.RegisterRequest;
import com.xtrade.trading.exception.TradingAuthenticationException;
import com.xtrade.trading.exception.TradingException;
import com.xtrade.trading.model.Trader;
import com.xtrade.trading.repository.TraderRepository;
import com.xtrade.trading.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Gère l'inscription et la connexion des utilisateurs.
 */
@Service
public class AuthService {

    private final TraderRepository traderRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthService(TraderRepository traderRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils,
                       AuthenticationManager authenticationManager) {
        this.traderRepository = traderRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = normalize(request.email());
        if (traderRepository.existsByEmail(email)) {
            throw new TradingException("Un compte existe déjà avec cet email : " + request.email());
        }
        Trader trader = new Trader(
                request.nom().trim(),
                request.balance(),
                email,
                passwordEncoder.encode(request.password()),
                "ROLE_USER"
        );
        trader = traderRepository.save(trader);
        String token = jwtUtils.generateToken(trader.getEmail());
        return AuthResponse.from(trader, token);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = normalize(request.email());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.password()));
        } catch (BadCredentialsException | DisabledException ex) {
            throw new TradingAuthenticationException("Email ou mot de passe incorrect.");
        }
        Trader trader = traderRepository.findByEmail(email)
                .orElseThrow(() -> new TradingAuthenticationException("Email ou mot de passe incorrect."));
        String token = jwtUtils.generateToken(trader.getEmail());
        return AuthResponse.from(trader, token);
    }

    private String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}