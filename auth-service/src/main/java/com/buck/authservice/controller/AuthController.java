package com.buck.authservice.controller;

import com.buck.authservice.dto.*;
import com.buck.authservice.model.User;
import com.buck.authservice.principal.UserPrincipal;
import com.buck.authservice.repository.UserRepository;
import com.buck.authservice.service.RefreshTokenService;
import com.buck.authservice.service.UserService;
import com.buck.authservice.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.buck.authservice.service.TokenBlacklistService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @PostMapping("/register")
    public UserResponseDTO  register(@RequestBody UserRequestDTO requestDTO) {
        System.out.println("DD");

        User user = new User();
        user.setEmail(requestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        user.setRole("ROLE_USER");

        User savedUser = userRepository.save(user);

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(savedUser.getId());
        responseDTO.setEmail(savedUser.getEmail());
        responseDTO.setRole(savedUser.getRole());
        return responseDTO;
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        String email = authentication.getName(); // Đây là email nếu bạn set đúng trong JwtFilter
        System.out.println("my email " + email);
        System.out.println("my user " + userService.getUserByEmail(email));

        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        long expireSeconds = jwtUtils.getRemainingExpiration(token);
        tokenBlacklistService.blacklistToken(token, expireSeconds);

        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        // Kiểm tra token hợp lệ
        if (!jwtUtils.isValidToken(refreshToken) || !refreshTokenService.isValid(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        // Lấy username (email) từ token
        String email = jwtUtils.extractEmail(refreshToken);

        // Tải lại user để xác minh (và phòng trường hợp user bị xoá)
        UserPrincipal user = userService.loadUserByUsername(email);

        // Sinh access token mới
        String newAccessToken = jwtUtils.generateToken(email);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequestDTO request,
            @RequestHeader("Authorization") String authHeader
    ) {
        System.out.println("Đang đổi mật khẩu");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Thiếu token");
        }

        String token = authHeader.substring(7);
        if (!jwtUtils.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ");
        }

        String email = jwtUtils.extractEmail(token);

        try {
            userService.changePassword(email, request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok("Đổi mật khẩu thành công");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        try {
            String token = userService.generateResetToken(request.getEmail());
            return ResponseEntity.ok("Đã gửi mã đặt lại mật khẩu (check log)");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        try {
            userService.resetPassword(request.getResetToken(), request.getNewPassword());
            return ResponseEntity.ok("Đặt lại mật khẩu thành công");
        } catch (RuntimeException e) {
            System.out.println("lỗi");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
