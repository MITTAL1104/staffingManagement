import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Layout from "./Layout";
import ReadAllocation from "./pages/read/ReadAllocation";
import CreateEmployee from "./pages/create/CreateEmployee";
import CreateProject from "./pages/create/CreateProject";
import PrivateRoute from "./pages/auth/PrivateRoute";
import Unauthorized from "./pages/Unauthorized";
import EmployeeUser from "./pages/userPages/EmployeeUser";
import { ToastContainer } from "react-toastify";
import UpdatePassword from "./pages/auth/UpdatePassword";
import LoginRegister from "./pages/auth/FlipLogin";
import AdminDetails from "./pages/AdminDetails";
import EmployeeAll from "./pages/displayAll/EmployeeAll";
import ProjectAll from "./pages/displayAll/ProjectAll";
import AllocationAll from "./pages/displayAll/AllocationAll";
import UpdateProject from "./pages/update/UpdateProject";
import RegisterWithDetails from "./pages/auth/RegisterWithDetails";
import AllocationUser from "./pages/userPages/AllocationUser";
import UpdateAllocation from "./pages/update/UpdateAllocation";
import DeleteAllocation from "./pages/delete/DeleteAllocation";
import NotifCleaner from "./components/NotifCleaner";
import ReadEmployee from "./pages/read/ReadEmployee";
import ReadProject from "./pages/read/ReadProject";
import UpdateEmployee from "./pages/update/UpdateEmployee";
import useIdleLogout from "./components/IdleLogout";
import DeleteEmployee from "./pages/delete/DeleteEmployee";
import DeleteProject from "./pages/delete/DeleteProject";
import CreateAllocation from "./pages/create/CreateAllocation";


const App = () => {

  useIdleLogout()
  return (
    <>
      <ToastContainer position="top-right" autoClose={5000} />
      <Router>
        <NotifCleaner/>
        <Layout> 
          <Routes>
            <Route path="/" element={<LoginRegister />} />
            <Route path="/unauthorized" element={<Unauthorized />} />
            
            <Route path="/register-with-details" element={<RegisterWithDetails/>}/>

            <Route path="/userEmployee" element={<PrivateRoute><EmployeeUser /></PrivateRoute>}/>
            <Route path="/userAllocation" element={<PrivateRoute><AllocationUser /></PrivateRoute>}/>
            
            <Route path="/updatePassword" element={<PrivateRoute><UpdatePassword /></PrivateRoute>} />

            <Route path="/adminDet" element={<PrivateRoute adminOnly={true}><AdminDetails/></PrivateRoute>}/>
            <Route path="/employeeAll" element={<PrivateRoute adminOnly={true}><EmployeeAll/></PrivateRoute>}/>
            <Route path="/projectAll" element={<PrivateRoute adminOnly={true}><ProjectAll/></PrivateRoute>}/>
            <Route path="/allocationAll" element={<PrivateRoute adminOnly={true}><AllocationAll/></PrivateRoute>}/>
            <Route path="/employee/read"element={<PrivateRoute adminOnly={true}><ReadEmployee/></PrivateRoute>}/>
            <Route path="/project/read"element={<PrivateRoute adminOnly={true}><ReadProject/></PrivateRoute>}/>
            <Route path="/allocation/read"element={<PrivateRoute adminOnly={true}><ReadAllocation /></PrivateRoute>}/>
            <Route path="/employee/create"element={<PrivateRoute adminOnly={true}><CreateEmployee /></PrivateRoute> }/>
            <Route path="/project/create"element={<PrivateRoute adminOnly={true}><CreateProject /> </PrivateRoute>}/>
            <Route path="/allocation/create" element={<PrivateRoute adminOnly={true}><CreateAllocation/></PrivateRoute>}/>
            <Route path="/employee/update" element={<PrivateRoute adminOnly={true}><UpdateEmployee /></PrivateRoute>}/>
            <Route path="/project/update"element={<PrivateRoute adminOnly={true}><UpdateProject /></PrivateRoute>}/>
            <Route path="/allocation/update"element={<PrivateRoute adminOnly={true}><UpdateAllocation/></PrivateRoute>}/>
            <Route path="/employee/delete"element={<PrivateRoute adminOnly={true}><DeleteEmployee/></PrivateRoute>}/> 
            <Route path="/project/delete"element={<PrivateRoute adminOnly={true}><DeleteProject/></PrivateRoute>}/>
            <Route path="/allocation/delete"element={<PrivateRoute adminOnly={true}><DeleteAllocation/></PrivateRoute>}/>
          </Routes>
        </Layout>
      </Router>
    </>
  );
};

export default App;
