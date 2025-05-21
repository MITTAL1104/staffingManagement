import React, { useState } from "react";
import {
  fetchAllNames,
  updateDataById,
  fetchAllProjNames,
  fetchAllocIds,
  fetchById,
  fetchAllIds} from "../../api/api";
import "../../styles/update2.css";
import { showMessage } from "../../utils/helpers";

const UpdateAllocation = () => {
  const [inputValue, setInputValue] = useState("");
  const [formData, setFormData] = useState(null);
  const [isFetching, setIsFetching] = useState(false);
  const [allAllocations, setAllAllocations] = useState([]);
  const [dropdownVisible, setDropdownVisible] = useState(false);
  const [selectedAllocationId, setSelectedAllocationId] = useState(null);

  const handleInputChange = async (e) => {
    const value = e.target.value;
    setInputValue(value);
    setSelectedAllocationId(null);

    if(!value.trim()){
      setAllAllocations([]);
      setDropdownVisible(false);
      return;
    }

    if (/^[a-zA-Z\s]*$/.test(value)) {
      //dropdown functionality
      try {
        const [names, projectNames, empIds, allocIds] = await Promise.all([
          fetchAllNames("allocation", value),
          fetchAllProjNames("allocation", value),
          fetchAllIds("allocation", value),
          fetchAllocIds(value),
        ]);

        if(names.length===0){
          setDropdownVisible(false);
          setAllAllocations([]);
          showMessage("No matching names found",true);
        } else{
          const combined = names.map((name, index) => ({
            name,
            project: projectNames[index] ?? "N/A",
            empId: empIds[index] ?? "N/A",
            allocId: allocIds[index] ?? "N/A",
          }));
        setAllAllocations(combined);
        setDropdownVisible(true);
        }

      } catch (error) {
        showMessage("Failed to fetch allocation list",true);
      }
    }
  };

  const handleDropdownSelect = (alloc) => {
    setInputValue(alloc.name);
    setSelectedAllocationId(alloc.allocId);
    setDropdownVisible(false);
  };

  const fetchAllocationData = async () => {
    if (!selectedAllocationId) {
      showMessage("Please select an employee name from dropdown!", true);
      return;
    }

    setIsFetching(true);
    try {
      const response = await fetchById("allocation", selectedAllocationId);
      if (response) {
        setFormData(response);
        showMessage("Allocation data loaded successfully");
      } else {
        showMessage("Allocation not found!", true);
        setFormData(null);
      }
    } catch (error) {
      showMessage("Network error. Try again!", true);
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
    if (!selectedAllocationId) {
      showMessage("Please select an employee name!", true);
      return;
    }

    try {
      const response = await updateDataById(
        "allocation",
        selectedAllocationId,
        formData
      );
      if (response) {
        showMessage("Allocation updated successfully");
        setFormData(null);
        setInputValue("");
        setSelectedAllocationId(null);
      } else {
        showMessage("Failed to update allocation", true);
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

  return (
    <div className="update-container">
      <h2>Edit Allocation Details</h2>

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
              {allAllocations.map((alloc, index) => (
                <div
                  key={index}
                  className="dropdown-item"
                  onClick={() => handleDropdownSelect(alloc)}
                >
                  {alloc.name} (Project: {alloc.project}, Employee ID:{" "}
                  {alloc.empId})
                </div>
              ))}
            </div>
          )}
        </div>

        <button
          type="button"
          onClick={fetchAllocationData}
          disabled={isFetching || !inputValue.trim()}
          className={`load-btn ${isFetching || !inputValue.trim()? "disabled-btn" : ""}`}
        >
          {isFetching ? "Fetching..." : "Preview"}
        </button>
      </div>

      {formData && (
        <form onSubmit={handleSubmit}>
          <label>Allocation ID</label>
          <input type="text" value={formData.allocationId} readOnly />

          <label>Employee Name</label>
          <input type="text" value={formData.assigneeName} readOnly />

          <label>Project Name</label>
          <input type="text" value={formData.projectName} readOnly />

          <label>Allocation Start Date</label>
          <input
            type="date"
            name="allocationStartDate"
            value={formData.allocationStartDate}
            onChange={handleInputChangeForm}
            required
          />

          <label>Allocation End Date</label>
          <input
            type="date"
            name="allocationEndDate"
            value={formData.allocationEndDate}
            onChange={handleInputChangeForm}
            required
          />

          <label>Allocated By</label>
          <input type="text" value={formData.allocatorName} readOnly />

          <label>Percentage Allocation</label>
          <input type="number" value={formData.percentageAllocation} readOnly />

          <div className="checkbox-container">
            <input
              type="checkbox"
              name="isActive"
              checked={formData.isActive}
              onChange={handleInputChangeForm}
            />
            <label>Active</label>
          </div>

          <button type="submit">Update Allocation</button>
        </form>
      )}
    </div>
  );
};

export default UpdateAllocation;
