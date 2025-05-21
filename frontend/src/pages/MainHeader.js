import React from "react";
import "../styles/userHeader.css";


const MainHeader = () => {

    return (
        <header>
            {/* Logo */}
            <div className="logo-container">
                <img src="/assets/staff.jpg" alt="Logo" className="logo"/>
                <span className="portal-title">Staff Management Portal</span>
            </div>
        </header>
    );
};

export default MainHeader;
