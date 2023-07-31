import React, {useEffect} from 'react';
import {useNavigate} from "react-router-dom";

let Redirector = props => {
    const navigate = useNavigate();

    useEffect(() => {
        navigate("/tests");
    }, [])
}

export default Redirector;