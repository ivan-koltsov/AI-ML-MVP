package com.aiml.fintech.auth;

import com.aiml.fintech.auth.dto.ErrorResponse;
import com.aiml.fintech.auth.dto.SignInRequest;
import com.aiml.fintech.auth.dto.SignUpRequest;
import com.aiml.fintech.auth.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserResponse> signIn(@Valid @RequestBody SignInRequest request, HttpServletRequest httpRequest) {
        UserResponse response = authService.signIn(request);
        User user = authService.findUserById(response.id());
        UserPrincipal principal = new UserPrincipal(user);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        httpRequest.getSession(true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody SignUpRequest request, HttpServletRequest httpRequest) {
        UserResponse response = authService.signUp(request);
        User user = authService.findUserById(response.id());
        UserPrincipal principal = new UserPrincipal(user);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        httpRequest.getSession(true);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<Void> signOut(HttpServletRequest request) {
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        authService.signOut();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(authService.getCurrentUser(principal.getUser()));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(AuthConflictException.class)
    public ResponseEntity<ErrorResponse> handleAuthConflict(AuthConflictException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(AuthBadRequestException.class)
    public ResponseEntity<ErrorResponse> handleAuthBadRequest(AuthBadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> new ErrorResponse.FieldError(e.getField(), e.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest().body(ErrorResponse.of("Validation failed", errors));
    }
}
