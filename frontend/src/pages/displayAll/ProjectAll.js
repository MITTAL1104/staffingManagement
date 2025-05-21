import React, { useState, useEffect } from "react";
import ProjectTable from "../../components/ProjectTable";
import { fetchAllData } from "../../api/api"; // Import the function
import "../../styles/displayall.css";

const ProjectAll = () => {
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true); // State for loading

  useEffect(() => {
    const loadProjects = async () => {
      try {
        const response = await fetchAllData("project");
        setProjects(response);
      } catch (error) {
        console.error("Error fetching projects:", error);
      } finally {
        setLoading(false); // Hide loading message once data is fetched
      }
    };
    loadProjects();
  }, []);

  return (
    <div className="all-page">
      <h2>Project Details</h2>
      {loading ? (
        <p className="loading-message">Loading Data...</p>
      ) : (
        <ProjectTable data={projects} />
      )}
    </div>
  );
};

export default ProjectAll;
