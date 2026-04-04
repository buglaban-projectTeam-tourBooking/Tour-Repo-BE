package org.buglaban.travelapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.buglaban.travelapi.components.JwtTokenUtils;
import org.buglaban.travelapi.dto.request.ChangePasswordRequest;
import org.buglaban.travelapi.dto.request.LoginRequestDTO;
import org.buglaban.travelapi.dto.request.RegisterRequestDTO;
import org.buglaban.travelapi.dto.request.UserRequestDTO;
import org.buglaban.travelapi.dto.response.LoginResponse;
import org.buglaban.travelapi.dto.response.PageResponse;
import org.buglaban.travelapi.dto.response.UserDetailResponseDTO;
import org.buglaban.travelapi.exception.DataNotFoundException;
import org.buglaban.travelapi.model.Role;
import org.buglaban.travelapi.model.User;
import org.buglaban.travelapi.repository.IRoleRepository;
import org.buglaban.travelapi.repository.IUserRepository;
import org.buglaban.travelapi.service.IUserService;
import org.buglaban.travelapi.util.UserStatus;
import org.buglaban.travelapi.util.UserType;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final IUserRepository iUserRepository;
    private final IRoleRepository iRoleRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public long userRegister(RegisterRequestDTO requestDTO) {
        Role userRole = iRoleRepository
                .findByRoleName(UserType.USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = User.builder()
                .fullName(requestDTO.getFullName())
                .email(requestDTO.getEmail())
                .passwordHash(passwordEncoder.encode(requestDTO.getPassword()))
                .status(UserStatus.ACTIVE)
                .emailVerified(false)
                .role(userRole)
                .build();
        iUserRepository.save(user);
        return user.getId();
    }

    @Override
    public LoginResponse userLogin(LoginRequestDTO requestDTO) throws Exception {
        Optional<User> optionalUser = iUserRepository.findByEmail(requestDTO.getEmail());
        if(optionalUser.isEmpty()) {
            throw new DataNotFoundException("Wrong email number or password");
        }
        Role userRole = iRoleRepository.getRoleByIdUser(requestDTO.getEmail());
        User user = optionalUser.get();
        if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPasswordHash())){
            new BadCredentialsException("Wrong email number or password");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                requestDTO.getEmail(),requestDTO.getPassword(), user.getAuthorities()
        );

        authenticationManager.authenticate(authenticationToken);
        String token = jwtTokenUtils.generateToken(user);
        return new LoginResponse(token, userRole.getRoleName().name(), user.getFullName());
    }

    @Override
    public void deleteUser(long id) {
        Optional<User> user = iUserRepository.findById(id);
        if (user.isEmpty()) {
            throw new DataNotFoundException("data user not fail");
        }
        iUserRepository.deleteById(id);
    }

    @Override
    public void changeStatusUser(long id, UserStatus userStatus) {
        Optional<User> user = iUserRepository.findById(id);
        if (user.isEmpty()){
            throw new DataNotFoundException("Data not found");
        }
        User us = user.get();
        us.setStatus(userStatus);
        iUserRepository.save(us);
    }

    @Override
    public UserDetailResponseDTO getUser(long userId) {
        User user = iUserRepository.findById(userId).get();
        UserDetailResponseDTO responseDTO = mapper.map(user, UserDetailResponseDTO.class);
        return responseDTO;
    }

    @Override
    public void updateUser(long userId, UserRequestDTO userRequestDTO) {
        Optional<Role> role = iRoleRepository.findByRoleName(userRequestDTO.getUserType());
        User user = iUserRepository.findById(userId).get();
//        if (StringUtils.hasLength(userRequestDTO.getFullName())) {
//            user.setFullName(userRequestDTO.getFullName());
//        }
        user.setRole(role.get());
        mapper.map(userRequestDTO, user);
        iUserRepository.save(user);
    }

    @Override
    public PageResponse<UserDetailResponseDTO> getAllUser(int pageNo, int pageSize) {
        Page<User> pageUsers = iUserRepository.findAll(PageRequest.of(pageNo, pageSize));
        List<UserDetailResponseDTO> responseDTO = pageUsers.getContent().stream()
                .map(user -> UserDetailResponseDTO.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .passwordHash(user.getPasswordHash())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .address(user.getAddress())
                .build())
                .toList();

        return PageResponse.<UserDetailResponseDTO>builder()
                .page(pageNo)
                .pageSize(pageSize)
                .totalPage(pageUsers.getTotalPages())
                .totalElements(pageUsers.getTotalElements())
                .items(responseDTO)
                .build();
    }

    @Override
    public void resetPassword(long userId, ChangePasswordRequest changePasswordRequest) {
        Optional<User> user = iUserRepository.findById(userId);
        if (user.isEmpty()) {
            throw new DataNotFoundException("Data not found");
        }
        if (user.get().getPasswordHash().equals(changePasswordRequest.getOldPassword())) {
            User us = user.get();
            us.setPasswordHash(changePasswordRequest.getNewPassword());
            iUserRepository.save(us);
        }
    }
}
