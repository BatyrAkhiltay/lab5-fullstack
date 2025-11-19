package xyz.akhiltay.lab4fullstack.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.akhiltay.lab4fullstack.domain.AppUser;
import xyz.akhiltay.lab4fullstack.repository.AppUserRepository;
import xyz.akhiltay.lab4fullstack.security.AccountCredentials;
import xyz.akhiltay.lab4fullstack.security.SignupRequest;
import xyz.akhiltay.lab4fullstack.service.JwtService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginController(JwtService jwtService,
                           AuthenticationManager authenticationManager,
                           AppUserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> getToken(@RequestBody AccountCredentials credentials) {
        UsernamePasswordAuthenticationToken creds =
                new UsernamePasswordAuthenticationToken(
                        credentials.username(),
                        credentials.password()
                );

        Authentication auth = authenticationManager.authenticate(creds);

        String jwts = jwtService.getToken(auth.getName());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwts)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        if (userRepository.findByUsername(signupRequest.username()).isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Username already exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        if (signupRequest.password() == null || signupRequest.password().length() < 4) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Password must be at least 4 characters long");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        String role = signupRequest.role() != null &&
                (signupRequest.role().equals("ADMIN") || signupRequest.role().equals("USER"))
                ? signupRequest.role()
                : "USER";

        AppUser newUser = new AppUser(
                signupRequest.username(),
                passwordEncoder.encode(signupRequest.password()),
                role
        );

        userRepository.save(newUser);

        String token = jwtService.getToken(newUser.getUsername());

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("username", newUser.getUsername());
        response.put("role", newUser.getRole());

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                .body(response);
    }
}