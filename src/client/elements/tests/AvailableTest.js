import React, {useEffect} from 'react';
import Header from '../Header'
import {useNavigate} from "react-router-dom";
import Score from "../Score"
import {TagPicker} from 'rsuite';

let AvailableTest = props => {
    let baseURL = window.location.origin;
    const navigate = useNavigate();

    let {
        name,
        endTime,
        duration,
        discipline,
        tasks,
        state,
        isFetching,
        isFetchingError,
        timerID,
        timerVal,
        inputDataTimerID,
        score
    } = props.testData;

    let {
        setTestFetching,
        setTestData,
        setTestAnswer,
        clearTimerID,
        setTimerID,
        updateTimerVal,
        setTestFetchError,
        setInputTimerID
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

    let {
        setListFetching, setUserData
    } = props.mainlistFuncs;

    clearTimerID();

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
                <div
                    className={"test__available_sub"}>{discipline + " • " + Math.floor(duration / 60 / 10) + Math.floor(duration / 60) % 10 +
                    " мин. " + Math.floor(duration % 60 / 10) + duration % 60 % 10 + " сек."}</div>
                <button className={"button test__available_button"} onClick={(ev) => {
                    setListFetching(true);

                    fetch(baseURL + "/api/tests/start", {
                        method: 'POST',
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        credentials: "include",
                        body: JSON.stringify({idTest: +new URL(window.location.href).searchParams.get("id_test")})
                    })
                        .then(res => {
                            setListFetching(false);

                            if (res.ok) {
                                setListFetching(true);

                                fetch(baseURL + "/api/tests/test?id_test=" + new URL(window.location.href).searchParams.get("id_test"), {
                                    method: 'GET',
                                    headers: {
                                        'Accept': 'application/json',
                                        'Content-Type': 'application/json'
                                    },
                                    credentials: "include"
                                })
                                    .then(res => {
                                        setListFetching(false);

                                        return res.json();
                                    })
                                    .then(res => {
                                        setListFetching(false);
                                        setUserData(res);
                                        setTestData(res);
                                    })
                                    .catch(res => {
                                        setListFetching(false);
                                        console.error("Vlad please fix this, something really bad happened");
                                        console.error(res.stack);
                                    })
                            }

                            return res.json();
                        })
                        .then(res => {
                        })
                        .catch(res => {
                            setListFetching(false);
                            console.error("Vlad please fix this, something really bad happened");
                            console.error(res.stack);
                        })
                }}>Начать
                </button>
            </div>
        </section>
    </div>
}

export default AvailableTest;