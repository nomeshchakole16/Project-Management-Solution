package com.projectmanagement.dto;

import lombok.Data;

@Data
public class UpdateProjectRequestDto {
	
	private int projectId;
	
	private int teamleadId;
	
	private int teammemberId;
	
	private String projectStatus;

}
