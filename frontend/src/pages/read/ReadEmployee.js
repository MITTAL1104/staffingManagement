import { useState } from "react";
import {
  downloadExcel,
  fetchAllData,
  fetchById,
  fetchByName,
} from "../../api/api";
import "../../styles/read.css";
import EmployeeTable from "../../components/EmployeeTable";
import { FaDownload } from "react-icons/fa";
import { showMessage } from "../../utils/helpers";

const ReadEmployee = () => {
  const [data, setData] = useState(null);
  const [filterType, setFilterType] = useState("byName");
  const [inputId, setInputId] = useState("");
  const [name, setName] = useState("");
  const [downloadId,setDownloadId] = useState("");
  const [downloadName,setDownloadName] = useState("");

  const handleFetchData = async () => {
    setData(null);
    let result;

    try {
      if (filterType === "all") {
        result = await fetchAllData("employee");
      } else if (filterType === "byId" && inputId) {
        result = await fetchById("employee", inputId);
      } else if (filterType === "byName" && name) {
        result = await fetchByName("employee", name);
      } else if (filterType === "allActive") {
        result = await fetchAllData("employee",true);
      }

      if (
        filterType === "byId" &&
        (!result || (Array.isArray(result) && result.length === 0))
      ) {
        setInputId("");
        setFilterType("byId");
        throw new Error(`Employee with ID '${inputId}' not found!`);
      } else if (
        filterType === "byName" &&
        (!result || (Array.isArray(result) && result.length === 0))
      ) {
        setName("");
        setFilterType("byName");
        throw new Error(`Employee with Name '${name}' not found!`);
      }

      // Ensure data is in array format for consistency
      setData(Array.isArray(result) ? result : [result]);
      showMessage(`Employee details loaded successfully`);
      setDownloadId(inputId);
      setDownloadName(name);

    } catch (error) {
      showMessage(error.response.data.error, true);
    }
  };

  const handleFilterChange = (type) => {
    setFilterType(type);
    setInputId("");
    setName("");
    setData(null);
  };

  const handleInputIdChange = (e) => {
    const value = e.target.value;
    if (/^\d*$/.test(value)) {
      setInputId(value);
    }
  };

  const handleNameChange = (e) => {
    const value = e.target.value;
    if (/^[a-zA-Z\s]*$/.test(value)) {
      setName(value);
    }
  };

  const handleDownload = () => {
    let fileName = "employee_data";
    let type = "", value = "";
    let entity="employee";
  
    if (filterType === "byId" && downloadId) {
      type = "byId";
      value = downloadId;
    } else if (filterType === "byName" && downloadName) {
      type = "byName";
      value = downloadName;
    } else if (filterType === "allActive") {
      type = "allActive";
    } 
    downloadExcel(fileName, type, value,entity);
  };
  
  return (
    <div className="read-container">
<div className="header-with-download">
  <h2 className="center-heading">View Employee Details</h2>
  <FaDownload
    className={`download-icon ${!data || data.length === 0 ? "disabled-icon" : ""}`}
    onClick={() => {
      if (data && data.length > 0) handleDownload();
    }}
    title="Download Excel"
  />
</div>
      <div className="read-options">
        <label>
          <input
            type="radio"
            name="filter"
            value="byName"
            checked={filterType === "byName"}
            onChange={() => handleFilterChange("byName")}
          />
          By Employee Name
        </label>
        <label>
          <input
            type="radio"
            name="filter"
            value="byId"
            checked={filterType === "byId"}
            onChange={() => handleFilterChange("byId")}
          />
          By Employee ID
        </label>
        <label>
          <input
            type="radio"
            name="filter"
            value="allActive"
            checked={filterType === "allActive"}
            onChange={() => handleFilterChange("allActive")}
          />
          Active Employees
        </label>
        <label>
          <input
            type="radio"
            name="filter"
            value="all"
            checked={filterType === "all"}
            onChange={() => handleFilterChange("all")}
          />
          All
        </label>
      </div>

      {filterType === "byId" && (
        <div className="id-input">
          <input
            type="text"
            placeholder={`Enter Employee ID`}
            value={inputId}
            onChange={handleInputIdChange}
          />
        </div>
      )}

      {filterType === "byName" && (
        <div className="name-input">
          <input
            type="text"
            placeholder={`Enter Employee Name`}
            value={name}
            onChange={handleNameChange}
          />
        </div>
      )}

      <button
        className={`fetch-button ${
          (filterType === "byId" && !inputId) ||
          (filterType === "byName" && !name)
            ? "disabled-btn"
            : ""
        }`}
        onClick={handleFetchData}
        disabled={
          (filterType === "byId" && !inputId) ||
          (filterType === "byName" && !name)
        }
      >
        Submit
      </button>

      {data && data.length > 0 && <EmployeeTable data={data} />}
    </div>
  );
};

export default ReadEmployee;
