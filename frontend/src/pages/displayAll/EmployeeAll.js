import React, { useState, useEffect } from "react";
import EmployeeTable from "../../components/EmployeeTable";
import { fetchAllData } from "../../api/api"; // Import the function
import "../../styles/displayall.css";

const EmployeeAll = () => {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true); // State for loading

  useEffect(() => {
    const loadEmployees = async () => {
      try {
        const response = await fetchAllData("employee");
        setEmployees(response);
      } catch (error) {
        console.error("Error fetching employees:", error);
      } finally {
        setLoading(false); // Hide loading message once data is fetched
      }
    };
    loadEmployees();
  }, []);

  return (
    <div className="all-page">
      <h2>Employee Details</h2>
      {loading ? (
        <p className="loading-message">
          Loading Data...
        </p>
      ) : (
        <EmployeeTable data={employees} />
      )}
    </div>
  );
};

export default EmployeeAll;
