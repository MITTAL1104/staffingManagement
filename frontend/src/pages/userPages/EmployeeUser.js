import React, { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  FaBriefcase,
  FaCalendarAlt,
  FaEnvelope,
  FaIdCard,
  FaTasks,
  FaUser,
} from "react-icons/fa";
import { fetchById, fetchUserDetails } from "../../api/api";
import "../../styles/userDetails.css";

const EmployeeUser = () => {
  const [employee, setEmployee] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const isMounted = useRef(false);

  const fetchEmployeeData = async () => {
    try {
      const userDetails = await fetchUserDetails(); // { employeeId, email, isAdmin }
      if (!userDetails?.data) {
        navigate("/"); // Not authenticated or missing employeeId
        return;
      }

      const empData = await fetchById("employee", userDetails.data.employeeId);
      setEmployee(empData);
    } catch (error) {
      console.error("Error fetching employee data:", error);
      navigate("/"); // Optional fallback
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!isMounted.current) {
      isMounted.current = true;
      fetchEmployeeData();
    }
  }, []);

  return (
    <div className="user-details-container">
      <h2>Personal Details</h2>

      {loading ? (
        <p className="loading-text">Loading your details...</p>
      ) : (
        employee && (
          <div className="details-card">
            <div className="details-row">
              <div>
                <FaIdCard className="icon" />
                <span className="label">Employee ID:</span>
              </div>
              <span>{employee.employeeId}</span>
            </div>
            <div className="details-row">
              <div>
                <FaUser className="icon" />
                <span className="label">Name:</span>
              </div>
              <span>{employee.name}</span>
            </div>
            <div className="details-row">
              <div>
                <FaEnvelope className="icon" />
                <span className="label">Email:</span>
              </div>
              <span>{employee.email}</span>
            </div>
            <div className="details-row">
              <div>
                <FaCalendarAlt className="icon" />
                <span className="label">Date of Joining:</span>
              </div>
              <span>{employee.dateOfJoining}</span>
            </div>
            <div className="details-row">
              <div>
                <FaBriefcase className="icon" />
                <span className="label">Role:</span>
              </div>
              <span>{employee.roleName}</span>
            </div>
            <div
              className="details-row"
              onClick={() => navigate("/userAllocation")}
              style={{ cursor: "pointer" }}
            >
              <div>
                <FaTasks className="icon" />
                <span className="label">Allocation:</span>
              </div>
              <span className="allocation-link">View Allocation</span>
            </div>
          </div>
        )
      )}
    </div>
  );
};

export default EmployeeUser;
