import React from 'react';
import {connect} from "react-redux";
import * as LoginFuncs from "../slices/loginReducer"
import {Route, BrowserRouter as Router, Routes} from "react-router-dom";
import LoginRegisterScreen from "./LoginRegisterScreen";
import style from '../assets/root.css'
import FetchScreen from "../elements/FetchScreen";
import {setErrorMail, setErrorPassword} from "../slices/loginReducer";

let App = props => {
    return <div>
        <FetchScreen isFetching={props.loginData.isFetching}/>
        <Router>
            <Routes>
                <Route exact path="/login" element={<div className="container">
                    <LoginRegisterScreen
                        loginData={props.loginData}
                        loginFuncs={props.loginFuncs}/>
                </div>}/>
            </Routes>
        </Router>

    </div>
}

let mapStateToProps = (state) => {
    return {
        loginData: state.login
    }
}

let mapDispatchToProps = (dispatch) => {
    return {
        loginFuncs: {
            changeLogin: (text) => dispatch(LoginFuncs.changeLogin(text)),
            changePassword: (text) => dispatch(LoginFuncs.changePassword(text)),
            loginRegisterFetching: (isFetching) => dispatch(LoginFuncs.loginRegisterFetching(isFetching)),
            setErrorLogin: () => dispatch(LoginFuncs.setErrorLogin()),
        }
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(App);
