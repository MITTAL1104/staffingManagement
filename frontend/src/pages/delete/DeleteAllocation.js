import { useState } from "react";
import {
  deleteDataByID,
  fetchAllocForDeleteByEmpName,
  fetchAllocForDeleteByProjName,
  fetchEmpIdByName,
  fetchProjIdByName,
} from "../../api/api";
import Swal from "sweetalert2";
import AllocationTable from "../../components/AllocationTable";
import "../../styles/delete2.css";
import { showMessage } from "../../utils/helpers";

const DeleteAllocation = () => {
  const [filterType, setFilterType] = useState("byEmpName");
  const [inputEmployeeName, setInputEmployeeeName] = useState("");
  const [inputProjectName, setInputProjectName] = useState("");
  const [allocations, setAllocations] = useState([]);
  const [selectedAllocationId, setSelectedAllocationId] = useState("");

  const handleFilterChange = (value) => {
    setFilterType(value);
    setInputEmployeeeName("");
    setInputProjectName("");
    setAllocations([]);
    setSelectedAllocationId("");
  };

  const handleInputNameChange = (e) => {
    const value = e.target.value;
    filterType === "byEmpName"
      ? setInputEmployeeeName(value)
      : setInputProjectName(value);
  };

  const fetchData = async () => {
    try {
      let response = [];

      if (filterType === "byEmpName") {
        const empId = await fetchEmpIdByName(inputEmployeeName);
        if (empId <= 0) {
          showMessage("Invalid Employee Name", true);
          return;
        }
        response = await fetchAllocForDeleteByEmpName(inputEmployeeName);
      } else {
        const projId = await fetchProjIdByName(inputProjectName);
        if (projId <= 0) {
          showMessage("Invalid Project Name", true);
          return;
        }
        response = await fetchAllocForDeleteByProjName(inputProjectName);
      }

      if (!response || response.length === 0) {
        showMessage("No active allocations found", true);
        setAllocations([]);
        return;
      }

      setAllocations(response);
      setSelectedAllocationId("");
    } catch (error) {
      setAllocations([]);
      showMessage("Could not get allocations", true);
    }
  };

  const handleDelete = async () => {
    if (!selectedAllocationId) {
      showMessage("Please select a valid allocation ID to delete", true);
      return;
    }

    const confirmation = await Swal.fire({
      title: `Delete Allocation ID: ${selectedAllocationId}?`,
      text: "Are you sure you want to delete this allocation?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      cancelButtonColor: "#3085d6",
      confirmButtonText: "Yes, delete it!",
    });

    if (confirmation.isConfirmed) {
      try {
        await deleteDataByID("allocation", selectedAllocationId);
        showMessage("Allocation Deleted Successfully!");
        setAllocations([]);
        setInputEmployeeeName("");
        setInputProjectName("");
        setSelectedAllocationId("");
      } catch (err) {
          if(err.response.status===500){
            showMessage("Internal Server Error",true);
          }else{
            const msg = err?.response?.data || "Network error. Please try again.";
            showMessage(msg, true);
          }
      }
    }
  };

  return (
    <>
      <div className="del-container">
        <h2 className="del-title">Delete Existing Allocation</h2>

        <div className="del-options">
          <label>
            <input
              type="radio"
              name="Employee Name"
              value="byEmpName"
              checked={filterType === "byEmpName"}
              onChange={() => handleFilterChange("byEmpName")}
            />
            Employee Name
          </label>
          <label>
            <input
              type="radio"
              name="Project Name"
              value="byProjName"
              checked={filterType === "byProjName"}
              onChange={() => handleFilterChange("byProjName")}
            />
            Project Name
          </label>
        </div>

        <div className="del-input">
          <input
            type="text"
            placeholder={`Enter ${
              filterType === "byEmpName" ? "Employee Name" : "Project Name"
            }`}
            value={
              filterType === "byEmpName"
                ? inputEmployeeName
                : inputProjectName
            }
            onChange={handleInputNameChange}
          />

          <div>
            <button
              className={`del-button ${
                (filterType === "byEmpName" && !inputEmployeeName) ||
                (filterType === "byProjName" && !inputProjectName)
                  ? "disabled"
                  : ""
              }`}
              onClick={fetchData}
            >
              Delete
            </button>
          </div>
        </div>
      </div>

      {allocations?.length > 0 && (
        <>
          <div className="table-wrapper-del">
            <h3>Matching Allocation(s)</h3>
            <AllocationTable data={allocations} />
          </div>

          <div className="delete-dropdown-del">
            <label htmlFor="allocationSelect-del">
              Select Allocation to Delete:
            </label>
            <select
              id="allocationSelect-del"
              value={selectedAllocationId}
              onChange={(e) => setSelectedAllocationId(e.target.value)}
            >
              <option value="">-- Select Allocation ID --</option>
              {allocations.map((alloc) => (
                <option key={alloc.allocationId} value={alloc.allocationId}>
                  {alloc.allocationId}
                </option>
              ))}
            </select>

            <button
              className="delete-btn-del"
              onClick={handleDelete}
              disabled={
                !selectedAllocationId ||
                (filterType === "byEmpName" && !inputEmployeeName.trim()) ||
                (filterType === "byProjName" && !inputProjectName.trim())
              }
            >
              Delete
            </button>
          </div>
        </>
      )}
    </>
  );
};

export default DeleteAllocation;
