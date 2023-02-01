package com.otp.server.services;


import com.otp.server.entities.Otp;
import com.otp.server.entities.User;
import com.otp.server.repositories.OtpRepository;
import com.otp.server.repositories.UserRepository;
import com.otp.server.utils.GenerateCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void auth(User user) {
        userRepository.findUserByUsername(user.getUsername())
                .filter(u -> passwordEncoder.matches(user.getPassword(), u.getPassword()))
                .ifPresentOrElse(this::renewOtp, () -> {throw new BadCredentialsException("Bad credentials.");});
    }

    public boolean check(Otp otpToValidate) {
        return otpRepository.findOtpByUsername(otpToValidate.getUsername())
                .filter(otp -> otpToValidate.getCode().equals(otp.getCode()))
                .isPresent();
    }

    private void renewOtp(User u) {
        String code = GenerateCodeUtil.generateCode();

        Optional<Otp> userOtp = otpRepository.findOtpByUsername(u.getUsername());
        if (userOtp.isPresent()) {
            Otp otp = userOtp.get();
            otp.setCode(code);
        } else {
            Otp otp = new Otp();
            otp.setUsername(u.getUsername());
            otp.setCode(code);
            otpRepository.save(otp);
        }
    }

}
