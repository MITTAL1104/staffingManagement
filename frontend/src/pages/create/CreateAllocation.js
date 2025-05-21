import { useEffect, useState } from "react";
import {
  createData,
  fetchByName,
  fetchEmpNameByEmail,
  fetchUserDetails,
} from "../../api/api";
import "../../styles/create.css";
import { showMessage } from "../../utils/helpers";

const CreateAllocation = () => {
  const [formData, setFormData] = useState({
    assigneeName: "",
    projectName: "",
    allocationStartDate: "",
    allocationEndDate: "",
    allocatorName: "",
    percentageAllocation: 100,
    isActive: true,
    assigneeId: null,
    projectId: null,
  });

  const [allocatorName, setAllocatorName] = useState("");
  const [employeeMatches, setEmployeeMatches] = useState([]);
  const [projectMatches, setProjectMatches] = useState([]);

  useEffect(() => {
    const fetchAllocatorName = async () => {
      try {
        const userResponse = await fetchUserDetails();
        if (userResponse?.data?.email) {
          const email = userResponse.data.email;
          const name = await fetchEmpNameByEmail("employee", email);
          if (name) {
            setAllocatorName(name);
            setFormData((prevData) => ({
              ...prevData,
              allocatorName: name,
            }));
          }
        }
      } catch (error) {
        showMessage("Failed to fetch allocator name", true);
      }
    };
    fetchAllocatorName();
  }, []);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    if (name === "assigneeName") {
      setFormData((prevData) => ({
        ...prevData,
        assigneeName: value,
        assigneeId: null,
      }));

      if (value.trim() !== "") {
        fetchByName("employee", value).then((res) => {
          if (res.length === 1) {
            setFormData((prev) => ({
              ...prev,
              assigneeName: res[0].name,
              assigneeId: res[0].employeeId,
            }));
            setEmployeeMatches([]);
          } else {
            setEmployeeMatches(res);
          }
        });
      } else {
        setEmployeeMatches([]);
      }
      return;
    }

    if (name === "projectName") {
      setFormData((prevData) => ({
        ...prevData,
        projectName: value,
        projectId: null,
      }));

      if (value.trim() !== "") {
        fetchByName("project", value).then((res) => {
          if (res.length === 1) {
            setFormData((prev) => ({
              ...prev,
              projectName: res[0].projectName,
              projectId: res[0].projectId,
            }));
            setProjectMatches([]);
          } else {
            setProjectMatches(res);
          }
        });
      } else {
        setProjectMatches([]);
      }
      return;
    }

    setFormData((prevData) => {
      let newValue = type === "checkbox" ? checked : value;

      if (
        name === "allocationEndDate" &&
        prevData.allocationStartDate &&
        newValue < prevData.allocationStartDate
      ) {
        showMessage("End Date cannot be before Start Date!", true);
        return prevData;
      }

      if (
        name === "allocationStartDate" &&
        prevData.allocationEndDate &&
        prevData.allocationEndDate < value
      ) {
        return {
          ...prevData,
          [name]: value,
          allocationEndDate: "",
        };
      }

      return { ...prevData, [name]: newValue };
    });
  };

  const handleSelectedEmployee = (e) => {
    const selectedId = parseInt(e.target.value);
    const selectedEmployee = employeeMatches.find(
      (emp) => emp.employeeId === selectedId
    );

    if (selectedEmployee) {
      setFormData((prev) => ({
        ...prev,
        assigneeName: selectedEmployee.name,
        assigneeId: selectedEmployee.employeeId,
      }));
      setEmployeeMatches([]);
    }
  };

  const handleSelectedProject = (e) => {
    const selectedId = parseInt(e.target.value);
    const selectedProject = projectMatches.find(
      (proj) => proj.projectId === selectedId
    );

    if (selectedProject) {
      setFormData((prev) => ({
        ...prev,
        projectName: selectedProject.projectName,
        projectId: selectedProject.projectId,
      }));
      setProjectMatches([]);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const payload = { ...formData };
      const response = await createData("allocation", payload);

      if (response) {
        showMessage("Allocation created successfully");
        setFormData({
          assigneeName: "",
          projectName: "",
          allocationStartDate: "",
          allocationEndDate: "",
          allocatorName: allocatorName,
          percentageAllocation: 100,
          isActive: true,
          assigneeId: null,
          projectId: null,
        });
      } else {
        showMessage(
          response?.data?.message || "Failed to create allocation",
          true
        );
      }
    } catch (error) {
      if (error.response) {
        const { data } = error.response;
        showMessage(` ${data}` || "An error occurred", true);
      } else {
        showMessage("Network error. Please try again.", true);
      }
    }
  };

  return (
    <div className="create-container">
      <h2>Create Allocation</h2>
      <form onSubmit={handleSubmit}>
        
        <label>Employee Name</label>
        <input
          type="text"
          name="assigneeName"
          placeholder="Enter Employee Name"
          value={formData.assigneeName}
          onChange={handleChange}
          required
        />
        {employeeMatches.length > 1 && formData.assigneeId === null && (
          <div>
            <select onChange={handleSelectedEmployee} defaultValue="">
              <option value="" disabled>
                Select an employee
              </option>
              {employeeMatches.map((emp) => (
                <option key={emp.employeeId} value={emp.employeeId}>
                  {emp.name} (ID: {emp.employeeId}, Role: {emp.roleName})
                </option>
              ))}
            </select>
          </div>
        )}

        <label>Project Name</label>
        <input
          type="text"
          name="projectName"
          placeholder="Enter Project Name"
          value={formData.projectName}
          onChange={handleChange}
          required
        />
        {projectMatches.length > 1 && formData.projectId === null && (
          <div>
            <select onChange={handleSelectedProject} defaultValue="">
              <option value="" disabled>
                Select a project
              </option>
              {projectMatches.map((proj) => (
                <option key={proj.projectId} value={proj.projectId}>
                  {proj.projectName} (ID: {proj.projectId}, Owner:{proj.projectOwnerName})
                </option>
              ))}
            </select>
          </div>
        )}

        <label>Allocation Start Date</label>
        <input
          type="date"
          name="allocationStartDate"
          value={formData.allocationStartDate}
          onChange={handleChange}
          required
        />
        <label>Allocation End Date</label>
        <input
          type="date"
          name="allocationEndDate"
          value={formData.allocationEndDate}
          onChange={handleChange}
          required
          disabled={!formData.allocationStartDate}
          min={formData.allocationStartDate || ""}
        />
        <label>Allocator Name</label>
        <input
          type="text"
          name="allocatorName"
          value={allocatorName}
          readOnly
        />
        <label>% Allocation</label>
        <input
          type="text"
          name="percentageAllocation"
          value={formData.percentageAllocation}
          readOnly
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
        <button type="submit">Create Allocation</button>
      </form>
    </div>
  );
};

export default CreateAllocation;
