package com.example.common;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//Maps the “roles” claim from the JWT token to a list of Granted Authorities in the endpoints service
public class JwtRoleConverter implements Converter<Jwt, Collection> {
    @Override
    public Collection convert(Jwt jwt) {
        @SuppressWarnings("unchecked")
        List<String> roles = (ArrayList) jwt.getClaims().get("roles");
        if (roles == null || roles.isEmpty()) {
            return new ArrayList<>();
        }
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
