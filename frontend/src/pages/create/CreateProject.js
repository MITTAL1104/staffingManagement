import { useEffect, useState } from "react";
import { createData,getAllEmpNames } from "../../api/api";
import "../../styles/create.css";
import { showMessage } from "../../utils/helpers";

const CreateProject = () => {
  const [employees,setEmployees] = useState([]);
  const [formData, setFormData] = useState({
    projectName: "",
    description: "",
    projectOwnerName: "",
    startDate: "",
    endDate: "",
    isActive: true,
  });

  useEffect(() => {
    const loadEmployees = async () => {
      try {
        const data = await getAllEmpNames();
        setEmployees(data);
      } catch (err) {
        console.error("Error fetching employees:", err);
      }
    };
  
    loadEmployees();
  }, []); //for project owner name dropdown

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    setFormData((prevData) => {
      let newValue = type === "checkbox" ? checked : value;

      if (name === "endDate" && prevData.startDate) {
        if (newValue < prevData.startDate) {
          showMessage("End Date cannot be before Start Date!", true);
          return prevData;
        }
      }

      if (
        name === "startDate" &&
        prevData.endDate &&
        prevData.endDate < value
      ) {
        return { ...prevData, [name]: value, endDate: "" };
      }

      return { ...prevData, [name]: newValue };
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await createData("project", formData);

      if (response) {
        showMessage("Project created successfully!");
        setFormData({
          projectName: "",
          description: "",
          projectOwnerName: "",
          startDate: "",
          endDate: "",
          isActive: true,
        });
      } else {
        showMessage("Error: Failed to create project", true);
      }
    } catch (error) {
      if (error.code === "ERR_BAD_REQUEST") {
        showMessage(`${error.response.data}`, true);
      } else {
        showMessage("Network error. Please try again.", true);
      }
    }
  };


  return (
    <div className="create-container">
      <h2>Create Project</h2>
      <form onSubmit={handleSubmit}>
        <label>Project Name</label>
        <input
          type="text"
          name="projectName"
          placeholder="Enter Project Name"
          value={formData.projectName}
          onChange={handleChange}
          required
        />
        <label>Project Description</label>
        <input
          type="text"
          name="description"
          placeholder="Enter Project Description"
          value={formData.description}
          onChange={handleChange}
          required
        />
        <label>Owner Name</label>
        <select
          name="projectOwnerName"
          value={formData.projectOwnerName}
          onChange={handleChange}
          required
        >
          <option value="">Select Owner</option>
          {employees.map((employee) => (
            <option key={employee.employeeId} value={employee.name}>
              {employee.name}
            </option>
          ))}
        </select>
        <label>Project Start Date</label>
        <input
          type="date"
          name="startDate"
          placeholder="Enter Project Start Date"
          value={formData.startDate}
          onChange={handleChange}
          required
        />
        <label>Project End Date</label>
        <input
          type="date"
          name="endDate"
          placeholder="Enter Project End Date"
          value={formData.endDate}
          required
          disabled={!formData.startDate}
          min={formData.startDate || ""}
          onChange={handleChange}
        />
        <div className="checkbox-container-create">
          <div className="checkbox-item-create">
            <input
              type="checkbox"
              name="isActive"
              checked={formData.isActive}
              onChange={handleChange}
            />
            <label>Active</label>
          </div>
        </div>
        <button type="submit">Create Project</button>
      </form>
    </div>
  );
};

export default CreateProject;
