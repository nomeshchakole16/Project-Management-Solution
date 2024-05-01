import { useState, useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import { useNavigate, useLocation } from "react-router-dom";
import axios from "axios";

const ChangePassword = () => {
  const [user, setUser] = useState({});
  const [sessionUserName, setSessionUserName] = useState("");
  const [sessionJWTName, setSessionJWTName] = useState("");

  const projectmanager = JSON.parse(sessionStorage.getItem("active-projectmanager"));
  const teamlead = JSON.parse(sessionStorage.getItem("active-teamlead"));
  const teammember = JSON.parse(sessionStorage.getItem("active-teammember"));

  const [loginRequest, setLoginRequest] = useState({
    userId: "",
    password: "",
  });

  useEffect(() => {
    if (projectmanager !== null) {
      loginRequest.userId = projectmanager.id;
      setSessionJWTName("projectmanager-jwtToken");
      setSessionUserName("active-projectmanager");
      setUser(projectmanager);
    } else if (teamlead !== null) {
      loginRequest.userId = teamlead.id;
      setSessionJWTName("teamlead-jwtToken");
      setSessionUserName("active-teamlead");
      setUser(teamlead);
    } else if (teammember !== null) {
      loginRequest.userId = teammember.id;
      setSessionJWTName("teammember-jwtToken");
      setSessionUserName("active-teammember");
      setUser(teammember);
    }
  }, []);

  const navigate = useNavigate();

  const handleUserInput = (e) => {
    setLoginRequest({
      ...loginRequest,
      [e.target.name]: e.target.value,
    });
  };

  const userChangePassword = (e) => {
    e.preventDefault();

    fetch("http://localhost:8080/api/user/changePassword", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(loginRequest),
    }).then((result) => {
      console.log("result", result);
      result.json().then((res) => {
        console.log(res);

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

          setTimeout(() => {
            sessionStorage.removeItem(sessionJWTName);
            sessionStorage.removeItem(sessionUserName);
            navigate("/user/login");
            window.location.reload(true);
          }, 3000); // Redirect after 3 seconds
        } else {
          console.log("Didn't got success response");
          toast.error("It seems server is down", {
            position: "top-center",
            autoClose: 1000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
          setTimeout(() => {
            window.location.reload(true);
          }, 1000); // Redirect after 3 seconds
        }
      });
    });
  };

  return (
    <div>
      <div className="mt-2 d-flex aligns-items-center justify-content-center">
        <div
          className="card form-card border-color custom-bg"
          style={{ width: "25rem" }}
        >
          <div className="card-header bg-color text-center custom-bg-text">
            <h5 className="card-title">Change Password</h5>
          </div>
          <div className="card-body text-color">
            <div className="mb-3 mt-1">
              <label htmlFor="quantity" className="form-label">
                <b>User Email Id</b>
              </label>
              <input
                type="text"
                className="form-control"
                value={user.emailId}
                required
                readOnly
              />
            </div>

            <div className="mb-3 mt-1">
              <label htmlFor="quantity" className="form-label">
                <b>User Contact No</b>
              </label>
              <input
                type="text"
                className="form-control"
                value={user.contact}
                required
                readOnly
              />
            </div>

            <form>
              <div className="mb-3 text-color">
                <label for="password" className="form-label">
                  <b>Password</b>
                </label>
                <input
                  type="password"
                  className="form-control"
                  id="password"
                  name="password"
                  onChange={handleUserInput}
                  value={loginRequest.password}
                />
              </div>

              <div class="col">
                <button
                  type="submit"
                  class="btn bg-color btn-sm custom-bg-text mt-4"
                  onClick={userChangePassword}
                >
                  Change Password
                </button>
                <ToastContainer />
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ChangePassword;
