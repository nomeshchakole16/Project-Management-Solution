import logo from "./logo.svg";
import "./App.css";
import { Route } from "react-router-dom";
import Header from "./NavbarComponent/Header";
import { Routes } from "react-router-dom";
import HomePage from "./page/HomePage";
import ContactUs from "./page/ContactUs";
import UserRegister from "./UserComponent/UserRegister";
import AboutUs from "./page/AboutUs";
import UserLoginForm from "./UserComponent/UserLoginForm";
import ViewAllTeamleaders from "./UserComponent/ViewAllTeamleaders";
import ViewAllTeammembers from "./UserComponent/ViewAllTeammembers";
import AddProject from "./ProjectComponent/AddProject";
import ViewAllProjects from "./ProjectComponent/ViewAllProjects";
import AssignProjectToTeamlead from "./ProjectComponent/AssignProjectToTeamlead";
import ViewAllTeamleadProjects from "./ProjectComponent/ViewAllTeamleadProjects";
import AssignProjectToTeammember from "./ProjectComponent/AssignProjectToTeammember";
import ViewAllTeammemberProjects from "./ProjectComponent/ViewAllTeammemberProjects";
import UpdateProjectStatus from "./ProjectComponent/UpdateProjectStatus";
import ChangePassword from "./UserComponent/ChangePassword";

function App() {
  return (
    <div>
      <Header />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="contact" element={<ContactUs />} />
        <Route path="about" element={<AboutUs />} />
        <Route path="user/projectmanager/register" element={<UserRegister />} />
        <Route path="user/teammember/register" element={<UserRegister />} />
        <Route path="user/teamlead/register" element={<UserRegister />} />
        <Route path="user/projectmanager/teamlead/all" element={<ViewAllTeamleaders />} />
        <Route path="user/teammember/all" element={<ViewAllTeammembers />} />
        <Route path="/user/login" element={<UserLoginForm />} />
        <Route path="/user/projectmanager/project/add" element={<AddProject />} />
        <Route path="/user/projectmanager/project/all" element={<ViewAllProjects />} />
        <Route
          path="/user/teamlead/project/all"
          element={<ViewAllTeamleadProjects />}
        />
        <Route
          path="/user/teammember/project/all"
          element={<ViewAllTeammemberProjects />}
        />
        <Route
          path="/project/assign/teamlead"
          element={<AssignProjectToTeamlead />}
        />
        <Route
          path="/project/assign/teammember"
          element={<AssignProjectToTeammember />}
        />
        <Route
          path="/teammember/project/status/update"
          element={<UpdateProjectStatus />}
        />
        <Route path="/user/change/password" element={<ChangePassword />} />
      </Routes>
    </div>
  );
}

export default App;
