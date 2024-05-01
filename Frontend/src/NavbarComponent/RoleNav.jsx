import ProjectmanagerHeader from "./ProjectmanagerHeader";
import TeammemberHeader from "./TeammemberHeader";
import TeamleadHeader from "./TeamleadHeader";
import NormalHeader from "./NormalHeader";

const RoleNav = () => {
  const teammember = JSON.parse(sessionStorage.getItem("active-teammember"));
  const projectmanager = JSON.parse(sessionStorage.getItem("active-projectmanager"));
  const teamlead = JSON.parse(sessionStorage.getItem("active-teamlead"));

  if (projectmanager != null) {
    return <ProjectmanagerHeader />;
  } else if (teamlead != null) {
    return <TeamleadHeader />;
  } else if (teammember != null) {
    return <TeammemberHeader />;
  } else {
    return <NormalHeader />;
  }
};

export default RoleNav;
