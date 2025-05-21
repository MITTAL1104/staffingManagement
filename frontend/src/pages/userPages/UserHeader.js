import React, { useState, useRef, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../../styles/userHeader2.css";
import Swal from "sweetalert2";
import {FaUser } from "react-icons/fa";
import { fetchEmpNameByEmail, fetchUserDetails, userLogout } from "../../api/api";

const Header = () => {
  const navigate = useNavigate();
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [username,setUsername] = useState("");
  const dropdownRef = useRef(null);

  useEffect(() => {
    const getUserDetails = async() => {
      try{
        const userDetails = await fetchUserDetails();

        if(userDetails){
          const name = await fetchEmpNameByEmail("employee",userDetails.data.email);
          setUsername(name);
        } else if(!userDetails){
          navigate("/");
        }
      } catch(err){
        console.error("Failed to load user info",err);
      }
    };

    getUserDetails();
  },[navigate])
  

  const handleLogout = () => {
    Swal.fire({
      title: "Are you sure?",
      text: "You will be logged out!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, log me out!",
    }).then(async(result) => {
      if (result.isConfirmed) {
        try{
          await userLogout();
        } catch(err){
          console.error("Logout API failed",err);
        } finally{
          navigate("/");
        }
      }
    });
  };

  // Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setDropdownOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <header>
      <div className="left-section"
        onClick={()=>navigate("/userAllocation")}>
        <img
          src="/assets/staff.jpg"
          alt="Logo"
          className="logo"
          style={{ cursor: "pointer" }}
        />
              <Link
          to="/userAllocation"
          className="admin-dropdown-button"
        >
          User Dashboard
        </Link>
      </div>


      <div className="right-section">
        <div className="admin-user-icon">
          <FaUser /> <span>Hi, {username} </span>
          <div className="admin-user-menu">
            <button onClick={() => navigate("/userEmployee")}>Personal Details</button>
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

export default Header;
