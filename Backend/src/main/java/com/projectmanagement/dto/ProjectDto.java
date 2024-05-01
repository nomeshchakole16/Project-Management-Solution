package com.projectmanagement.dto;

import lombok.Data;

@Data
public class ProjectDto {
	
	private int id;
	
	private String name;
	
	private String description;
	
	private String assignedToTeamlead;
	
	private String assignedToTeammember;
	
	private String projectStatus;
	
    private int teammemberId;
	
	private int teamleadId;
	
	private String teamleadName;
	
	private String teammemberName;
	
	private String requirement;
	
	private String createdDate;
	
	private String assignedDate;
	
	private String deadlineDate;

}
