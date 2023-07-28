import React from 'react';
import { CSSProperties } from "react";
import BeatLoader from "react-spinners/BeatLoader";
import style from '../assets/fetchScreen.css'

let LoginRegisterScreen = props => {
    let {isFetching} = props;

    return <div className={"fetchScreen " + (isFetching ? "fetchScreen_fetching" : "")}>
        <BeatLoader
            loading={isFetching}
            color={"#1E90FFFF"}
        />
    </div>
}

export default LoginRegisterScreen;