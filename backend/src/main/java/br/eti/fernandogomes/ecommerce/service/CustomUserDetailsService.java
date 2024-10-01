package br.eti.fernandogomes.ecommerce.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equals(username)) {
            // Cria um usuário em memória com nome de usuário "admin" e senha "admin"
            return User.withDefaultPasswordEncoder()
                    .username("admin")
                    .password("admin")
                    .roles("ADMIN")
                    .build();
        } else {
            // Lança uma exceção se o usuário não for encontrado
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
    }
}
