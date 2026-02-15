package com.library.book_api.security; // Ou .config selon ton choix

import com.library.book_api.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    // 1. CORRECTION : On injecte l'interface UserDetailsService, pas la classe concrète (bonnes pratiques)
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Vérification rapide : Si pas de token, on laisse passer (Spring Security bloquera plus loin si nécessaire)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraction du Token
        jwt = authHeader.substring(7); // Plus propre que replace()
        userEmail = jwtService.extractUsername(jwt);

        // 4. Validation du contexte
        // On vérifie si l'email existe ET si l'utilisateur n'est pas déjà authentifié
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 5. CORRECTION : On charge l'utilisateur via l'instance injectée
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 6. Si le token est valide...
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // On crée le "Badge" d'accès (UsernamePasswordAuthenticationToken)
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // On ajoute des détails techniques (ex: adresse IP de l'utilisateur)
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 7. CORRECTION CRUCIALE : On donne le badge au vigile (SecurityContext)
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 8. CORRECTION : On passe TOUJOURS la main à la suite, sinon la requête s'arrête ici !
        filterChain.doFilter(request, response);
    }
}