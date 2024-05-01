import { useState, useEffect } from "react";
import axios from "axios";
import React from "react";
import { ToastContainer, toast } from "react-toastify";

const ViewAllTeamleaders = () => {
  const [allTeamleaders, setAllTeamleaders] = useState([]);

  useEffect(() => {
    const getAllTeamlead = async () => {
      const allTeamlead = await retrieveAllTeamleaders();
      if (allTeamlead) {
        setAllTeamleaders(allTeamlead.users);
      }
    };

    getAllTeamlead();
  }, []);

  const retrieveAllTeamleaders = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/user/teamlead/all"
    );
    console.log(response.data);
    return response.data;
  };

  const deleteTeamlead = (userId) => {
    fetch("http://localhost:8080/api/user/delete?userId=" + userId, {
      method: "DELETE",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    })
      .then((result) => {
        result.json().then((res) => {
          if (res.success) {
            console.log("Got the success response");

            toast.success(res.responseMessage, {
              position: "top-center",
              autoClose: 1000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });
          } else {
            console.log("Failed to delete the teammember");
            toast.error("It seems server is down", {
              position: "top-center",
              autoClose: 1000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });
          }
        });
      })
      .catch((error) => {
        console.error(error);
        toast.error("It seems server is down", {
          position: "top-center",
          autoClose: 1000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
      });

    setTimeout(() => {
      window.location.reload(true);
    }, 2000); // Reload after 3 seconds 3000
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
          <h2>All Team Leaders</h2>
        </div>
        <div
          className="card-body"
          style={{
            overflowY: "auto",
          }}
        >
          <div className="table-responsive">
            <table className="table table-hover text-color text-center">
              <thead className="table-bordered border-color bg-color custom-bg-text">
                <tr>
                  <th scope="col">First Name</th>
                  <th scope="col">Last Name</th>
                  <th scope="col">Email Id</th>
                  <th scope="col">Phone No</th>
                  <th scope="col">Address</th>
                  <th scope="col">Action</th>
                </tr>
              </thead>
              <tbody>
                {allTeamleaders.map((teamlead) => {
                  return (
                    <tr>
                      <td>
                        <b>{teamlead.firstName}</b>
                      </td>

                      <td>
                        <b>{teamlead.lastName}</b>
                      </td>
                      <td>
                        <b>{teamlead.emailId}</b>
                      </td>
                      <td>
                        <b>{teamlead.contact}</b>
                      </td>

                      <td>
                        <b>
                          {teamlead.street +
                            " " +
                            teamlead.city +
                            " " +
                            teamlead.pincode}
                        </b>
                      </td>
                      <td>
                        <button
                          onClick={() => deleteTeamlead(teamlead.id)}
                          className="btn btn-sm bg-color custom-bg-text"
                        >
                          Remove
                        </button>
                        <ToastContainer />
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

export default ViewAllTeamleaders;
