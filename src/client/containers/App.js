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
import Score from "../elements/Score"

let App = props => {
    return <div>
        <FetchScreen isFetching={props.loginData.isFetching || props.mainlistData.isFetching}/>
        <Router>
            <Routes>
                <Route exact path="/" element={<Score score={1} width={200} height={100} fontSize={70}/>}/>
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
                        mainlistFuncs={props.mainlistFuncs}
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
            setListData: (data) => dispatch(MainlistFuncs.setListData(data)),
            setUserData: (data) => dispatch(MainlistFuncs.setUserData(data)),
            changeSlot: (slot) => dispatch(MainlistFuncs.changeSlot(slot))
        },
        testFuncs: {
            setTestData: (ev) => dispatch(TestFuncs.setTestData(ev)),
            setTestFetching: (fetching) => dispatch(TestFuncs.setTestFetching(fetching)),
            setTestAnswer: (taskId, answer) => dispatch(TestFuncs.setTestAnswer({data: answer, taskId})),
            clearTimerID: () => dispatch(TestFuncs.clearTimerID()),
            setTimerID: (timerID) => dispatch(TestFuncs.setTimerID(timerID)),
            updateTimerVal: () => dispatch(TestFuncs.updateTimerVal()),
            setTestFetchError: (val) => dispatch(TestFuncs.setTestFetchError(val)),
            setInputTimerID: (val) => dispatch(TestFuncs.setInputTimerID(val))
        }
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(App);
