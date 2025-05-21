import React, {useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import "../../styles/Flipform.css";
import { toast } from "react-toastify";
import { fetchUserDetails, userLogin, userLogout, userRegister } from "../../api/api";
import { showMessage } from "../../utils/helpers";

const FlipLogin = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const queryParams = new URLSearchParams(location.search);
  const initialFlipState = queryParams.get("register") === "true";
  const [isFlipped, setIsFlipped] = useState(initialFlipState);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  useEffect(() => {
    const clearCreds = async() => {
      await userLogout();
    }
    clearCreds();
  })

  const handleLogin = async (e) => {
    e.preventDefault();
        let isAdmin= false;
    try {
      const response = await userLogin(email, password);

      // console.log(response);
      if (response) {

        showMessage("Logged In Successfully");

        const userDetailsResponse = await fetchUserDetails();
        if(userDetailsResponse.data){
          isAdmin= userDetailsResponse.data.isAdmin;
        }

        setTimeout(() => {
          toast.dismiss();
          navigate(isAdmin ? "/allocationAll" : "/userAllocation");
        }, 1500);
      } //redirection logic upon successfull login

    } catch (error) {
      if (error.response.status) {
        if (error.response.status === 401) {
          showMessage("Invalid credentials", true);
        } else if (error.response.status === 400) {
          showMessage("User does not exist.Please signup", true);
        } else {
          showMessage("An error occured", true);
        }
      }
    } //error messages accordingly
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      const result = await userRegister(email, password);
      if (result.success) {
        showMessage("Registration successful!");
        setTimeout(() => {
          toast.dismiss();
          navigate("/");
        }, 1000);
      } else {
        if (result.message && result.message.includes("Email not found")) {
          navigate("/register-with-details", {
            state: { email, password },
          });
        } else if(result.message && result.message.includes("User already exists!")){
          showMessage("User already exists!",true);
        }else {
          showMessage(result.message, true);
        }
      } // redirection logic upon register and respective error messages (if any)
    } catch (error) {
      showMessage("Something went wrong!", true);
    }
  };

  return (
    <div className={`ff-container ${isFlipped ? "ff-flipped" : ""}`}>
      <div className="ff-book">
        {/* Login Page */}
        <div className="ff-page ff-page-front">
          <div className="ff-left">
            <img src="/assets/login.png" alt="Login Visual" />
          </div>
          <div className="ff-right">
            <div className="ff-form-content">
              <h2>Login</h2>
              <form onSubmit={handleLogin}>
                <input
                  type="email"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
                <input
                  type="password"
                  placeholder="Enter your password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
                <button type="submit">Login</button>
              </form>
              <p
                onClick={() => {
                  setIsFlipped(true);
                  setEmail("");
                  setPassword("");
                }}
              >
                Don't have an account? <span>Sign Up</span>
              </p>
            </div>
          </div>
        </div>

        {/* Register Page */}
        <div className="ff-page ff-page-back">
          <div className="ff-left">
            <div className="ff-form-content">
              <h2>Sign Up</h2>
              <form onSubmit={handleRegister}>
                <input
                  type="email"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
                <input
                  type="password"
                  placeholder="Enter your password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
                <button type="submit">Register</button>
              </form>
              <p
                onClick={() => {
                  setIsFlipped(false);
                  setEmail("");
                  setPassword("");
                }}
              >
                Already have an account? <span>Login</span>
              </p>
            </div>
          </div>
          <div className="ff-right">
            <img src="/assets/login.png" alt="Register Visual" />
          </div>
        </div>
      </div>
    </div>
  );
};

export default FlipLogin;
