package in.om.billingsoftware.filter;

import in.om.billingsoftware.service.impl.AppUserDetailsService;
import in.om.billingsoftware.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final AppUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7).trim();

            if (!jwt.isEmpty() && jwt.split("\\.").length == 3) {
                try {
                    email = jwtUtil.extractUsername(jwt);
                    System.out.println("🔍 Extracted Email from JWT: " + email);
                } catch (Exception e) {
                    System.out.println("⚠️ Invalid JWT token: " + e.getMessage());
                }
            } else {
                System.out.println("⚠️ Malformed or empty JWT token: " + jwt);
            }
        } else {
            System.out.println("⚠️ Missing or malformed Authorization header: " + authorizationHeader);
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                System.out.println("👤 Loaded UserDetails: " + userDetails.getUsername() + " | Authorities: " + userDetails.getAuthorities());

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    System.out.println("✅ SecurityContext successfully populated with authentication!");
                } else {
                    System.out.println("❌ jwtUtil.validateToken returned FALSE for user: " + email);
                }
            } catch (Exception e) {
                System.out.println("❌ Error loading user or setting authentication: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
