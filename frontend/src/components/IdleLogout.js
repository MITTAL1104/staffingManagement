import { useEffect, useRef } from "react";
import {userLogout } from "../api/api"; // assumes your logout API is setup

const useIdleLogout = (timeout = 15 * 60 * 1000) => {
  const timer = useRef(null);

  const resetTimer = () => {
    if (timer.current) clearTimeout(timer.current);
    timer.current = setTimeout(() => {
      handleLogout();
    }, timeout);
  };

  const handleLogout = async () => {
    try {
      await userLogout();
      window.location.href = "/"; 
    } catch (err) {
      console.error("Auto logout failed", err);
    }
  };

  useEffect(() => {
    const events = ["mousemove", "mousedown", "keypress", "scroll", "touchstart"];
    events.forEach((event) => window.addEventListener(event, resetTimer));

    resetTimer();

    return () => {
      events.forEach((event) => window.removeEventListener(event, resetTimer));
      if (timer.current) clearTimeout(timer.current);
    };
  });
};

export default useIdleLogout;
