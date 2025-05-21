import { useState } from "react";
import {
  downloadExcel,
  fetchAllAllocationByEmployeeName,
  fetchAllData,
  fetchAllocationByProjectName,
  fetchById,
} from "../../api/api";
import "../../styles/read.css";
import AllocationTable from "../../components/AllocationTable";
import { FaDownload } from "react-icons/fa";
import { showMessage } from "../../utils/helpers";

const ReadAllocation = () => {
  const [data, setData] = useState([]);
  const [filterType, setFilterType] = useState("byId");
  const [inputId, setInputId] = useState("");
  const [inputEmployeeName, setEmployeeName] = useState("");
  const [inputProjectName, setProjectName] = useState("");
  const [downloadId, setDownloadId] = useState("");
  const [downloadEmpName, setDownloadEmpName] = useState("");
  const [downloadProjName, setDownloadProjName] = useState("");

  const handleFetchData = async () => {
    setData(null);

    let result = [];
    try{

    if (filterType === "all") {
      result = await fetchAllData("allocation");
    } else if (filterType === "byId" && inputId) {
      result = await fetchById("allocation",inputId);
      result = result ? [result] : [];
    } else if (filterType === "byEmployeeName" && inputEmployeeName) {
      result = await fetchAllAllocationByEmployeeName(inputEmployeeName);
    } else if (filterType === "byProjectName" && inputProjectName) {
      result = await fetchAllocationByProjectName(inputProjectName);
    } else if (filterType === "allActive") {
      result = await fetchAllData("allocation",true);
    }

    if (
      filterType === "byId" &&
      (!result ||
        (Array.isArray(result) && result.length === 0) ||
        (typeof result === "object" && Object.keys(result).length === 0))
    ) {
      showMessage(`Allocation with ID '${inputId}' not found!`, true);
      setInputId("");
      setFilterType("byId");
      return;
    } //not found messages

    if (
      filterType === "byEmployeeName" &&
      (!result ||
        (Array.isArray(result) && result.length === 0) ||
        (typeof result === "object" && Object.keys(result).length === 0))
    ) {
      showMessage(
        `Allocation for Employee Name '${inputEmployeeName}' not found!`,
        true
      );
      setEmployeeName("");
      setFilterType("byEmployeeName");
      return;
    }

    if (
      filterType === "byProjectName" &&
      (!result ||
        (Array.isArray(result) && result.length === 0) ||
        (typeof result === "object" && Object.keys(result).length === 0))
    ) {
      showMessage(
        `Allocations for Project Name '${inputProjectName}' not found!`,
        true
      );
      setProjectName("");
      setFilterType("byProjectName");
      return;
    }

    setData(Array.isArray(result) ? result : [result]);
    showMessage(`Allocation details loaded successfully `);
    setDownloadId(inputId);
    setDownloadEmpName(inputEmployeeName);
    setDownloadProjName(inputProjectName);
    setInputId("");

  }catch(error){
    showMessage(error.response.data.error, true);
  }
  };

  const handleInputIdChange = (e) => {
    const value = e.target.value;
    if (/^\d*$/.test(value)) {
      if (filterType === "byId") {
        setInputId(value);
      }
    }
  };

  const handleNameChange = (e) => {
    const value = e.target.value;
    if (/^[a-zA-Z\s]*$/.test(value)) {
      if (filterType === "byEmployeeName") {
        setEmployeeName(value);
      } else if (filterType === "byProjectName") {
        setProjectName(value);
      }
    }
  };

  const handleFilterChange = (type) => {
    setFilterType(type);
    setInputId("");
    setEmployeeName("");
    setProjectName("");
    setData(null);
  };

  const handleDownload = () => {
    let fileName = "allocation_data";
    let type = "",
    value = "";
    let entity = "allocation";

    if (filterType === "byId" && downloadId) {
      type = "byId";
      value = downloadId;
    } else if (filterType === "byEmployeeName" && downloadEmpName) {
      type = "byEmployeeName";
      value = downloadEmpName;
    } else if (filterType === "byProjectName" && downloadProjName) {
      type = "byProjectName";
      value = downloadProjName;
    } else if (filterType === "allActive") {
      type = "allActive";
    }
    downloadExcel(fileName, type, value, entity);
  };

  return (
    <div className="read-container">
    <div className="header-with-download">
  <h2 className="center-heading">View Allocation Details</h2>
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
            value="byId"
            checked={filterType === "byId"}
            onChange={() => handleFilterChange("byId")}
          />
          By Allocation ID
        </label>
        <label>
          <input
            type="radio"
            name="filter"
            value="byEmployeeName"
            checked={filterType === "byEmployeeName"}
            onChange={() => handleFilterChange("byEmployeeName")}
          />
          By Employee Name
        </label>
        <label>
          <input
            type="radio"
            name="filter"
            value="byProjectName"
            checked={filterType === "byProjectName"}
            onChange={() => handleFilterChange("byProjectName")}
          />
          By Project Name
        </label>
        <label>
          <input
            type="radio"
            name="filter"
            value="allActive"
            checked={filterType === "allActive"}
            onChange={() => handleFilterChange("allActive")}
          />
          Active Allocations
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
            placeholder="Enter Allocation ID"
            value={inputId}
            onChange={handleInputIdChange}
          />
        </div>
      )}
      {filterType === "byEmployeeName" && (
        <div className="id-input">
          <input
            type="text"
            placeholder="Enter Employee Name"
            value={inputEmployeeName}
            onChange={handleNameChange}
          />
        </div>
      )}
      {filterType === "byProjectName" && (
        <div className="id-input">
          <input
            type="text"
            placeholder="Enter Project Name"
            value={inputProjectName}
            onChange={handleNameChange}
          />
        </div>
      )}

      <button
        className={`fetch-button ${
          (filterType === "byId" && !inputId) ||
          (filterType === "byEmployeeName" && !inputEmployeeName) ||
          (filterType === "byProjectName" && !inputProjectName)
            ? "disabled-btn"
            : ""
        }`}
        onClick={handleFetchData}
        disabled={
          (filterType === "byId" && !inputId) ||
          (filterType === "byEmployeeName" && !inputEmployeeName) ||
          (filterType === "byProjectName" && !inputProjectName)
        }
      >
        Submit
      </button>
      {data && data.length > 0 && <AllocationTable data={data} />}
    </div>
  );
};

export default ReadAllocation;
