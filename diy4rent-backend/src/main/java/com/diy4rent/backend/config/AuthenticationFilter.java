package com.diy4rent.backend.config;


import com.diy4rent.backend.SpringApplicationContext;
import com.diy4rent.backend.model.Usuario;
import com.diy4rent.backend.requests.UserLoginRequest;
import com.diy4rent.backend.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private CustomAuthenticationManager authenticationManager;

    private ObjectMapper objectMapper = new ObjectMapper();
    public AuthenticationFilter(CustomAuthenticationManager authenticationManager) {
        this.authenticationManager=authenticationManager;

    }




    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {

        try {
            req.getInputStream();
            UserLoginRequest creds = new ObjectMapper().readValue(req.getInputStream(), UserLoginRequest.class);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(creds.getCorreo(), creds.getPassword(), new ArrayList<>());

            return authenticationManager.authenticate(usernamePasswordAuthenticationToken
            );
        }

        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String email = ( auth.getName());
        UserService userService =(UserService) SpringApplicationContext.getBean("userService");

        Usuario user = userService.getUser(email);

        String token = Jwts.builder()
                .setSubject(email)
                .claim("id",user.getId())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.Token_Expiration_Time))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET )
                .compact();



        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.Token_Prefix + token);


        res.setStatus(HttpStatus.OK.value());
        Map<String, Object> data = new HashMap<>();
        Map<String, String> tokenResponse = new HashMap<>();
        tokenResponse.put("token", token);
        data.put(
                "code",
                200);
        data.put(
                "data",
                tokenResponse
        );

        data.put(
                "success",
                true);

        res.setContentType("application/json");

        // Write the JSON string to the response output stream
        res.getOutputStream().write(objectMapper.writeValueAsBytes(data));
    }


}
