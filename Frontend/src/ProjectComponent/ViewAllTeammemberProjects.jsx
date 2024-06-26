import { useState, useEffect } from "react";
import axios from "axios";
import React from "react";
import { useNavigate } from "react-router-dom";

const ViewAllTeammemberProjects = () => {
  const teammember = JSON.parse(sessionStorage.getItem("active-teammember"));

  const [allProjects, setAllProjects] = useState([]);

  const [projectName, setProjectName] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    const getAllProject = async () => {
      const allProject = await retrieveAllProject();
      if (allProject) {
        setAllProjects(allProject.projects);
      }
    };

    getAllProject();
  }, []);

  const retrieveAllProject = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/project/fetch/teammember?teammemberId=" +
      teammember.id
    );
    console.log(response.data);
    return response.data;
  };

  const getProjectsByName = async () => {
    const allProject = await retrieveProjectByName();
    if (allProject) {
      setAllProjects(allProject.projects);
    }
  };

  const retrieveProjectByName = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/project/teammember/search?projectName=" +
        projectName +
        "&teammemberId=" +
        teammember.id
    );
    console.log(response.data);
    return response.data;
  };

  const searchProjectbyName = (e) => {
    getProjectsByName();
    setProjectName("");
    e.preventDefault();
  };

  const updateProjectStatus = (project) => {
    navigate("/teammember/project/status/update", { state: project });
  };

  return (
    <div className="mt-3">
      <div
        className="card form-card ms-2 me-2 mb-5 custom-bg border-color "
        style={{
          height: "45rem",
        }}
      >
        <div className="card-header custom-bg-text text-center bg-color">
          <h2>My Projects</h2>
        </div>
        <div
          className="card-body"
          style={{
            overflowY: "auto",
          }}
        >
          <div className="row g-3">
            <div class="col-auto">
              <form class="row g-3">
                <div class="col-auto">
                  <input
                    type="text"
                    class="form-control"
                    id="inputPassword2"
                    placeholder="Enter Project Name..."
                    onChange={(e) => setProjectName(e.target.value)}
                    value={projectName}
                  />
                </div>
                <div class="col-auto">
                  <button
                    type="submit"
                    class="btn bg-color custom-bg-text mb-3"
                    onClick={searchProjectbyName}
                  >
                    Search
                  </button>
                </div>
              </form>
            </div>
          </div>
          <div className="table-responsive">
            <table className="table table-hover text-color text-center">
              <thead className="table-bordered border-color bg-color custom-bg-text">
                <tr>
                  <th scope="col">Project Name</th>
                  <th scope="col">Project Description</th>
                  <th scope="col">Project Requirement</th>
                  <th scope="col">Team Lead Assign Status</th>
                  <th scope="col">Team Lead Name</th>
                  <th scope="col">Team Member Assign Status</th>
                  <th scope="col">Team Member Name</th>
                  <th scope="col">Project Created Date</th>
                  <th scope="col">Project Assign Date</th>
                  <th scope="col">Project Deadline</th>
                  <th scope="col">Project Status</th>
                  <th scope="col">Action</th>
                </tr>
              </thead>
              <tbody>
                {allProjects.map((project) => {
                  return (
                    <tr>
                      <td>
                        <b>{project.name}</b>
                      </td>

                      <td>
                        <b>{project.description}</b>
                      </td>
                      <td>
                        <b>{project.requirement}</b>
                      </td>
                      <td>
                        <b>{project.assignedToTeamlead}</b>
                      </td>
                      <td>
                        <b>{project.teamleadName}</b>
                      </td>
                      <td>
                        <b>{project.assignedToTeammember}</b>
                      </td>
                      <td>
                        <b>{project.teammemberName}</b>
                      </td>
                      <td>
                        <b>{project.createdDate}</b>
                      </td>
                      <td>
                        <b>{project.assignedDate}</b>
                      </td>
                      <td>
                        <b>{project.deadlineDate}</b>
                      </td>
                      <td>
                        <b>{project.projectStatus}</b>
                      </td>
                      <td>
                        {(() => {
                          if (project.projectStatus !== "Completed") {
                            return (
                              <button
                                onClick={() => updateProjectStatus(project)}
                                className="btn btn-sm bg-color custom-bg-text"
                              >
                                <b>Update Project Status</b>
                              </button>
                            );
                          }
                        })()}
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ViewAllTeammemberProjects;
