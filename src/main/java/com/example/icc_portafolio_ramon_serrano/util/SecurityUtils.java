package com.example.icc_portafolio_ramon_serrano.util;

import com.example.icc_portafolio_ramon_serrano.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static UserPrincipal getCurrentPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return null;
        }
        if (auth.getPrincipal() instanceof UserPrincipal principal) {
            return principal;
        }
        if (auth.getPrincipal() instanceof UserDetails userDetails) {
            return null;
        }
        return null;
    }
}
