import { useEffect } from "react";
import { useLocation } from "react-router-dom"
import { toast } from "react-toastify";
import Swal from "sweetalert2";

const NotifCleaner = () => {
    const location = useLocation();

    useEffect(() => {
        Swal.close();
        toast.dismiss();
    },[location.pathname]);

    return null;
}

export default NotifCleaner;