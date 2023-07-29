import React, {useEffect} from 'react';
import Header from '../elements/Header'
import {useNavigate} from "react-router-dom";
import style from "../assets/test.css"

let Test = props => {
    let baseURL = window.location.origin;
    const navigate = useNavigate();

    let {
        name,
        duration,
        discipline,
        tests,
        state,
        isFetching
    } = props.testData;

    let {
        setTestFetching, setTestData, setTestAnswer
    } = props.testFuncs;

    let {
        userType,
        active,
        completed,
        draft,
        currentSlot,
        userName,
        userSurname,
        userPatronymic,
        headerName
    } = props.mainlistData;

    return <div>
        <Header showBackButton={true} showAddButton={false} showExportButton={userType !== "student"}
                userInfo={headerName} onUserClick={() => {
            navigate("/profile");
        }} onBackClicked={() => {
            navigate("/tests");
        }}/>

        <section className={"test"}>
            <div className={"container"}>
                <div className={"test__available_title"}>{name}</div>
                <div className={"test__available_sub"}>{discipline + " • " + Math.floor(duration / 60 / 10) + Math.floor(duration / 60) % 10 +
                    " мин. " + Math.floor(duration % 60 / 10) + duration % 60 % 10 + " сек."}</div>
                <button className={"button test__available_button"} onClick={(ev) => {
                    ev.preventDefault();
                }}>Начать</button>
            </div>
        </section>
    </div>
}

export default Test;