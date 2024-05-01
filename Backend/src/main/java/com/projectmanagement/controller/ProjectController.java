package com.projectmanagement.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projectmanagement.dto.CommonApiResponse;
import com.projectmanagement.dto.ProjectDto;
import com.projectmanagement.dto.ProjectResponseDto;
import com.projectmanagement.dto.UpdateProjectRequestDto;
import com.projectmanagement.entity.Project;
import com.projectmanagement.entity.User;
import com.projectmanagement.service.ProjectService;
import com.projectmanagement.service.UserService;
import com.projectmanagement.utility.Constants.ProjectAssignStatus;
import com.projectmanagement.utility.Constants.ProjectStatus;
import com.projectmanagement.utility.Constants.UserRole;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("api/project/")
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectController {
	
	Logger LOG = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
    private ProjectService projectService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("add")
	@ApiOperation(value = "Api to add project")
	public ResponseEntity<CommonApiResponse> addProject(@RequestBody Project project) {
		
		LOG.info("Recieved request for adding the project");

		CommonApiResponse response = new CommonApiResponse();

		 // Get today's date
       // LocalDate today = LocalDate.now();

        // Define the desired format yyyy-mm-dd
       // String desiredFormat = "YYYY-MM-DD"; // Change this format as needed

        // Create a DateTimeFormatter with the desired format
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern(desiredFormat);

        // Format the date to the desired format
        //String formattedTodaysDate = today.format(formatter);
		LocalDate today = LocalDate.now();
        String desiredFormat = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(desiredFormat);
        String formattedTodaysDate = today.format(formatter);
        
        if(project == null) {
			response.setSuccess(true);
			response.setResponseMessage("bad request, missing project data");
			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
		project.setAssignStatus(ProjectAssignStatus.NOT_ASSIGNED.value());
		project.setStatus(ProjectAssignStatus.NOT_ASSIGNED_TO_TEAMLEAD.value());
		project.setCreatedDate(formattedTodaysDate);
		
		Project addedProduct = this.projectService.addProject(project);
		
		if (addedProduct != null) {
			response.setSuccess(true);
			response.setResponseMessage("Project Added Successfully");
			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
		}

		else {
			response.setSuccess(true);
			response.setResponseMessage("Failed to add project");
			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("fetch")
	@ApiOperation(value = "Api to fetch all projects")
	public ResponseEntity<ProjectResponseDto> fetchAllProjects() {
		LOG.info("Recieved request for Fetching all the projects");

		ProjectResponseDto response = new ProjectResponseDto();
		
		List<ProjectDto> projectDtos = new ArrayList<>(); 
		
        List<Project> projects = new ArrayList<>();
        
        projects = this.projectService.getAllProjects();
        
        if(projects == null) {
        	response.setProjects(projectDtos);
			response.setSuccess(true);
			response.setResponseMessage("Projects fetched successful");
			return new ResponseEntity<ProjectResponseDto>(response, HttpStatus.OK);
		}
        
        for(Project project : projects) {
        	ProjectDto projectDto = new ProjectDto();
        	projectDto.setId(project.getId());
        	projectDto.setName(project.getName());
        	projectDto.setDescription(project.getDescription());
        	projectDto.setCreatedDate(project.getCreatedDate());
        	projectDto.setRequirement(project.getRequirement());
        	projectDto.setDeadlineDate(project.getDeadlineDate());
        	projectDto.setProjectStatus(project.getStatus());
        	
        	if(project.getTeamleadId() == 0) {
        		
        		projectDto.setAssignedToTeamlead(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setAssignedDate(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeamleadName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setProjectStatus(ProjectAssignStatus.NOT_ASSIGNED_TO_TEAMLEAD.value());
        	    
        	    projectDto.setAssignedToTeammember(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeammemberName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	}
        	
			else {
				User teamlead = this.userService.getUserById(project.getTeamleadId());

				projectDto.setTeamleadName(teamlead.getFirstName() + " " + teamlead.getLastName());
				projectDto.setTeamleadId(teamlead.getId());
				projectDto.setAssignedToTeamlead(ProjectAssignStatus.ASSIGNED_TO_TEAMLEAD.value());
				projectDto.setAssignedDate(project.getAssignedDate());

			}
        	
        	if(project.getTeammemberId() == 0) {
        		projectDto.setAssignedToTeammember(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeammemberName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	} 
        	
			else {
				User teamember = this.userService.getUserById(project.getTeammemberId());

				projectDto.setTeammemberName(teamember.getFirstName() + " " + teamember.getLastName());
				projectDto.setAssignedToTeammember(ProjectAssignStatus.ASSIGNED_TO_TEAMMEMBER.value());

				projectDtos.add(projectDto);
			}
        	
        }
        
        response.setProjects(projectDtos);
		response.setSuccess(true);
		response.setResponseMessage("Projects fetched successful");
		return new ResponseEntity<ProjectResponseDto>(response, HttpStatus.OK);

	}
	
	@GetMapping("search")
	@ApiOperation(value = "Api to fetch all projects by name")
	public ResponseEntity<ProjectResponseDto> fetchAllProjectsByName(@RequestParam("projectName") String projectName) {
		LOG.info("Recieved request for Fetch all projects by name");
		
		ProjectResponseDto response = new ProjectResponseDto();

		List<ProjectDto> projectDtos = new ArrayList<>(); 
		
        List<Project> projects = new ArrayList<>();
        
        projects = this.projectService.getAllProjectsByProjectName(projectName);
        
        if(projects == null) {
        	response.setProjects(projectDtos);
			response.setSuccess(true);
			response.setResponseMessage("Projects fetched successful");
			return new ResponseEntity<ProjectResponseDto>(response, HttpStatus.OK);
		}
        
        for(Project project : projects) {
        	ProjectDto projectDto = new ProjectDto();
        	projectDto.setId(project.getId());
        	projectDto.setName(project.getName());
        	projectDto.setDescription(project.getDescription());
        	projectDto.setCreatedDate(project.getCreatedDate());
        	projectDto.setRequirement(project.getRequirement());
        	projectDto.setDeadlineDate(project.getDeadlineDate());
        	projectDto.setProjectStatus(project.getStatus());
        	
        	if(project.getTeamleadId() == 0) {
        		
        		projectDto.setAssignedToTeamlead(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setAssignedDate(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeamleadName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setProjectStatus(ProjectAssignStatus.NOT_ASSIGNED_TO_TEAMLEAD.value());
        	    
        	    projectDto.setAssignedToTeammember(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeammemberName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	}
        	
			else {
				User teamlead = this.userService.getUserById(project.getTeamleadId());

				projectDto.setTeamleadName(teamlead.getFirstName() + " " + teamlead.getLastName());
				projectDto.setTeamleadId(teamlead.getId());
				projectDto.setAssignedToTeamlead(ProjectAssignStatus.ASSIGNED_TO_TEAMLEAD.value());
				projectDto.setAssignedDate(project.getAssignedDate());

			}
        	
        	if(project.getTeammemberId() == 0) {
        		projectDto.setAssignedToTeammember(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeammemberName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	} 
        	
			else {
				User teammember = this.userService.getUserById(project.getTeammemberId());

				projectDto.setTeammemberName(teammember.getFirstName() + " " + teammember.getLastName());
				projectDto.setAssignedToTeammember(ProjectAssignStatus.ASSIGNED_TO_TEAMMEMBER.value());

				projectDtos.add(projectDto);
			}
        	
        }
        
        response.setProjects(projectDtos);
		response.setSuccess(true);
		response.setResponseMessage("Projects fetched successful");
		return new ResponseEntity<ProjectResponseDto>(response, HttpStatus.OK);

	}
	
	@GetMapping("search/id")
	@ApiOperation(value = "Api to fetch all projects by id")
	public ResponseEntity<ProjectResponseDto> fetchAllProjectsByName(@RequestParam("projectId") int projectId) {
		LOG.info("Recieved request for Fetch project by id");

		ProjectResponseDto response = new ProjectResponseDto();
		
		List<ProjectDto> projectDtos = new ArrayList<>(); 
		
        List<Project> projects = new ArrayList<>();
        
        if(projectId == 0) {
        	response.setProjects(projectDtos);
			response.setSuccess(true);
			response.setResponseMessage("Project not found using this Id");
			return new ResponseEntity<ProjectResponseDto>(response, HttpStatus.OK);
		}
        
        Project p = this.projectService.getProjectById(projectId);

        if(p != null) {
          projects.add(p);
        }
        
        for(Project project : projects) {
        	ProjectDto projectDto = new ProjectDto();
        	projectDto.setId(project.getId());
        	projectDto.setName(project.getName());
        	projectDto.setDescription(project.getDescription());
        	projectDto.setCreatedDate(project.getCreatedDate());
        	projectDto.setRequirement(project.getRequirement());
        	projectDto.setDeadlineDate(project.getDeadlineDate());
        	projectDto.setProjectStatus(project.getStatus());
        	
        	if(project.getTeamleadId() == 0) {
        		
        		projectDto.setAssignedToTeamlead(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setAssignedDate(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeamleadName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setProjectStatus(ProjectAssignStatus.NOT_ASSIGNED_TO_TEAMLEAD.value());
        	    
        	    projectDto.setAssignedToTeammember(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeammemberName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	}
        	
			else {
				User teamlead = this.userService.getUserById(project.getTeamleadId());

				projectDto.setTeamleadName(teamlead.getFirstName() + " " + teamlead.getLastName());
				projectDto.setTeamleadId(teamlead.getId());
				projectDto.setAssignedToTeamlead(ProjectAssignStatus.ASSIGNED_TO_TEAMLEAD.value());
				projectDto.setAssignedDate(project.getAssignedDate());

			}
        	
        	if(project.getTeammemberId() == 0) {
        		projectDto.setAssignedToTeammember(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeammemberName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	} 
        	
			else {
				User teammember = this.userService.getUserById(project.getTeammemberId());

				projectDto.setTeammemberName(teammember.getFirstName() + " " + teammember.getLastName());
				projectDto.setAssignedToTeammember(ProjectAssignStatus.ASSIGNED_TO_TEAMMEMBER.value());

				projectDtos.add(projectDto);
			}
        	
        }
        
        response.setProjects(projectDtos);
		response.setSuccess(true);
		response.setResponseMessage("Projects fetched successful");
		return new ResponseEntity<ProjectResponseDto>(response, HttpStatus.OK);

	}
	
	@PostMapping("update")
	@ApiOperation(value = "Api to update the project status")
	public ResponseEntity<CommonApiResponse> updateProject(@RequestBody UpdateProjectRequestDto updateProjectRequest) {
		
		LOG.info("Recieved request for updating the project");

		CommonApiResponse response = new CommonApiResponse();
		
		if(updateProjectRequest == null) {
			response.setSuccess(true);
			response.setResponseMessage("request data is missing");
			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		 // Get today's date
		LocalDate today = LocalDate.now();
        String desiredFormat = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(desiredFormat);
        String formattedTodaysDate = today.format(formatter);
		
		Project project = this.projectService.getProjectById(updateProjectRequest.getProjectId());

		// PROJECT MANAGER is assigning the project to TEAM LEAD
		if (updateProjectRequest.getTeamleadId() != 0) {
			
			User teamlead = this.userService.getUserById(updateProjectRequest.getTeamleadId());
			
			if(teamlead == null || !teamlead.getRole().equals(UserRole.TEAMLEAD.value())) {
				response.setSuccess(true);
				response.setResponseMessage("failed to assign the project to teamlead");
				return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
			}
			
			project.setTeamleadId(teamlead.getId());
			project.setAssignedDate(formattedTodaysDate);
			project.setStatus(ProjectAssignStatus.NOT_ASSIGNED_TO_TEAMMEMBER.value());
			
			Project updatedProject = this.projectService.updateProject(project);
			
			if(updatedProject == null) {
				response.setSuccess(true);
				response.setResponseMessage("failed to update the project status");
				return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
			} else {
				response.setSuccess(true);
				response.setResponseMessage("assigned project to teamlead successfully");
				return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
			}

		}

		// project manager is assigning the project to team member
		if (updateProjectRequest.getTeammemberId() != 0) {
            User teammember = this.userService.getUserById(updateProjectRequest.getTeammemberId());
			
			if(teammember == null || !teammember.getRole().equals(UserRole.TEAMMEMBER.value())) {
				response.setSuccess(true);
				response.setResponseMessage("failed to assign the project to teammember");
				return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
			}
			
			project.setTeammemberId(teammember.getId());
			project.setStatus(ProjectStatus.PENDING.value());
			
			Project updatedProject = this.projectService.updateProject(project);
			
			if(updatedProject == null) {
				response.setSuccess(true);
				response.setResponseMessage("failed to assign the project to teammember");
				return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
			} else {
				response.setSuccess(true);
				response.setResponseMessage("assigned project to teammember successfully");
				return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
			}

		}
		
		// teammember is updating the project status
		if(updateProjectRequest.getProjectStatus() != null && !StringUtils.isEmpty(updateProjectRequest.getProjectStatus())) {
			
			project.setStatus(updateProjectRequest.getProjectStatus());
			
			Project updatedProject = this.projectService.updateProject(project);
			
			if(updatedProject == null) {
				response.setSuccess(true);
				response.setResponseMessage("failed to update the project status");
				return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
			} else {
				response.setSuccess(true);
				response.setResponseMessage("project status updated successfully");
				return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
			}
		}
		
		return null;
	
	}
	
	@GetMapping("fetch/teamlead")
	@ApiOperation(value = "Api to fetch all projects by teamlead id")
	public ResponseEntity<ProjectResponseDto> fetchAllProjectsByTeamleadId(@RequestParam("teamleadId") int teamleadId) {
		LOG.info("Recieved request for Fetch projects by using teamlead Id");

		ProjectResponseDto response = new ProjectResponseDto();
		
		List<ProjectDto> projectDtos = new ArrayList<>(); 
		
        List<Project> projects = new ArrayList<>();
        
        projects = this.projectService.getAllProjectsByManagerId(teamleadId);
        
        if(projects == null) {
        	response.setProjects(projectDtos);
			response.setSuccess(true);
			response.setResponseMessage("Projects fetched successful");
			return new ResponseEntity<ProjectResponseDto>(response, HttpStatus.OK);
		}
        
        for(Project project : projects) {
        	ProjectDto projectDto = new ProjectDto();
        	projectDto.setId(project.getId());
        	projectDto.setName(project.getName());
        	projectDto.setDescription(project.getDescription());
        	projectDto.setCreatedDate(project.getCreatedDate());
        	projectDto.setRequirement(project.getRequirement());
        	projectDto.setDeadlineDate(project.getDeadlineDate());
        	projectDto.setProjectStatus(project.getStatus());
        	
        	if(project.getTeamleadId() == 0) {
        		
        		projectDto.setAssignedToTeamlead(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setAssignedDate(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeamleadName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setProjectStatus(ProjectAssignStatus.NOT_ASSIGNED_TO_TEAMLEAD.value());
        	    
        	    projectDto.setAssignedToTeammember(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeammemberName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	}
        	
			else {
				User teamlead = this.userService.getUserById(project.getTeamleadId());

				projectDto.setTeamleadName(teamlead.getFirstName() + " " +teamlead.getLastName());
				projectDto.setTeamleadId(teamlead.getId());
				projectDto.setAssignedToTeamlead(ProjectAssignStatus.ASSIGNED_TO_TEAMLEAD.value());
				projectDto.setAssignedDate(project.getAssignedDate());

			}
        	
        	if(project.getTeammemberId() == 0) {
        		projectDto.setAssignedToTeammember(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeammemberName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    
        	    projectDtos.add(projectDto);
        	    
        	    continue;
			}

			else {
				User teammember = this.userService.getUserById(project.getTeammemberId());

				projectDto.setTeammemberName(teammember.getFirstName() + " " + teammember.getLastName());
				projectDto.setAssignedToTeammember(ProjectAssignStatus.ASSIGNED_TO_TEAMMEMBER.value());

				projectDtos.add(projectDto);
			}

		}

		response.setProjects(projectDtos);
		response.setSuccess(true);
		response.setResponseMessage("Projects fetched successful");
		return new ResponseEntity<ProjectResponseDto>(response, HttpStatus.OK);
		

	}
	
	@GetMapping("fetch/teammember")
	@ApiOperation(value = "Api to fetch all projects by teamlead id")
	public ResponseEntity<ProjectResponseDto> fetchAllProjectsByTeammemberId(@RequestParam("teammemberId") int teammemberId) {
		LOG.info("Recieved request for Fetch projects by using employee Id");

		ProjectResponseDto response = new ProjectResponseDto();

		List<ProjectDto> projectDtos = new ArrayList<>(); 
		
        List<Project> projects = new ArrayList<>();
        
        projects = this.projectService.getAllProjectsByTeammemberId(teammemberId);
        
        if(projects == null) {
        	response.setProjects(projectDtos);
			response.setSuccess(true);
			response.setResponseMessage("Projects fetched successful");
			return new ResponseEntity<ProjectResponseDto>(response, HttpStatus.OK);
		}
        
        for(Project project : projects) {
        	ProjectDto projectDto = new ProjectDto();
        	projectDto.setId(project.getId());
        	projectDto.setName(project.getName());
        	projectDto.setDescription(project.getDescription());
        	projectDto.setCreatedDate(project.getCreatedDate());
        	projectDto.setRequirement(project.getRequirement());
        	projectDto.setDeadlineDate(project.getDeadlineDate());
        	projectDto.setProjectStatus(project.getStatus());
        	
        	if(project.getTeamleadId() == 0) {
        		
        		projectDto.setAssignedToTeamlead(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setAssignedDate(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeamleadName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setProjectStatus(ProjectAssignStatus.NOT_ASSIGNED_TO_TEAMLEAD.value());
        	    
        	    projectDto.setAssignedToTeammember(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeammemberName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	}
        	
			else {
				User teamlead = this.userService.getUserById(project.getTeamleadId());

				projectDto.setTeamleadName(teamlead.getFirstName() + " " + teamlead.getLastName());
				projectDto.setTeamleadId(teamlead.getId());
				projectDto.setAssignedToTeamlead(ProjectAssignStatus.ASSIGNED_TO_TEAMLEAD.value());
				projectDto.setAssignedDate(project.getAssignedDate());

			}
        	
        	if(project.getTeammemberId() == 0) {
        		projectDto.setAssignedToTeammember(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeammemberName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	} 
        	
			else {
				User teammember = this.userService.getUserById(project.getTeammemberId());

				projectDto.setTeammemberName(teammember.getFirstName() + " " + teammember.getLastName());
				projectDto.setAssignedToTeammember(ProjectAssignStatus.ASSIGNED_TO_TEAMMEMBER.value());

				projectDtos.add(projectDto);
			}
        	
        }
        
        response.setProjects(projectDtos);
		response.setSuccess(true);
		response.setResponseMessage("Projects fetched successful");
		return new ResponseEntity<ProjectResponseDto>(response, HttpStatus.OK);

	}
	
	@GetMapping("teamlead/search")
	@ApiOperation(value = "Api to fetch all projects by name")
	public ResponseEntity<ProjectResponseDto> fetchAllProjectsByNameAndTeamlead(@RequestParam("projectName") String projectName, @RequestParam("managerId") int managerId) {
		LOG.info("Recieved request for searching the project by using project name and teamlead id");

		ProjectResponseDto response = new ProjectResponseDto();
		List<ProjectDto> projectDtos = new ArrayList<>(); 
		
		if(projectName == null || managerId == 0) {
			response.setProjects(projectDtos);
			response.setSuccess(true);
			response.setResponseMessage("bad request, request data is missing");
			return new ResponseEntity<ProjectResponseDto>(response, HttpStatus.BAD_REQUEST);
		}
		
        List<Project> projects = new ArrayList<>();
        
        projects = this.projectService.getAllProjectsByProjectNameAndTeamleadId(projectName, managerId);
        
        if(projects == null) {
        	response.setProjects(projectDtos);
			response.setSuccess(true);
			response.setResponseMessage("Projects fetched successful");
			return new ResponseEntity<ProjectResponseDto>(response, HttpStatus.OK);
		}
        
        for(Project project : projects) {
        	ProjectDto projectDto = new ProjectDto();
        	projectDto.setId(project.getId());
        	projectDto.setName(project.getName());
        	projectDto.setDescription(project.getDescription());
        	projectDto.setCreatedDate(project.getCreatedDate());
        	projectDto.setRequirement(project.getRequirement());
        	projectDto.setDeadlineDate(project.getDeadlineDate());
        	projectDto.setProjectStatus(project.getStatus());
        	
        	if(project.getTeamleadId() == 0) {
        		
        		projectDto.setAssignedToTeamlead(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setAssignedDate(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeamleadName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setProjectStatus(ProjectAssignStatus.NOT_ASSIGNED_TO_TEAMLEAD.value());
        	    
        	    projectDto.setAssignedToTeammember(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeammemberName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	}
        	
			else {
				User teamlead = this.userService.getUserById(project.getTeamleadId());

				projectDto.setTeamleadName(teamlead.getFirstName() + " " + teamlead.getLastName());
				projectDto.setTeamleadId(teamlead.getId());
				projectDto.setAssignedToTeamlead(ProjectAssignStatus.ASSIGNED_TO_TEAMLEAD.value());
				projectDto.setAssignedDate(project.getAssignedDate());

			}
        	
        	if(project.getTeammemberId() == 0) {
        		projectDto.setAssignedToTeammember(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeammemberName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	} 
        	
			else {
				User teammember = this.userService.getUserById(project.getTeammemberId());

				projectDto.setTeammemberName(teammember.getFirstName() + " " + teammember.getLastName());
				projectDto.setAssignedToTeammember(ProjectAssignStatus.ASSIGNED_TO_TEAMMEMBER.value());

				projectDtos.add(projectDto);
			}
        	
        }
        
        response.setProjects(projectDtos);
		response.setSuccess(true);
		response.setResponseMessage("Projects fetched successful");
		return new ResponseEntity<ProjectResponseDto>(response, HttpStatus.OK);

	}
	
	@GetMapping("teammember/search")
	@ApiOperation(value = "Api to fetch all projects by name")
	public ResponseEntity<ProjectResponseDto> fetchAllProjectsByNameAndTeammember(@RequestParam("projectName") String projectName, @RequestParam("employeeId") int employeeId) {
		LOG.info("Recieved request for searching the project by using project name and teamlead id");

		ProjectResponseDto response = new ProjectResponseDto();

		List<ProjectDto> projectDtos = new ArrayList<>(); 
		
		if(projectName == null || employeeId == 0) {
			response.setProjects(projectDtos);
			response.setSuccess(true);
			response.setResponseMessage("bad request, request data is missing");
			return new ResponseEntity<ProjectResponseDto>(response, HttpStatus.BAD_REQUEST);
		}
		
        List<Project> projects = new ArrayList<>();
        
        projects = this.projectService.getAllProjectsByProjectNameAndTeammemberId(projectName, employeeId);
        
        if(projects == null) {
        	response.setProjects(projectDtos);
			response.setSuccess(true);
			response.setResponseMessage("Projects fetched successful");
			return new ResponseEntity<ProjectResponseDto>(response, HttpStatus.OK);
		}
        
        for(Project project : projects) {
        	ProjectDto projectDto = new ProjectDto();
        	projectDto.setId(project.getId());
        	projectDto.setName(project.getName());
        	projectDto.setDescription(project.getDescription());
        	projectDto.setCreatedDate(project.getCreatedDate());
        	projectDto.setRequirement(project.getRequirement());
        	projectDto.setDeadlineDate(project.getDeadlineDate());
        	projectDto.setProjectStatus(project.getStatus());
        	
        	if(project.getTeamleadId() == 0) {
        		
        		projectDto.setAssignedToTeamlead(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setAssignedDate(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeamleadName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setProjectStatus(ProjectAssignStatus.NOT_ASSIGNED_TO_TEAMLEAD.value());
        	    
        	    projectDto.setAssignedToTeammember(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeammemberName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	}
        	
			else {
				User teamlead = this.userService.getUserById(project.getTeamleadId());

				projectDto.setTeamleadName(teamlead.getFirstName() + " " + teamlead.getLastName());
				projectDto.setTeamleadId(teamlead.getId());
				projectDto.setAssignedToTeamlead(ProjectAssignStatus.ASSIGNED_TO_TEAMLEAD.value());
				projectDto.setAssignedDate(project.getAssignedDate());

			}
        	
        	if(project.getTeammemberId() == 0) {
        		projectDto.setAssignedToTeammember(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    projectDto.setTeammemberName(ProjectAssignStatus.NOT_ASSIGNED.value());
        	    
        	    projectDtos.add(projectDto);
        	    
        	    continue;
        	} 
        	
			else {
				User teammember = this.userService.getUserById(project.getTeammemberId());

				projectDto.setTeammemberName(teammember.getFirstName() + " " + teammember.getLastName());
				projectDto.setAssignedToTeammember(ProjectAssignStatus.ASSIGNED_TO_TEAMMEMBER.value());

				projectDtos.add(projectDto);
			}
        	
        }
        
        response.setProjects(projectDtos);
		response.setSuccess(true);
		response.setResponseMessage("Projects fetched successful");
		return new ResponseEntity<ProjectResponseDto>(response, HttpStatus.OK);

	}
	
	@GetMapping("allStatus")
	@ApiOperation(value = "Api to fetch all projects by name")
	public ResponseEntity<List<String>> fetchAllProjectStatus() {
		LOG.info("Recieved request for Fecth all the project status");

        List<String> allStatus = new ArrayList<>();
        
        for(ProjectStatus status : ProjectStatus.values()) {
        	allStatus.add(status.value());
        }
		
		return new ResponseEntity<List<String>>(allStatus, HttpStatus.OK);

	}
	
}
