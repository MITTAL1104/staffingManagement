import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import Header from "./pages/userPages/UserHeader";
import MainHeader from "./pages/MainHeader";
import AdminHeader from "./pages/AdminHeader";
import { fetchUserDetails } from "./api/api";
import { showMessage } from "./utils/helpers";

const Layout = ({ children }) => {
  const location = useLocation();
  const path = location.pathname;

  const userHeaderRoute = ["/userEmployee", "/userAllocation", "/unauthorized","/updatePassword"];
  const mainHeaderRoute = ["/", "/register-with-details"];

  const [isAdmin, setIsAdmin] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false); // null = unknown yet

  const getUserDetails = async () => {
    try {
      const response = await fetchUserDetails();

      if (response.data) {
        setIsAdmin(response.data.isAdmin);
        setIsLoggedIn(true);
      }
    } catch (error) {
      showMessage(error,true);
      setIsLoggedIn(false);
    }
  };

  // Only run getUserDetails after first render once
  useEffect(() => {
    getUserDetails();
  }, [path]); // Only once when Layout mounts

  const showUserHeader =
    isLoggedIn &&
    !isAdmin &&
    userHeaderRoute.includes(path);

  const showMainHeader =
    isLoggedIn === false || mainHeaderRoute.includes(path); 

  const showAdminHeader =
    isLoggedIn &&
    (!showUserHeader && !showMainHeader)

  return (
    <>
      {showMainHeader && <MainHeader />}
      {showAdminHeader && <AdminHeader />}
      {showUserHeader && <Header />}
      {children}
    </>
  );
};

export default Layout;