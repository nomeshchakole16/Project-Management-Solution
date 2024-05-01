package com.projectmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectmanagement.dao.ProjectDao;
import com.projectmanagement.entity.Project;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectDao projectDao;
	
	public Project addProject(Project project) {
		return projectDao.save(project);
	}
	
	public Project updateProject(Project project) {
		return projectDao.save(project);
	}
	
	public List<Project> getAllProjects() {
		return projectDao.findAll();
	}
	
	public Project getProjectById(int projectId) {
		
		Project p = null;
		
		Optional<Project> oP = projectDao.findById(projectId);
		
		if(oP.isPresent()) {
			p = oP.get();
		}
		
		return p;
	}
	
	public List<Project> getAllProjectsByProjectName(String projectName) {
		return projectDao.findByNameContainingIgnoreCase(projectName);
	}
	
	public List<Project> getAllProjectsByProjectNameAndTeamleadId(String projectName, int teamleadId) {
		return projectDao.findByNameContainingIgnoreCaseAndTeamleadId(projectName, teamleadId);
	}
	
	public List<Project> getAllProjectsByProjectNameAndTeammemberId(String projectName, int teammemberId) {
		return projectDao.findByNameContainingIgnoreCaseAndTeammemberId(projectName, teammemberId);
	}
	
	public List<Project> getAllProjectsByTeammemberId(int teammemberId) {
		return projectDao.findByTeammemberId(teammemberId);
	}
	
	public List<Project> getAllProjectsByManagerId(int teamleadId) {
		return projectDao.findByTeamleadId(teamleadId);
	}

}
