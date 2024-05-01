package com.projectmanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectmanagement.entity.Project;

@Repository
public interface ProjectDao extends JpaRepository<Project, Integer> {
	
	List<Project> findByNameContainingIgnoreCase(String projectName);
	List<Project> findByNameContainingIgnoreCaseAndTeammemberId(String projectName, int teammember);
	List<Project> findByNameContainingIgnoreCaseAndTeamleadId(String projectName, int teamleadId);
	List<Project> findByTeammemberId(int teammemberId);
	List<Project> findByTeamleadId(int teamleadId);

}
