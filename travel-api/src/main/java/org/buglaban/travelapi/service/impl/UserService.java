package org.buglaban.travelapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.buglaban.travelapi.dto.request.ChangePasswordRequest;
import org.buglaban.travelapi.dto.request.LoginRequestDTO;
import org.buglaban.travelapi.dto.request.RegisterRequestDTO;
import org.buglaban.travelapi.dto.request.UserRequestDTO;
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

    @Override
    public long userRegister(RegisterRequestDTO requestDTO) {
        Role userRole = iRoleRepository
                .findByRoleName(UserType.USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = User.builder()
                .fullName(requestDTO.getFullName())
                .email(requestDTO.getEmail())
//                .passwordHash(passwordEncoder.encode(requestDTO.getPasswordHash()))
                .passwordHash(requestDTO.getPasswordHash())
                .status(UserStatus.ACTIVE)
                .emailVerified(false)
                .role(userRole)
                .build();
        iUserRepository.save(user);
        return user.getId();
    }

    @Override
    public long userLogin(LoginRequestDTO requestDTO) {
        Optional<User> optionalUser = iUserRepository.findByEmail(requestDTO.getEmail());
        if(optionalUser.isEmpty()) {
            throw new DataNotFoundException("Wrong email number or password");
        }

        return 0;
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
    public PageResponse<?> getAllUser(int pageNo, int pageSize) {
        Page<User> pageUsers = iUserRepository.findAll(PageRequest.of(pageNo, pageSize));
        List<UserDetailResponseDTO> responseDTO = pageUsers.stream().map(user -> UserDetailResponseDTO.builder()
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

        return PageResponse.builder()
                .page(pageNo)
                .pageSize(pageSize)
                .totalPage(pageUsers.getTotalPages())
                .item(responseDTO)
                .build();
    }

    @Override
    public void resetPassword(long userId, ChangePasswordRequest changePasswordRequest) {

    }
}
