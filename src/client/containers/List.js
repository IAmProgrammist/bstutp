import React, {useEffect} from 'react';
import style from '../assets/list.css'
import Header from '../elements/Header'
import {useNavigate} from "react-router-dom";

let List = props => {
    let baseURL = window.location.origin;
    const navigate = useNavigate();

    let {
        userType,
        active,
        completed,
        draft,
        isFetching,
        currentSlot,
        userName,
        userSurname,
        userPatronymic,
        headerName
    } = props.mainlistData;

    let {
        setListFetching, setListData
    } = props.mainlistFuncs;

    let {
        setTestData
    } = props;

    useEffect(() => {
            setListFetching(true);

            fetch(baseURL + "/api/tests/list", {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                credentials: "include"
            })
                .then(res => {
                    setListFetching(false);
                    if (res.ok)
                        navigate("/tests");
                    else
                        navigate("/login");

                    return res.json();
                })
                .then(res => {
                    setListFetching(false);

                    setListData(res);
                })
                .catch(res => {
                    setListFetching(false);
                    console.error("Vlad please fix this, something really bad happened");
                    console.error(res.stack);
                })
        }, []
    );


    const renderNav = () => {
        if (userType === "student") {
            return <div className={"list__navigator_items"}>
                <ul className={"list__navigator_items"}>
                    <li className={"list__navigatoritem " + (currentSlot === "active" ? "list__navigatoritem_state_active" : "")}>Активные
                        тесты
                    </li>
                    <li className={"list__navigatoritem " + (currentSlot === "completed" ? "list__navigatoritem_state_active" : "")}>Результаты</li>
                </ul>
                <hr/>
            </div>
        } else if (userType === "teacher") {
            return <div className={"list__navigator_items"}>
                <ul className={"list__navigator_items"}>
                    <li className={"list__navigatoritem " + (currentSlot === "active" ? "list__navigatoritem_state_active" : "")}>Активные
                        тесты
                    </li>
                    <li className={"list__navigatoritem " + (currentSlot === "draft" ? "list__navigatoritem_state_active" : "")}>Черновики</li>
                </ul>
                <hr/>
            </div>
        } else if (userType === "admin") {
            return <div className={"list__navigator_items"}>
                <ul className={"list__navigator_items"}>
                    <li className={"list__navigatoritem " + (currentSlot === "active" ? "list__navigatoritem_state_active" : "")}>Активные
                        тесты
                    </li>
                    <li className={"list__navigatoritem " + (currentSlot === "draft" ? "list__navigatoritem_state_active" : "")}>Черновики</li>
                    <li className={"list__navigatoritem " + (currentSlot === "admin" ? "list__navigatoritem_state_active" : "")}>Админка</li>
                </ul>
                <hr/>
            </div>
        }
    }

    let renderStudentTestsActive = (test) => {
        return <div>Unable to recohnize test type</div>
    };
    let testsArray = {};
    if (userType === "student" && currentSlot === "active") {
        testsArray = active;

        renderStudentTestsActive = (test) => {
            return <li className={"list__testitem"} onClick={(ev) => {
                setListFetching(true);

                fetch(baseURL + "/api/tests/test?id_test=" + test.id, {
                    method: 'GET',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    credentials: "include"
                })
                    .then(res => {
                        setListFetching(false);
                        if (res.ok)
                            navigate("/test?id_test=" + test.id);

                        return res.json();
                    })
                    .then(res => {
                        setListFetching(false);

                        setTestData(res);
                    })
                    .catch(res => {
                        setListFetching(false);
                        console.error("Vlad please fix this, something really bad happened");
                        console.error(res.stack);
                    })
            }}>
                <div className={"list__testitem_info"}>
                    <div className={"list__testitem_info_title"}>{test.name}</div>
                    <div
                        className={"list__testitem_info_sub"}>{test.discipline + " • " + Math.floor(test.duration / 60 / 10) + Math.floor(test.duration / 60) % 10 +
                        " мин. " + Math.floor(test.duration % 60 / 10) + test.duration % 60 % 10 + " сек."}</div>
                </div>
            </li>
        }
    }

    let mapper = (testss) => {
        if (testss)
            return testss.map(test => renderStudentTestsActive(test));
    }

    console.log(props);

    return <div>
        <Header showBackButton={false} showAddButton={userType !== "student"} showExportButton={false}
                userInfo={headerName} onUserClick={() => {
            navigate("/profile");
        }} onAddClicked={() => {
            console.log("Здесь мы будем добавлять тест!")
        }}/>

        <section>
            <div className={"container list__container"}>
                {renderNav()}
                <ul className={"list__tests"}>
                    {
                        mapper(testsArray.data)
                    }
                    <li className={"list__test"}></li>
                </ul>
            </div>
        </section>
    </div>
}

export default List;