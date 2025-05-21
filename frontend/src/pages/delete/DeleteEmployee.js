import { useState } from "react";
import Swal from "sweetalert2";
import { deleteDataByID,fetchById, fetchEmpByNameForDelete} from "../../api/api";
import EmployeeTable from "../../components/EmployeeTable";
import { showMessage } from "../../utils/helpers";

const DeleteEmployee = () => {

    const [filterType,setFilterType] = useState("byName");
    const [inputId,setInputId] = useState("");
    const [name,setName] = useState("");
    const [duplicateEmployees,setDuplicateEmployees] = useState([]);
    const [selectedId,setSelectedId]=useState("");

    const handleFilterChange = (value) => {
        setFilterType(value);
        setInputId("");
        setName("");
        setDuplicateEmployees([]);
        setSelectedId("");
    };

    const handleFetchData = async() => {
        try{
            let response = [];

            if(filterType==="byId"){
                response = await fetchById("employee",inputId);
                showDeleteConfirmation(response);
            } else {
                response = await fetchEmpByNameForDelete("employee",name);
                if(response.length===1){
                    showDeleteConfirmation(response[0]);
                } else if(response.length>1){
                    setDuplicateEmployees(response);
                } else{
                    showMessage("No matching(active) employees found",true)
                }
            }
        }catch(err){
            showMessage("Error fetching employees",true);
        }
    }

    const handleFinalDelete = async () => {

        try{
            const confirm = await Swal.fire({
                title:"Confirm Delete Employee",
                text:`Are you sure you want to delete employee with ID: ${selectedId}`,
                icon:"warning",
                showCancelButton:true,
                confirmButtonColor:"#d33",
                cancelButtonColor:"#3085d6",
                confirmButtonText:"Yes, Delete it!",
                cancelButtonText:"Cancel",
            })

            if(confirm.isConfirmed){
                // console.log(selectedId);
                const deleteRes = await deleteDataByID("employee",selectedId);

                if(deleteRes){
                    showMessage("Employee Deleted Successfully");
                    setName("");
                    setSelectedId("");
                    setDuplicateEmployees([]);    
                }
            }
        }catch(err){
            if(err.response.status===500){
                showMessage("Internal Server Error",true);
            }else{
                    const msg = err?.response?.data || "Network error. Please try again.";
                    showMessage(msg, true);
            }
        }
    }

    const handleDelete = async(id) => {
        try{
            if(filterType==="byId"){
                const delId = await deleteDataByID("employee",id);

                if(delId){
                    showMessage("Employee Deleted Successfully");
                    setInputId("");
                }
            } else{
                const delName = await deleteDataByID("employee",id);
                
                if(delName){
                    showMessage("Employee Deleted Successfully");
                    setName("");
                }
            }
        } catch(err){
            if(err.response.status===500){
                showMessage("Internal Server Error",true);
            }else{
                    const msg = err?.response?.data || "Network error. Please try again.";
                    showMessage(msg, true);
            }

        }
    }

    const showDeleteConfirmation = (employee) => {
        const entityDetails =  `
        <strong>ID:</strong> ${employee.employeeId} <br>
        <strong>Name:</strong> ${employee.name} <br>
        <strong>Email:</strong> ${employee.email} <br>
        <strong>Role:</strong> ${employee.roleName} <br>
        <strong>Date of Joining:</strong> ${employee.dateOfJoining} <br>
        <strong>Status:</strong> ${employee.isActive?"Active":"Inactive"} <br>
        <strong>Admin:</strong> ${employee.isAdmin?"Yes":"No"} <br>`

        Swal.fire({
            title:"Confirm Delete Employee",
            html:`
                <div class="del-preview-box">
                    ${entityDetails}
                </div>
                <p class="del-confirm-warning">
                    Are you sure you want to delete this employee?
                </p>`,
            icon:"warning",
            showCancelButton:true,
            confirmButtonColor:"#d33",
            cancelButtonColor:"#3085d6",
            confirmButtonText:"Yes, Delete it!",
            cancelButtonText:"Cancel",
        }).then(async (result)=>{
            if(result.isConfirmed){
                handleDelete(employee.employeeId);
            }
        })
    }

    return(
        <>
        <div className="del-container">
            <h2 className="del-title">Delete Employee</h2>

            <div className="del-options">
                <label>
                    <input
                        type="radio"
                        name="filterType"
                        value="byName" 
                        checked={filterType==="byName"}
                        onChange={()=> handleFilterChange("byName")}
                    />
                    Delete By Name
                </label>
                <label>
                    <input
                        type="radio"
                        name="filterType"
                        value="byId"
                        checked={filterType === "byId"}
                        onChange={() => handleFilterChange("byId")}
                     />
                    Delete By ID
                </label>
            </div>

            {filterType==="byId"?(
                <div className="del-input">
                    <input
                        type="text"
                        placeholder="Enter Employee ID"
                        value={inputId}
                        onChange={(e) => setInputId(e.target.value)}
                    />
                    <button
                        onClick={handleFetchData}
                        className={`del-button ${!inputId.trim() ? "disabled" : ""}`}
                        disabled={!inputId.trim()}
                    >
                    Delete
                    </button>
                </div>
            ):(
                <div className="del-input">
                <input
                  type="text"
                  placeholder="Enter Employee Name"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                />
                <button
                  onClick={handleFetchData}
                  className={`get-button-del ${!name.trim() ? "disabled" : ""}`}
                  disabled={!name.trim()}
                >
                  Search
                </button>
              </div>
            )}
            </div>

            {duplicateEmployees.length > 1 && (
                <div>
                    <div className="table-wrapper-del">
                        <h3>Employees with Matching names</h3>
                        <EmployeeTable data={duplicateEmployees} />
                    </div>

                <div className="delete-dropdown-del">
                    <label htmlFor="allocationSelect-del">
                        Select Employee ID to delete:
                    </label>
                    <select
                        id="allocationSelect"
                        value={selectedId}
                        onChange={(e) => setSelectedId(e.target.value)}
                    >
                        <option value="">-- Select ID --</option>
                            {duplicateEmployees.map((emp) => (
                                <option key={emp.employeeId} value={emp.employeeId}>
                                {emp.employeeId}
                        </option>
                            ))}
                    </select>
            <button
              onClick={handleFinalDelete}
              className="delete-btn-del"
              disabled={!selectedId}
            >
              Confirm Delete
            </button>
          </div>
        </div>
      )}

        </>

    )
}

export default DeleteEmployee;