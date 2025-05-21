import { useState } from "react";
import Swal from "sweetalert2";
import { deleteDataByID,fetchById, fetchProjByNameForDelete} from "../../api/api";
import ProjectTable from "../../components/ProjectTable";
import { showMessage } from "../../utils/helpers";

const DeleteProject = () => {

    const [filterType,setFilterType] = useState("byName");
    const [inputId,setInputId] = useState("");
    const [name,setName] = useState("");
    const [duplicateProjects,setDuplicateProjects] = useState([]);
    const [selectedId,setSelectedId]=useState("");

    const handleFilterChange = (value) => {
        setFilterType(value);
        setInputId("");
        setName("");
        setDuplicateProjects([]);
        setSelectedId("");
    };

    const handleFetchData = async() => {
        try{
            let response = [];

            if(filterType==="byId"){
                response = await fetchById("project",inputId);
                showDeleteConfirmation(response);
            } else {
                response = await fetchProjByNameForDelete("project",name);
                if(response.length===1){
                    showDeleteConfirmation(response[0]);
                } else if(response.length>1){
                    setDuplicateProjects(response);
                } else{
                    showMessage("No matching(active) projects found",true);
                }
            }
        }catch(err){
            showMessage("Error fetching projects",true);
        }
    }

    const handleFinalDelete = async () => {

        try{
            const confirm = await Swal.fire({
                title:"Confirm Delete Project",
                text:`Are you sure you want to delete project with ID: ${selectedId}`,
                icon:"warning",
                showCancelButton:true,
                confirmButtonColor:"#d33",
                cancelButtonColor:"#3085d6",
                confirmButtonText:"Yes, Delete it!",
                cancelButtonText:"Cancel",
            })

            if(confirm.isConfirmed){
                const deleteRes = await deleteDataByID("project",selectedId);

                if(deleteRes){
                    showMessage("Project Deleted Successfully");
                    setName("");
                    setSelectedId("");
                    setDuplicateProjects([]);    
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
                const delId = await deleteDataByID("project",id);

                if(delId){
                    showMessage("Project Deleted Successfully");
                    setInputId("");
                }
            } else{
                const delName = await deleteDataByID("project",id);
                
                if(delName){
                    showMessage("Project Deleted Successfully");
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

    const showDeleteConfirmation = (project) => {
        const entityDetails =  `
        <strong>Project ID:</strong> ${project.projectId} <br>
        <strong>Project Name:</strong> ${project.projectName} <br>
        <strong>Description:</strong> ${project.description} <br>
        <strong>Owner Name:</strong> ${project.projectOwnerName} <br>
        <strong>Start Date:</strong> ${project.startDate} <br>
        <strong>End Date:</strong> ${project.endDate} <br>
        <strong>Status:</strong> ${project.isActive?"Active":"Inactive"} <br>`

        Swal.fire({
            title:"Confirm Delete Project",
            html:`
                <div class="del-preview-box">
                    ${entityDetails}
                </div>
                <p class="del-confirm-warning">
                    Are you sure you want to delete this project?
                </p>`,
            icon:"warning",
            showCancelButton:true,
            confirmButtonColor:"#d33",
            cancelButtonColor:"#3085d6",
            confirmButtonText:"Yes, Delete it!",
            cancelButtonText:"Cancel",
        }).then(async (result)=>{
            if(result.isConfirmed){
                handleDelete(project.projectId);
            }
        })
    }

    return(
        <>
        <div className="del-container">
            <h2 className="del-title">Delete Project</h2>

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
                        placeholder="Enter Project ID"
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
                  placeholder="Enter Project Name"
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

            {duplicateProjects.length > 1 && (
                <div>
                    <div className="table-wrapper-del">
                        <h3>Projects with Matching names</h3>
                        <ProjectTable data={duplicateProjects} />
                    </div>

                <div className="delete-dropdown-del">
                    <label htmlFor="allocationSelect-del">
                        Select Project ID to delete:
                    </label>
                    <select
                        id="allocationSelect"
                        value={selectedId}
                        onChange={(e) => setSelectedId(e.target.value)}
                    >
                        <option value="">-- Select ID --</option>
                            {duplicateProjects.map((proj) => (
                                <option key={proj.projectId} value={proj.projectId}>
                                {proj.projectId}
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

export default DeleteProject;