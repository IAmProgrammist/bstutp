import React from 'react';
import {connect} from "react-redux";
import * as LoginFuncs from "../slices/loginReducer"
import * as MainlistFuncs from "../slices/mainListReducer"
import * as TestFuncs from "../slices/testReducer"
import {Route, BrowserRouter as Router, Routes} from "react-router-dom";
import LoginRegisterScreen from "./LoginRegisterScreen";
import style from '../assets/root.css'
import FetchScreen from "../elements/FetchScreen";
import {setErrorMail, setErrorPassword} from "../slices/loginReducer";
import Header from "../elements/Header";
import List from "./List";
import Test from "./Test"

let App = props => {
    return <div>
        <FetchScreen isFetching={props.loginData.isFetching || props.mainlistData.isFetching || props.testData.isFetching}/>
        <Router>
            <Routes>
                <Route exact path="/" element={<Header showBackButton={true} showAddButton={true} showExportButton={true}
                                                       userInfo={"Пахомов В.А."} onUserClick={() => console.log("eheh")}
                                                       onBackClicked={() => {console.log("back")}} onAddClicked={() => {console.log("add")}} onExportClicked={() => console.log("export")}/>}/>
                <Route exact path="/login" element={<div className="container">
                    <LoginRegisterScreen
                        loginData={props.loginData}
                        loginFuncs={props.loginFuncs}
                        setListFetching={props.mainlistFuncs.setListFetching}
                        setListData={props.mainlistFuncs.setListData}/>
                </div>}/>
                <Route exact path="/tests" element={
                    <List
                        mainlistData={props.mainlistData}
                        mainlistFuncs={props.mainlistFuncs}
                        setTestData={props.testFuncs.setTestData}
                        />
                }/>
                <Route exact path="/test" element={
                    <Test
                        testData={props.testData}
                        testFuncs={props.testFuncs}
                        mainlistData={props.mainlistData}
                        />
                }/>
            </Routes>
        </Router>

    </div>
}

let mapStateToProps = (state) => {
    return {
        loginData: state.login,
        mainlistData: state.mainlist,
        testData: state.test
    }
}

let mapDispatchToProps = (dispatch) => {
    return {
        loginFuncs: {
            changeLogin: (text) => dispatch(LoginFuncs.changeLogin(text)),
            changePassword: (text) => dispatch(LoginFuncs.changePassword(text)),
            loginRegisterFetching: (isFetching) => dispatch(LoginFuncs.loginRegisterFetching(isFetching)),
            setErrorLogin: () => dispatch(LoginFuncs.setErrorLogin()),
            changeRememberMe: (val) => dispatch(LoginFuncs.changeRememberMe(val))
        },
        mainlistFuncs: {
            setListFetching: (ev) => dispatch(MainlistFuncs.setListFetching(ev)),
            setListData: (data) => dispatch(MainlistFuncs.setListData(data))
        },
        testFuncs: {
            setTestData: (ev) => dispatch(TestFuncs.setTestData(ev)),
            setTestFetching: (fetching) => dispatch(TestFuncs.setTestFetching(fetching)),
            setTestAnswer: (setAnswer) => dispatch(TestFuncs.setTestAnswer(setAnswer))
        }
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(App);
