import React, { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { fetchByEmployeeId, fetchById, fetchUserDetails} from "../../api/api";
import { FaEye } from "react-icons/fa";
import Swal from "sweetalert2";
import "../../styles/userAllocations.css";
import { preventBackNavigation, showMessage } from "../../utils/helpers";

const AllocationUser = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [allocations, setAllocations] = useState([]);
  const [noAllocations, setNoAllocations] = useState(false);
  const isMounted = useRef(false);

  useEffect(() => {
    if (!isMounted.current) {
      isMounted.current = true;
      getUserDetails();
    }
  }, []);

    useEffect(() => {
    const cleanup = preventBackNavigation();

    return cleanup;
  }, []);

  const getUserDetails = async () => {
    try {
      setLoading(true);

      const userInfo = await fetchUserDetails(); // this hits /api/auth/me

      const empId = userInfo.data.employeeId;
      if (!empId) {
        showMessage("Unauthorized. Please login.", true);
        navigate("/");
        return;
      }

      const timeout = setTimeout(() => {
        setNoAllocations(true);
        setLoading(false);
      }, 10000);

      const response = await fetchByEmployeeId(empId);

      if (response.length > 0) {
        const allocationsWithProject = await Promise.all(
          response.map(async (allocation) => {
            const projectData = await fetchById("project", allocation.projectId);
            return {
              ...allocation,
              project: projectData,
            };
          })
        );

        clearTimeout(timeout);
        setAllocations(allocationsWithProject);
        setNoAllocations(false);
      } else {
        setNoAllocations(true);
      }
    } catch (error) {
      showMessage("Error fetching allocation data", true);
      setNoAllocations(true);
    } finally {
      setLoading(false);
    }
  };

  const handleViewDetails = (allocation) => {
    Swal.fire({
      title: `<strong>Allocation ID: ${allocation.allocationId}</strong>`,
      html: `
        <div style="text-align: left;">
          <p><strong>Employee Name:</strong> ${allocation.assigneeName}</p>
          <p><strong>Project Name:</strong> ${allocation.project?.projectName}</p>
          <p><strong>Start Date:</strong> ${allocation.allocationStartDate}</p>
          <p><strong>End Date:</strong> ${allocation.allocationEndDate}</p>
          <p><strong>Percentage Allocation:</strong> ${allocation.percentageAllocation}%</p>
          <p><strong>Allocated By:</strong> ${allocation.allocatorName}</p>
          <p><strong>Status:</strong> ${allocation.isActive ? "Active" : "Inactive"}</p>
        </div>
      `,
      confirmButtonText: "Close",
      confirmButtonColor: "#dc3545",
      customClass: {
        popup: "allocation-swal-popup",
      },
    });
  };

  return (
    <div className="normal-user-container">
      <h2>Your Allocation Details</h2>
      {loading ? (
        <p className="loading-text">Loading...</p>
      ) : noAllocations ? (
        <p className="no-project">No allocation data found.</p>
      ) : (
        <div className="table-container">
          <table className="allocation-table">
            <thead>
              <tr>
                {/*<th>Allocation ID</th>*/}
                <th>Project Name</th>
                <th>Start Date</th>
                <th>End Date</th>
                {/*<th>Allocated By</th>*/}
                <th>Status</th>
                <th>View</th>
              </tr>
            </thead>
            <tbody>
              {allocations.map((allocation) => (
                <tr key={allocation.allocationId}>
                  {/*<td>{allocation.allocationId}</td>*/}
                  <td>{allocation.project?.projectName || "N/A"}</td>
                  <td>{allocation.allocationStartDate}</td>
                  <td>{allocation.allocationEndDate}</td>
                  {/*<td>{allocation.allocatorName}</td>*/}
                  <td>{allocation.isActive ? "Active" : "Inactive"}</td>
                  <td>
                    <button className="view-button" onClick={() => handleViewDetails(allocation)}>
                      <FaEye />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default AllocationUser;
