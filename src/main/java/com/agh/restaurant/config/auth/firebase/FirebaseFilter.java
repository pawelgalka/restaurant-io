package com.agh.restaurant.config.auth.firebase;

import com.agh.restaurant.service.FirebaseService;
import com.agh.restaurant.service.exception.FirebaseTokenInvalidException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang.StringUtils.isBlank;

public class FirebaseFilter extends OncePerRequestFilter {

    private FirebaseService firebaseService;

    public FirebaseFilter(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String HEADER_NAME = "X-Authorization-Firebase";
        String xAuth = request.getHeader(HEADER_NAME);
        if (isBlank(xAuth)) {
            filterChain.doFilter(request, response);
        } else {
            try {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(xAuth);


                String userName = decodedToken.getUid();

                Authentication auth = new FirebaseAuthenticationToken(userName, decodedToken);

                SecurityContextHolder.getContext().setAuthentication(auth);
                request.setAttribute("username",userName);
                filterChain.doFilter(request, response);
            } catch (FirebaseTokenInvalidException | FirebaseAuthException e) {
                throw new SecurityException(e);
            }
        }
    }

}
