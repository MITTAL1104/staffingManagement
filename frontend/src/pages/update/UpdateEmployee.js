import React, { useEffect, useState } from "react";
import {
  fetchAllNames,
  fetchAllIds,
  fetchAllRoles,
  updateDataById,
  fetchById,
} from "../../api/api";
import "../../styles/update2.css";
import { showMessage } from "../../utils/helpers";

const UpdateEmployee = () => {
  const [inputValue, setInputValue] = useState("");
  const [formData, setFormData] = useState(null);
  const [isFetching, setIsFetching] = useState(false);
  const [selectedEmployeeID,setSelectedEmployeeID] = useState(null);
  const [allEmployees, setAllEmployees] = useState([]); // [{ name, id }]
  const [dropdownVisible, setDropdownVisible] = useState(false);
  const [rolesList,setRolesList] = useState([]);

  //for checking if invalid role name is enterd in updation
  useEffect(() => {
    const loadRoles = async() => {
      try{
        const roles = await fetchAllRoles();
        setRolesList(roles);
      }catch(err){
        showMessage("Failed to laod role names")
      }
    };
    loadRoles();
  },[])


  const handleInputChange = async (e) => {
    const value = e.target.value;
    setInputValue(value);
    setSelectedEmployeeID(null);
  
    if (!value.trim()) {
      setAllEmployees([]);
      setDropdownVisible(false);
      return;
    }

    //dropdown fucntionality
    if (/^[a-zA-Z\s]*$/.test(value)) {
      try {
        const [names, ids] = await Promise.all([
          fetchAllNames("employee", value),
          fetchAllIds("employee", value),
        ]);
  
        if (names.length === 0) {
          setDropdownVisible(false);
          setAllEmployees([]);
          showMessage("No matching names found",true)
          
        } else {
          const combined = names.map((name, index) => ({
            name,
            id: ids[index] ?? "N/A",
          }));
          setAllEmployees(combined);
          setDropdownVisible(true);
        }
  

      } catch (error) {
        showMessage("Failed to fetch employee list", true);
      }
    }
  };
  
  const handleDropdownSelect = (emp) => {  
    setInputValue(emp.name); 
    setSelectedEmployeeID(emp.id);
    setDropdownVisible(false);
  };
  
  const fetchEmployeeData = async () => {
    if (!selectedEmployeeID) {
      showMessage("Please select an employee name from dropdown!", true);
      return;
    }

    setIsFetching(true);
    try {
      const response = await fetchById("employee", selectedEmployeeID);
      if (response) {
        setFormData(response);
        showMessage("Employee data loaded successfully");
        setDropdownVisible(false);
        setInputValue(response.name); 
      } else {
        showMessage(`Employee '${inputValue}' not found!`, true);
        setFormData(null);
      }
    } catch (error) {
      showMessage("Network error. Please try again!", true);
    }
    setIsFetching(false);
  };

  const handleInputChangeForm = (e) => {
    const { name, value, type, checked } = e.target;

    setFormData((prevData) => ({
      ...prevData,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData?.employeeId) {
      showMessage("Invalid employee ID!", true);
      return;
    }

    const matchedRole = rolesList.find(
      (role) => role.roleName === formData.roleName
    );

    if(!matchedRole){
      showMessage("Invalid Role Name",true);
      return;
    }

    const updatedPayLoad = {
      ...formData,
      roleId:matchedRole.roleId,
    }
    
    //update logic
    try {
      const response = await  updateDataById("employee", formData.employeeId, updatedPayLoad);
      if (response) {
        showMessage("Employee updated successfully");
        setFormData(null);
        setInputValue("");
      } else {
        showMessage("Failed to update employee", true);
      }
    } catch (error) {
        if(error.response.status===500){
            showMessage("Internal Server Error",true);
        }else{
            const msg = error?.response?.data || "Network error. Please try again.";
            showMessage(msg, true);
        }
    }
  };

  const isFormValid =
    formData?.name &&
    formData?.email &&
    formData?.dateOfJoining &&
    formData?.roleName;

  return (
    <div className="update-container">
      <h2>Edit Employee Details</h2>

      <div className="fetch-section">
        <div className="dropdown-container">
          <input
            type="text"
            placeholder="Enter Employee Name"
            value={inputValue}
            onChange={handleInputChange}
          />
          {dropdownVisible && (
            <div className="dropdown">
              {allEmployees.map((emp, index) => (
                <div
                  key={index}
                  className="dropdown-item"
                  onClick={() => handleDropdownSelect(emp)}
                >
                  {emp.name} (Employee ID:{emp.id})
                </div>
              ))}
            </div>
          )}
        </div>

        <button
          type="button"
          onClick={fetchEmployeeData}
          disabled={isFetching || !inputValue.trim()}
          className={`load-btn ${
            isFetching || !inputValue.trim() ? "disabled-btn" : ""
          }`}
        >
          {isFetching ? "Fetching..." : "Preview"}
        </button>
      </div>

      {formData && (
        <form onSubmit={handleSubmit}>
          <label htmlFor="employeeId">ID</label>
          <input
            type="number"
            name="employeeId"
            value={formData.employeeId}
            readOnly
          />
          <label htmlFor="name">Employee Name</label>
          <input type="text" name="name" value={formData.name} onChange={handleInputChangeForm}/>
          <label htmlFor="email">Email</label>
          <input type="email" name="email" value={formData.email} readOnly />
          <label htmlFor="dateOfJoining">Date Of Joining</label>
          <input
            type="date"
            name="dateOfJoining"
            value={formData.dateOfJoining}
            onChange={handleInputChangeForm}
          />
          <label htmlFor="roleName">Role Name</label>
          <input
            type="text"
            name="roleName"
            value={formData.roleName}
            onChange={handleInputChangeForm}
          />
          <div className="checkbox-container">
            <input
              type="checkbox"
              name="isActive"
              checked={formData.isActive}
              onChange={handleInputChangeForm}
            />
            <label>Active</label>
            <input
              type="checkbox"
              name="isAdmin"
              checked={formData.isAdmin}
              onChange={handleInputChangeForm}
            />
            <label>Admin</label>
          </div>
          <button type="submit" disabled={!isFormValid}>
            Update Employee
          </button>
        </form>
      )}
    </div>
  );
};

export default UpdateEmployee;
