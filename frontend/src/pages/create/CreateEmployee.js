import { useEffect, useState } from "react";
import { createData, fetchAllRoles } from "../../api/api";
import "../../styles/create.css";
import { showMessage } from "../../utils/helpers";

const CreateEmployee = () => {
  const [roles, setRoles] = useState([]);
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    dateOfJoining: "",
    roleName: "",
    isActive: true,
    isAdmin: false,
  });

  useEffect(() => {
    const loadRoles = async () => {
      try {
        const data = await fetchAllRoles();
        setRoles(data);
      } catch (err) {
        console.error("Error fetching roles:", err);
      }
    };

    loadRoles();
  }, []); //for role names dropdown

  
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    setFormData({
      ...formData,
      [name]: type === "checkbox" ? checked : value,
    });
  }; //to handle the changed inputs

  const handleSubmit = async (e) => {
    e.preventDefault();
    setFormData("");

    try {
      const response = await createData("employee", formData);

      if (response) {
        showMessage("Employee created successfully!");
        setFormData({
          name: "",
          email: "",
          dateOfJoining: "",
          roleName: "",
          isActive: true,
          isAdmin: false,
        });
      } else {
        showMessage("Error: Failed to created employee", true);
      }
    } catch (error) {
      if (error.code === "ERR_BAD_REQUEST") {
        showMessage(`${error.response.data}`, true);
      } else {
        showMessage("Network error. Please try again.", true);
      }
    }
  };
  return (
    <div className="create-container">
      <h2>Create Employee</h2>
      <form onSubmit={handleSubmit}>
        <label>Employee Name</label>
        <input
          type="text"
          name="name"
          placeholder="Enter Name"
          value={formData.name}
          onChange={handleChange}
          required
        />

        <label>Employee Email</label>
        <input
          type="email"
          name="email"
          placeholder="Enter Email"
          value={formData.email}
          onChange={handleChange}
          required
        />
        <label>Date of Joining</label>
        <input
          type="date"
          name="dateOfJoining"
          placeholder="Enter Date of Joining"
          value={formData.dateOfJoining}
          onChange={handleChange}
          required
        />
        <label>Role</label>
        <select
          type="role"
          name="roleName"
          value={formData.roleName}
          onChange={handleChange}
          required
        >
          <option value="">Select a role</option>
          {roles.map((role) => (
            <option key={role.roleId} value={role.roleName}>
              {role.roleName}
            </option>
          ))}
        </select>

        <div className="checkbox-container-create">
          <div className="checkbox-item-create">
            <input
              type="checkbox"
              name="isActive"
              checked={formData.isActive}
              onChange={handleChange}
            />
            <label>Active</label>
          </div>
          <div className="checkbox-item-create">
            <input
              type="checkbox"
              name="isAdmin"
              checked={formData.isAdmin}
              onChange={handleChange}
            />
            <label>Admin</label>
          </div>
        </div>
        <button type="submit">Create Employee</button>
      </form>
    </div>
  );
};

export default CreateEmployee;
