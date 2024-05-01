import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const ProjectmanagerHeader = () => {
  let navigate = useNavigate();

  const user = JSON.parse(sessionStorage.getItem("active-projectmanager"));
  console.log(user);

  const projectmanagerLogout = () => {
    toast.success("logged out!!!", {
      position: "top-center",
      autoClose: 1000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    });
    sessionStorage.removeItem("active-projectmanager");
    sessionStorage.removeItem("projectmanager-jwtToken");
    
    window.location.reload(true);
    navigate("/home");
  };

  return (
    <ul class="navbar-nav ms-auto mb-2 mb-lg-0 me-5">
      <li className="nav-item">
        <Link
          to="/user/teamlead/register"
          className="nav-link active"
          aria-current="page"
        >
          <b className="text-color">Register Team Lead</b>
        </Link>
      </li>

      <li class="nav-item">
        <Link
          to="/user/projectmanager/project/add"
          class="nav-link active"
          aria-current="page"
        >
          <b className="text-color">Add Project</b>
        </Link>
      </li>

      <li class="nav-item">
        <Link
          to="/user/projectmanager/project/all"
          class="nav-link active"
          aria-current="page"
        >
          <b className="text-color">All Projects</b>
        </Link>
      </li>

      <li class="nav-item">
        <Link
          to="/user/projectmanager/teamLead/all"
          class="nav-link active"
          aria-current="page"
        >
          <b className="text-color">View Team Leaders</b>
        </Link>
      </li>

      <li class="nav-item">
        <Link
          to="/user/teammember/all"
          class="nav-link active"
          aria-current="page"
        >
          <b className="text-color">View All Team Members</b>
        </Link>
      </li>

      <li className="nav-item">
        <Link
          to="/user/change/password"
          className="nav-link active"
          aria-current="page"
        >
          <b className="text-color">Change Password</b>
        </Link>
      </li>

      <li class="nav-item">
        <Link
          to=""
          class="nav-link active"
          aria-current="page"
          onClick={projectmanagerLogout}
        >
          <b className="text-color">Logout</b>
        </Link>
        <ToastContainer />
      </li>
    </ul>
  );
};

export default ProjectmanagerHeader;
