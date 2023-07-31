import React, {useEffect} from 'react';
import {useNavigate} from "react-router-dom";
import style from "../assets/test.css"

import AvailableTest from "../elements/tests/AvailableTest"
import RunningTest from "../elements/tests/RunningTest"
import CompletedTest from "../elements/tests/CompletedTest"
import EditableTest from "../elements/tests/EditableTest"

let Test = props => {

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

    useEffect(() => {
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

                if (!res.ok)
                    navigate("/tests");

                return res.json();
            })
            .then(res => {
                setListFetching(false);

                setTestData(res);
                setUserData(res);
            })
            .catch(res => {
                setListFetching(false);
                console.error("Vlad please fix this, something really bad happened");
                console.error(res.stack);
            })
    }, []);

    if (state === "available") {
        return <AvailableTest testData={props.testData} testFuncs={props.testFuncs} mainlistData={props.mainlistData}
                              mainlistFuncs={props.mainlistFuncs}/>
    } else if (state === "running") {
        return <RunningTest testData={props.testData} testFuncs={props.testFuncs} mainlistData={props.mainlistData}
                              mainlistFuncs={props.mainlistFuncs}/>
    } else if (state === "completed") {
        return <CompletedTest testData={props.testData} testFuncs={props.testFuncs} mainlistData={props.mainlistData}
                            mainlistFuncs={props.mainlistFuncs}/>
    } else if (state === "draft") {
        return <EditableTest testData={props.testData} testFuncs={props.testFuncs} mainlistData={props.mainlistData}
                              mainlistFuncs={props.mainlistFuncs}/>
    }
}

export default Test;