package com.company.authenticate.service;

import com.company.authenticate.dto.ApiResponseDTO;
import com.company.authenticate.dto.UserProfileDTO;
import com.company.authenticate.dto.UserProfileResponseDTO;
import com.company.authenticate.entity.UserProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

public interface UserProfileService {

    ApiResponseDTO<String> testDatabaseConnection();

    UserProfileDTO toDTO(UserProfileEntity entity);

    UserProfileEntity toEntity(UserProfileDTO dto);

    ApiResponseDTO<List<UserProfileDTO>> fetchPagedDataList(Pageable pageable);

    Page<UserProfileDTO> fetchPageData(Pageable pageable);

    ApiResponseDTO<UserProfileResponseDTO> addDataToDataBase(ArrayList<UserProfileDTO> userList);

    void addData(UserProfileEntity authenticate);

    ApiResponseDTO<UserProfileResponseDTO> searchDataBase(long userId);

    UserProfileEntity searchData(long userId);

    ApiResponseDTO<UserProfileResponseDTO> updateDataToDataBase(ArrayList<UserProfileDTO> userList);

    ApiResponseDTO<UserProfileResponseDTO> deleteDataFromDataBase(ArrayList<UserProfileDTO> userList);

    UserDetails loadUserByUsername(String username);
}
