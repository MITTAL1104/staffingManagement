import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { fetchUserDetails } from "../../api/api";

const PrivateRoute = ({ children, adminOnly = false }) => {
    const navigate = useNavigate();
    const [isAuthorized, setIsAuthorized] = useState(false);
    const [isLoading,setIsLoading] = useState(true);

    useEffect(() => {
        const checkUserAuth = async() => {
            try{
                const response = await fetchUserDetails();

                if(response){
                    if(adminOnly && !response.data.isAdmin){
                        navigate("/unauthorized");
                    } else{
                        setIsAuthorized(true);
                    }
                } else{
                    navigate("/")
                }
            } catch(error){
                navigate("/")
            } finally{
                setIsLoading(false);
            }
        };

        checkUserAuth();
    },[navigate,adminOnly]);

    if(isLoading){
    }

    return isAuthorized?children:null;
};

export default PrivateRoute;
