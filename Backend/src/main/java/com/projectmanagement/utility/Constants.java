package com.projectmanagement.utility;

public class Constants {
	
	public enum UserRole {
		PROJECTMANAGER("projectmanager"),
		TEAMMEMBER("teammember"),
		TEAMLEAD("teamlead");
		
		private String role;

	    private UserRole(String role) {
	      this.role = role;
	    }

	    public String value() {
	      return this.role;
	    }    
	}
	
	public enum Sex {
		MALE("Male"),
		FEMALE("Female");
		
		
		private String sex;

	    private Sex(String sex) {
	      this.sex = sex;
	    }

	    public String value() {
	      return this.sex;
	    }    
	}
	
	public enum ProjectStatus {
		COMPLETED("Completed"),
		PENDING("Pending"),
		WORKING("Working");
		
		private String status;

	    private ProjectStatus(String status) {
	      this.status = status;
	    }

	    public String value() {
	      return this.status;
	    }    
	}
	
	public enum ProjectAssignStatus {
		ASSIGNED_TO_TEAMLEAD("Assigned to Teamlead"),
		ASSIGNED_TO_TEAMMEMBER("Assigned to Teammember"),
		NOT_ASSIGNED_TO_TEAMMEMBER("Not Assigned to Teammember"),
		NOT_ASSIGNED_TO_TEAMLEAD("Not Assigned to Teamlead"),
		NOT_ASSIGNED("Not Assigned");
		
		
		private String status;

	    private ProjectAssignStatus(String status) {
	      this.status = status;
	    }

	    public String value() {
	      return this.status;
	    }    
	}
	
	public enum ResponseCode {
		SUCCESS(0),
		FAILED(1);
		
		
		private int code;

	    private ResponseCode(int code) {
	      this.code = code;
	    }

	    public int value() {
	      return this.code;
	    }    
	}
	
	public enum UserStatus {
		ACTIVE(1),
		DELETED(0);
		
		
		private int status;

	    private UserStatus(int status) {
	      this.status = status;
	    }

	    public int value() {
	      return this.status;
	    }    
	}
	
}
