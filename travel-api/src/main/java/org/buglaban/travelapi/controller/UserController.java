package org.buglaban.travelapi.controller;

import com.sun.java.accessibility.util.Translator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.buglaban.travelapi.dto.request.ChangePasswordRequest;
import org.buglaban.travelapi.dto.request.ChangeStatusRequest;
import org.buglaban.travelapi.dto.request.RegisterRequestDTO;
import org.buglaban.travelapi.dto.request.UserRequestDTO;
import org.buglaban.travelapi.dto.response.PageResponse;
import org.buglaban.travelapi.dto.response.ResponseData;
import org.buglaban.travelapi.dto.response.ResponseFailure;
import org.buglaban.travelapi.dto.response.UserDetailResponseDTO;
import org.buglaban.travelapi.model.User;
import org.buglaban.travelapi.service.IUserService;
import org.buglaban.travelapi.util.UserStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/user/")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final IUserService iUserService;

    @PostMapping("register")
    public ResponseData<Long> registerUser (@Valid @RequestBody RegisterRequestDTO requestDTO){
        try {
            long userId = iUserService.userRegister(requestDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), "User added successfully", userId);
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "User added fail");
        }
    }

    @GetMapping("{id}")
    public ResponseData<?> getDetailUser (@PathVariable long id){
        try {
            UserDetailResponseDTO detailResponseDTO = iUserService.getUser(id);
            return new ResponseData<>(HttpStatus.CREATED.value(), "User get successfully", detailResponseDTO);
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "User get fail");
        }
    }

    @PatchMapping("update/{id}")
    public ResponseData<?> updateUser (@PathVariable long id, @Valid @RequestBody UserRequestDTO requestDTO){
        try {
            iUserService.updateUser(id, requestDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), "User update successfully");
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "User added fail");
        }
    }

    @PutMapping("{id}/status")
    public ResponseData<?> changeUser (@PathVariable long id, @RequestBody ChangeStatusRequest request){
        try {
            iUserService.changeStatusUser(id, request.getStatus());
            return new ResponseData<>(HttpStatus.OK.value(), "User change successfully");
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "User change fail");
        }
    }

    @DeleteMapping ("delete/{id}")
    public ResponseData<?> deleteUser (@PathVariable long id){
        try {
            iUserService.deleteUser(id);
            return new ResponseData<>(HttpStatus.OK.value(), "User delete successfully");
        } catch (Exception e) {
//            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "User delete fail");
        }
    }

    @GetMapping ("")
    public ResponseData<?> getAllUser (@RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        try {
            PageResponse<?> response = iUserService.getAllUser(page, pageSize);
            return new ResponseData<>(HttpStatus.OK.value(), "Get user successfully", response);
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Get user fail");
        }
    }

    @PutMapping ("reset-password/{id}")
    public ResponseData<?> resetPassword (@PathVariable long id,
                                          @RequestBody ChangePasswordRequest changePasswordRequest){
        try {
            iUserService.resetPassword(id, changePasswordRequest);
            return new ResponseData<>(HttpStatus.OK.value(), "Get user successfully");
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Get user fail");
        }
    }
}
