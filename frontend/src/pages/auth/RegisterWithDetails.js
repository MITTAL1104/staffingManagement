import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { userRegisterWithDetails } from "../../api/api";
import "../../styles/regdetails.css";
import { showMessage } from "../../utils/helpers";

const RegisterWithDetails = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { email, password } = location.state || {};

  const [name, setName] = useState("");
  const [role, setRole] = useState("");
  const [dateOfJoining, setDateOfJoining] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const result = await userRegisterWithDetails(
        email,
        password,
        name,
        role,
        dateOfJoining
      );

      if (result.success) {
        showMessage("Registered successfully!");
        setTimeout(() => {
          toast.dismiss();
          navigate("/");
        }, 1000);
      } else {
        showMessage(result.message, true);
      } // corresponding message upon registeration (success or not)
    } catch (error) {
      showMessage("Something went wrong!");
    }
  };

  return (
    <div className="register-container-reg">
      <h2 className="heading-reg">Complete Your Registration</h2>
      <form onSubmit={handleSubmit} className="form-reg">
        <input
          className="input-reg"
          type="text"
          value={email}
          readOnly
          disabled
        />
        <input
          className="input-reg"
          type="password"
          value={password}
          readOnly
          disabled
        />
        <input
          className="input-reg"
          type="text"
          placeholder="Name"
          required
          onChange={(e) => setName(e.target.value)}
        />
        <input
          className="input-reg"
          type="text"
          placeholder="Role"
          required
          onChange={(e) => setRole(e.target.value)}
        />
        <input
          className="input-reg"
          type="date"
          placeholder="Date of Joining"
          required
          onChange={(e) => setDateOfJoining(e.target.value)}
        />
        <button type="submit" className="button-reg">
          Register
        </button>
      </form>
    </div>
  );
};

export default RegisterWithDetails;
