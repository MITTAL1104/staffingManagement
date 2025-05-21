import axios from "axios";

const API_BASE_URL = "http://localhost:8080/staff";

const authorizedRequest = async (method, url, data = null) => {

  try {
    const response = await axios({
      method,
      url,
      data,
      withCredentials:true,
    });
    return response.data;
  } catch (error) {
    console.error("Error in authorized request: ", error);
    throw error;
  }
};

export const downloadExcel = async (fileName = "data", type, value, entity) => {
  const url = new URL(`${API_BASE_URL}/${entity}/downloadExcel`);
  if (type !== undefined) url.searchParams.append("type", type);
  if (value !== undefined) url.searchParams.append("value", value);

  try {
    const response = await fetch(url, {
      method: "GET",
      credentials: "include",
    });

    if (!response.ok) throw new Error("Failed to download");

    const blob = await response.blob();
    const urlBlob = window.URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = urlBlob;
    link.setAttribute("download", `${fileName}.xlsx`);
    document.body.appendChild(link);
    link.click();
    link.remove();
  } catch (error) {
    console.error("Download failed", error);
  }
};


export const userRegister = async (email, password) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/register`, {
      email,
      password,
    });
    return { success: true, message: response.data };
  } catch (error) {
    return {
      success: false,
      message: error.response ? error.response.data : "Error registering user",
    };
  }
};

export const userRegisterWithDetails = async (
  email,
  password,
  name,
  role,
  dateOfJoining
) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/registerWithDetails`, {
      email,
      password,
      name,
      role,
      dateOfJoining,
    });
    return { success: true, message: response.data };
  } catch (error) {
    return {
      success: false,
      message: error.response
        ? error.response.data
        : "Error registering user with details",
    };
  }
};

export const userLogin = async (email, password) => {
  const response = await axios.post(`${API_BASE_URL}/login`, {
    email,
    password,
  },{withCredentials:true});
  return response.data;
};

export const userLogout = async () => {
  const response = await axios.get(`${API_BASE_URL}/logout`, {
    withCredentials: true, 
  });
  return response.data;
};

export const updatePassword = async(email,oldPassword,newPassword) => {
  return authorizedRequest("post",`${API_BASE_URL}/updatePassword`,{
    email,
    oldPassword,
    newPassword
  })
}

export const fetchUserDetails = async() =>{
  try{
    const response = await axios.get(`${API_BASE_URL}/details`,{
      withCredentials:true,
    });
    return response;
  } catch(error){
    throw error;
  }
}

export const fetchAllData = async (entity,onlyActive=false) => {
  const queryParam = onlyActive?"?onlyActive=true":"?onlyActive=false";
  return authorizedRequest("get", `${API_BASE_URL}/${entity}/getAll${queryParam}`);
};

export const fetchById = async (entity, id) => {
  return authorizedRequest("get", `${API_BASE_URL}/${entity}/getById/${id}`);
};

export const fetchAllAllocationByEmployeeName = async (name) => {
  return authorizedRequest(
    "get",
    `${API_BASE_URL}/allocation/getAllocByEmpName/${name}`
  );
};

export const fetchByEmployeeId = async (id) => {
  return authorizedRequest(
    "get",
    `${API_BASE_URL}/allocation/getByEmpId/${id}`
  );
};

export const fetchByName = async (entity, name) => {
  return authorizedRequest(
    "get",
    `${API_BASE_URL}/${entity}/getAllByName/${name}`
  );
};

export const fetchEmpByNameForDelete = async (entity, name) => {
  return authorizedRequest(
    "get",
    `${API_BASE_URL}/${entity}/getEmpByNameForDelete/${name}`
  );
};

export const fetchProjByNameForDelete = async (entity, name) => {
  return authorizedRequest(
    "get",
    `${API_BASE_URL}/${entity}/getProjByNameForDelete/${name}`
  );
};

export const fetchAllocationByProjectName = async (name) => {
  return authorizedRequest(
    "get",
    `${API_BASE_URL}/allocation/getByProjName/${name}`
  );
};

export const fetchAllNames = async (entity,name) => {
  return authorizedRequest("get", `${API_BASE_URL}/${entity}/getNames/${name}`);
};

export const getAllEmpNames = async()=> {
  return authorizedRequest("get",`${API_BASE_URL}/employee/getAllNames`);
}

export const fetchAllIds = async (entity, name = "") => {
  return authorizedRequest("get", `${API_BASE_URL}/${entity}/getIds/${name}`);
};

export const fetchAllocIds = async (name = "") => {
  return authorizedRequest(
    "get",
    `${API_BASE_URL}/allocation/getAllocIds/${name}`
  );
};

export const fetchAllProjNames = async (entity, name = "") => {
  return authorizedRequest(
    "get",
    `${API_BASE_URL}/${entity}/getProjects/${name}`
  );
};

export const fetchEmpNameByEmail = async (entity, email) => {
  return authorizedRequest(
    "get",
    `${API_BASE_URL}/${entity}/getNameByEmail/${email}`
  );
};

export const fetchEmpIdByName = async (name) => {
  return authorizedRequest(
    "get",
    `${API_BASE_URL}/allocation/getEmpIdByName/${name}`
  );
};

export const fetchProjIdByName = async (name) => {
  return authorizedRequest(
    "get",
    `${API_BASE_URL}/allocation/getProjIdByName/${name}`
  );
};

export const fetchAllocForDeleteByEmpName = async (name) => {
  return authorizedRequest(
    "get",
    `${API_BASE_URL}/allocation/getAllocDelByEmpName/${name}`
  );
};

export const fetchAllocForDeleteByProjName = async (name) => {
  return authorizedRequest(
    "get",
    `${API_BASE_URL}/allocation/getAllocDelByProjName/${name}`
  )
};

export const fetchAllRoles = async()=>{
  return authorizedRequest(
    "get",
    `${API_BASE_URL}/employee/getRoles`
  )
} 

export const createData = async (entity, data) => {
  return authorizedRequest("post", `${API_BASE_URL}/${entity}/add`, data);
};

export const updateDataById = async (entity, id, data) => {
  return authorizedRequest(
    "put",
    `${API_BASE_URL}/${entity}/updateId/${id}`,
    data
  );
};

export const deleteDataByID = async (entity, id) => {
  return authorizedRequest(
    "delete",
    `${API_BASE_URL}/${entity}/deleteId/${id}`
  );
};
