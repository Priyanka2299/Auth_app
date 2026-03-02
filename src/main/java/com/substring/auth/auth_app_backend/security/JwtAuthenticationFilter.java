package com.substring.auth.auth_app_backend.security;

import com.substring.auth.auth_app_backend.helper.UserHelper;
import com.substring.auth.auth_app_backend.models.User;
import com.substring.auth.auth_app_backend.repositories.UserRepository;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer")){          //Authorization: Bearer 2232837 --> if header is not null and it starts with Bearer then everything is ok
            //if Bearer is there then do token extract and validate then create authentication and set under security context
            String token = header.substring(7);
            //we will parse token Parse Means: analyze and extract meaningful parts from a string --> error might come so we will use try & catch

            try{
                Jws<Claims> parse = jwtService.parse(token);
                Claims payload = parse.getPayload();
                String userId = payload.getSubject();
                UUID userUuid = UserHelper.parseUUID(userId);

                userRepository.findById(userUuid)
                        .ifPresent(user -> {
                            //if user is found in the db
                            List<GrantedAuthority> authorities = user.getRoles() == null ? List.of(): user.getRoles().stream()  //If roles is null then we will provide empty list else we will take one by role object and convert that into SimpleGrantedAuthority and under SimpleGrantedAuthorty - > we will add role name (ed. if role_admin then role.getName() -> role_admin wil come)
                                        .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                                        .collect(Collectors.toList());
                            //Now we'll make authentication. To make that we will use authentication interface
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    user.getEmail(),
                                    null,
                                    authorities
                            );
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            //this is final line to set the authentication to security context
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        });

            }catch(ExpiredJwtException e){
                e.printStackTrace();
            } catch(MalformedJwtException e){

            } catch(JwtException e){

            } catch(Exception e){

            }



        }
        filterChain.doFilter(request, response); // else if the Bearer is not there, then we will sent request and response to security context without settings
    }
}
