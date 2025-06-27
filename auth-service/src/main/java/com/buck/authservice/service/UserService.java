package com.buck.authservice.service;

import com.buck.authservice.config.SecurityConfig;
import com.buck.authservice.dto.LoginRequestDTO;
import com.buck.authservice.dto.LoginResponseDTO;
import com.buck.authservice.dto.UserRequestDTO;
import com.buck.authservice.dto.UserResponseDTO;
import com.buck.authservice.model.User;
import com.buck.authservice.principal.UserPrincipal;
import com.buck.authservice.repository.UserRepository;
import com.buck.authservice.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.buck.authservice.service.RefreshTokenService;

import java.time.Duration;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    public UserPrincipal loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));
        return new UserPrincipal(user);
    }

    public UserResponseDTO register(UserRequestDTO requestDTO) {
        // Kiểm tra email đã tồn tại
        if(userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại");
        }

        User user = new User();
        user.setEmail(requestDTO.getEmail());
        user.setPassword(requestDTO.getPassword());
        user.setRole("ROLE_USER");

        User savedUser = userRepository.save(user);

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(savedUser.getId());
        responseDTO.setEmail(savedUser.getEmail());
        responseDTO.setRole(savedUser.getRole());
        return responseDTO;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));
        System.out.println("Password input: " + request.getPassword());
        System.out.println("Password in DB (encoded): " + user.getPassword());
        System.out.println("Match result: " + passwordEncoder.matches(request.getPassword(), user.getPassword()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Sai mật khẩu");
        }

        String token = jwtUtils.generateToken(user.getEmail());
        String refreshToken = jwtUtils.generateRefreshToken(new UserPrincipal(user));
        refreshTokenService.store(
                refreshToken,
                user.getEmail(),
                7 * 24 * 60 * 60 * 1000 // 7 ngày
        );
        LoginResponseDTO response = new LoginResponseDTO();
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        return response;
    }

    public String getEmailFromToken(String token) {
        return jwtUtils.extractEmail(token);
    }
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }


    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }
        // Encode và lưu mật khẩu mới
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


    public String generateResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        String token = UUID.randomUUID().toString(); // random token

        redisTemplate.opsForValue().set("reset_token:" + token, email, Duration.ofMinutes(15));

        System.out.println("🔐 Reset token for " + email + ": " + token);

        return token;
    }


    public void resetPassword(String token, String newPassword) {
        String redisKey = "reset_token:" + token;
        String email = redisTemplate.opsForValue().get(redisKey);

        if (email == null) {
            throw new RuntimeException("Token không hợp lệ hoặc đã hết hạn");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        redisTemplate.delete(redisKey);
    }
}
