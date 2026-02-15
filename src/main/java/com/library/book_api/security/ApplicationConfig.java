package com.library.book_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider; // Ajouté
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Ajouté
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService; // Ajouté
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor; // Ajouté pour l'injection

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserDetailsService userDetailsService; // Injecté pour l'AuthenticationProvider

    // 1. Le "Hacheur" de mot de passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Le "Chef de la Sécurité" (Correction ici)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // On utilise le paramètre 'config' injecté par Spring, pas un 'new' !
        return config.getAuthenticationManager();
    }

    // 3. Le "Fournisseur d'Authentification" (C'est lui qui fait le lien BDD <-> Login)
    // C'est une pièce manquante importante pour que ça marche !
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Il a besoin de ton service pour trouver l'user
        authProvider.setPasswordEncoder(passwordEncoder()); // Il a besoin de ton encodeur pour vérifier le mdp
        return authProvider;
    }
}