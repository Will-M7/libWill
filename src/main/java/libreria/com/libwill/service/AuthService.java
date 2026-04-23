package libreria.com.libwill.service;


import libreria.com.libwill.dto.JwtResponseDTO;
import libreria.com.libwill.dto.LoginRequestDTO;
import libreria.com.libwill.dto.RegisterRequestDTO;

public interface AuthService {
    JwtResponseDTO authenticateUser(LoginRequestDTO loginRequest);
    String registerUser(RegisterRequestDTO registerRequest);
    boolean isUsernameAvailable(String username);
    boolean isEmailAvailable(String email);
}
