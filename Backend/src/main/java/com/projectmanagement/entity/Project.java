package com.projectmanagement.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Project {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	private String description;
	
	private String requirement;
	
	private int teammemberId;
	
	private int teamleadId;
	
	private String status;
	
	private String assignStatus;
	
	private String createdDate;
	
	private String assignedDate;
	
	private String deadlineDate;
	
}
