package org.buglaban.travelapi.service;

import org.buglaban.travelapi.dto.request.ChangePasswordRequest;
import org.buglaban.travelapi.dto.request.LoginRequestDTO;
import org.buglaban.travelapi.dto.request.RegisterRequestDTO;
import org.buglaban.travelapi.dto.request.UserRequestDTO;
import org.buglaban.travelapi.dto.response.PageResponse;
import org.buglaban.travelapi.dto.response.UserDetailResponseDTO;
import org.buglaban.travelapi.util.UserStatus;

import java.util.List;

public interface IUserService {
    long userRegister (RegisterRequestDTO requestDTO);
    long userLogin (LoginRequestDTO requestDTO);
    UserDetailResponseDTO getUser(long userId);
    void updateUser (long userId, UserRequestDTO userRequestDTO);
    void deleteUser (long id);
    void changeStatusUser (long id, UserStatus userStatus);
    PageResponse<?> getAllUser (int pageNo, int pageSize);
    void resetPassword (long userId, ChangePasswordRequest changePasswordRequest);
}
