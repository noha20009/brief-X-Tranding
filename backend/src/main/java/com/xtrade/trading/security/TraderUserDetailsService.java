package com.xtrade.trading.security;

import com.xtrade.trading.model.Trader;
import com.xtrade.trading.repository.TraderRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Charge un utilisateur à partir de son email (login du JWT).
 */
@Service
public class TraderUserDetailsService implements UserDetailsService {

    private final TraderRepository traderRepository;

    public TraderUserDetailsService(TraderRepository traderRepository) {
        this.traderRepository = traderRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String normalized = normalize(email);
        Trader trader = traderRepository.findByEmail(normalized)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + email));
        return new User(
                trader.getEmail(),
                trader.getPassword(),
                List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(trader.getRole()))
        );
    }

    public Trader findByEmail(String email) {
        return traderRepository.findByEmail(normalize(email))
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + email));
    }

    private String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}