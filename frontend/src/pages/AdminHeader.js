import React, { useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import "../styles/adminheader.css";
import Swal from "sweetalert2";
import { FaProjectDiagram, FaTasks, FaUser } from "react-icons/fa";
import { fetchEmpNameByEmail, fetchUserDetails, userLogout } from "../api/api";

const AdminHeader = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const isEmployeePage = location.pathname.startsWith("/emp");
  const isProjectPage = location.pathname.startsWith("/project");
  const isAllocationPage = location.pathname.startsWith("/allocation");



 const [email,setEmail] = useState("");

  const getUserDetails = async() => {
    const response = await fetchUserDetails();

    if(response.data){
      setEmail(response.data.email)
    }
  }

  const [userName,setUserName] = useState("");

  const fetchUserName = async () => {
    try {
      const name = await fetchEmpNameByEmail("employee", email);
      setUserName(name);
    } catch (err) {
      console.error("Failed to fetch user name", err);
    }
  };
  
  useEffect(() => {

    getUserDetails();
    
    if (email) {
      fetchUserName();
    }
  });
  

  const handleLogout = () => {
    Swal.fire({
      title: "Are you sure?",
      text: "You will be logged out!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, log me out!",
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          await userLogout();
        } catch (err) {
          console.error("Logout API failed", err);
        } finally {
          navigate("/");
        }
      }
    });
  };
  return (
    <header className="admin-header">
      {/* Left: Logo & Title */}
      <div
        className="admin-logo-container"
        onClick={() => navigate("/allocationAll")}
      >
        <img
          src="/assets/staff.jpg"
          alt="Logo"
          className="admin-logo"
          style={{ cursor: "pointer" }}
        />
        <Link
          to="/allocationAll"
          className="admin-dropdown-button"
        >
          Admin Dashboard
        </Link>
      </div>

      {/* Center: Admin Dashboard + Navigation */}
      <div className="admin-center">
        <div className="admin-center-nav">
          {/* Allocations Dropdown */}
          <div className="admin-dropdown">
            <Link
              to="/allocationAll"
              className="admin-dropdown-button"
              style={isAllocationPage ? { color: "#38bdf8" } : {}}
            >
              <FaTasks /> Allocations
            </Link>
            <div className="admin-dropdown-content">
              <button onClick={() => navigate("/allocation/create")} >
                Create New Allocation
              </button>
              <button onClick={() => navigate("/allocation/read")}>
                View Allocation Details
              </button>
              <button onClick={() => navigate("/allocation/update")}>
                Edit Allocation Details
              </button>
              <button onClick={() => navigate("/allocation/delete")}>
                Delete Allocation
              </button>
            </div>
          </div>

          {/* Projects Dropdown */}
          <div className="admin-dropdown">
            <Link
              to="/projectAll"
              className="admin-dropdown-button"
              style={isProjectPage ? { color: "#38bdf8" } : {}}
            >
              <FaProjectDiagram /> Projects
            </Link>
            <div className="admin-dropdown-content">
              <button onClick={() => navigate("/project/create")}>
                Create New Project
              </button>
              <button onClick={() => navigate("/project/read")}>
                View Project Details
              </button>
              <button onClick={() => navigate("/project/update")}>
                Edit Project Details
              </button>
              <button onClick={() => navigate("/project/delete")}>
                Delete Project
              </button>
            </div>
          </div>

          {/* Employees Dropdown */}
          <div className="admin-dropdown">
            <Link
              to="/employeeAll"
              className="admin-dropdown-button"
              style={isEmployeePage ? { color: "#38bdf8" } : {}}
            >
              <FaUser /> Employees
            </Link>
            <div className="admin-dropdown-content">
              <button onClick={() => navigate("/employee/create")}>
                Create New Employee
              </button>
              <button onClick={() => navigate("/employee/read")}>
                View Employee Details
              </button>
              <button onClick={() => navigate("/employee/update")}>
                Edit Employee Details
              </button>
              <button onClick={() => navigate("/employee/delete")}>
                Delete Employee
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Right: Icons */}
      <div className="admin-right-section">
        <div
          className="admin-user-icon">
          <FaUser /> <span>Hi, {userName} </span>
          <div className="admin-user-menu">
            <button onClick={() => navigate("/adminDet")}>
              Personal Details
            </button>
            <button onClick={() => navigate("/updatePassword")}>
              Change Password
            </button>
            <button onClick={handleLogout} style={{color:"red"}}>Logout</button>
          </div>
        </div>
      </div>
    </header>
  );
};

export default AdminHeader;
