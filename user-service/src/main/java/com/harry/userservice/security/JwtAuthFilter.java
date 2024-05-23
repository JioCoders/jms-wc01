// package com.harry.userservice.security;

// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import com.harry.userservice.controller.UserResponse;
// import com.harry.userservice.entity.UserX;
// import com.harry.userservice.service.api.UserService;

// import io.jsonwebtoken.io.IOException;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.RequiredArgsConstructor;

// @Component
// @RequiredArgsConstructor
// public class JwtAuthFilter extends OncePerRequestFilter {

//     private final JwtService jwtService;
//     private final UserService userService;

//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//             throws ServletException, IOException, java.io.IOException {
//         String authHeader = request.getHeader("Authorization");
//         if (authHeader != null && authHeader.startsWith("Bearer ")) {
//             String token = authHeader.substring(7);
//             String userIdString = jwtService.extractUserId(token);
//             if (userIdString != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                 int userId = Integer.parseInt(jwtService.extractUserId(token));
//                 UserResponse res = userService.findByUserId(userId);
//                 UserX user = res.getUserX();
//                 if (jwtService.validateTokenId(token, user)) {
//                     UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                             user, null, user.getAuthorities());
//                     authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                     SecurityContextHolder.getContext().setAuthentication(authToken);
//                 }
//             }
//         }
//         filterChain.doFilter(request, response);
//     }
// }