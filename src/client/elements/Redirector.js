import React from 'react';
import {useNavigate} from "react-router-dom";

let Redirector = props => {
    const navigate = useNavigate();

    navigate("/tests");
}

export default Redirector;