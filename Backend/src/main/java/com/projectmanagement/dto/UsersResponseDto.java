package com.projectmanagement.dto;

import java.util.List;

import com.projectmanagement.entity.User;

import lombok.Data;

@Data
public class UsersResponseDto extends CommonApiResponse {
	
	private List<User> users;

}
