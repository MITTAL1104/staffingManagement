import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { updatePassword, fetchUserDetails } from "../../api/api";
import "../../styles/updatepass.css";
import { showMessage } from "../../utils/helpers";

const UpdatePassword = () => {
  const [email, setEmail] = useState("");
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const getUserEmail = async () => {
      try {
        const userDetails = await fetchUserDetails(); // From cookie-authenticated session
        if (userDetails?.data) {
          setEmail(userDetails.data.email);
        } else {
          navigate("/"); // Redirect to login if no email
        }
      } catch (error) {
        showMessage("Failed to fetch user details",true);
        navigate("/");
      }
    };

    getUserEmail();
  }, [navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await updatePassword(email, oldPassword, newPassword);
      if (response) {
        showMessage("Password updated successfully");

        // await userLogout();

        setTimeout(() => {
          toast.dismiss();
          navigate("/");
        }, 1500);
      }
    } catch (error) {
      if (error.response?.data) {
        showMessage("Old password is incorrect!", true);
      } else {
        showMessage("Something went wrong!", true);
      }
    }
  };

  return (
    <div className="auth-container-pass">
      <div className="auth-box-pass">
        <h2 className="auth-title-pass">Update Password</h2>
        <form onSubmit={handleSubmit} className="auth-form-pass">
          <input
            type="email"
            placeholder="Email"
            value={email}
            readOnly
            required
            className="auth-input-pass"
          />
          <input
            type="password"
            placeholder="Old Password"
            value={oldPassword}
            onChange={(e) => setOldPassword(e.target.value)}
            required
            className="auth-input-pass"
          />
          <input
            type="password"
            placeholder="New Password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            required
            className="auth-input-pass"
          />
          <button type="submit" className="auth-button-pass">
            Update Password
          </button>
        </form>
      </div>
    </div>
  );
};

export default UpdatePassword;
