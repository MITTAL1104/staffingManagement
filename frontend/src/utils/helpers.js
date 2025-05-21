import { toast } from "react-toastify"

export const showMessage = (message,isError = false) => {
    isError? toast.error(message):toast.success(message);
}

export function preventBackNavigation() {
    window.history.pushState(null,"",window.location.href);
    window.onpopstate = () => {
        window.history.go(1);
    };

    return () => {
        window.onpopstate=null;
    }
}