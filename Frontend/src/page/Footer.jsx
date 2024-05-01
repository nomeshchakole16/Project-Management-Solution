import { Link } from "react-router-dom";

const Footer = () => {
  return (
    <div>
      <div className="container my-5">
        <footer className="text-center text-lg-start text-color">
          <div className="container-fluid p-4 pb-0">
            <section className="">
              <div className="row">
                <div className="col-lg-12 mb-4">
                  <h5 className="text-uppercase text-color text-center">
                    Project Management Solution
                  </h5>

                  <p>
                    A Project management solution is a powerful tool designed to help
                    individuals and teams efficiently organize, track, and
                    accomplish their tasks and projects.
                    Additionally, the system's data analytics provide valuable insights 
                    into productivity trends, enabling users to identify areas for improvement 
                    and optimize their workflows. Whether for personal use or within organizations, 
                    a task management system enhances productivity, reduces stress, and empowers 
                    individuals and teams to achieve their goals efficiently.
                  </p>
                </div>
              </div>
            </section>

            <hr className="mb-4" />

            <section className="">
              <p className="d-flex justify-content-center align-items-center">
                <span className="me-3 text-color">Login from here</span>
                <Link to="/user/login" className="active">
                  <button
                    type="button"
                    className="btn btn-outline-light btn-rounded bg-color custom-bg-text"
                  >
                    Log in
                  </button>
                </Link>
              </p>
            </section>

            <hr className="mb-4" />
          </div>

          <div className="text-center">
            © 2024 CDAC DAC TEAM Copyright:
            <a className="text-color-3" href="https://www.cdac.in/">
              cdac.in
            </a>
          </div>
        </footer>
      </div>
    </div>
  );
};

export default Footer;
