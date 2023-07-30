import React, {useEffect} from 'react';
import style from '../assets/list.css'
import Header from '../elements/Header'
import Score from '../elements/Score'
import {useNavigate, useLocation} from "react-router-dom";

let List = props => {
    let baseURL = window.location.origin;
    const navigate = useNavigate();
    const location = useLocation();

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
        setListFetching, setListData, changeSlot
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
                    if (!res.ok)
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

    useEffect(() => {
            setListFetching(true);

            fetch(baseURL + "/api/tests/list?task_type=" + new URL(window.location.href).searchParams.get("task_type"), {
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
        }, [currentSlot]
    );


    const renderNav = () => {
        if (userType === "student") {
            return <div className={"list__navigator_items"}>
                <div className={"list__navigator_items"}>
                    <a href={"#"} onClick={(ev) => {
                        ev.preventDefault();
                        if (currentSlot !== "active") { navigate("/tests?task_type=active"); changeSlot("active");}
                    }}
                       className={"list__navigatoritem " + (currentSlot === "active" ? "list__navigatoritem_state_active" : "")}>Активные
                        тесты
                    </a>
                    <a href={"#"} onClick={(ev) => {
                        ev.preventDefault();
                        if (currentSlot !== "completed") {navigate("/tests?task_type=completed"); changeSlot("completed")}
                    }}
                       className={"list__navigatoritem " + (currentSlot === "completed" ? "list__navigatoritem_state_active" : "")}>Результаты</a>
                </div>
                <hr/>
            </div>
        } else if (userType === "teacher") {
            return <div className={"list__navigator_items"}>
                <div className={"list__navigator_items"}>
                    <a href={"#"} onClick={(ev) => {
                        ev.preventDefault();
                        if (currentSlot !== "active") { navigate("/tests?task_type=active"); changeSlot("active");}
                    }}
                       className={"list__navigatoritem " + (currentSlot === "active" ? "list__navigatoritem_state_active" : "")}>Активные
                        тесты
                    </a>
                    <a href={"#"} onClick={(ev) => {
                        ev.preventDefault();
                        if (currentSlot !== "draft") { navigate("/tests?task_type=draft"); changeSlot("draft");}
                    }}
                       className={"list__navigatoritem " + (currentSlot === "draft" ? "list__navigatoritem_state_active" : "")}>Черновики</a>
                </div>
                <hr/>
            </div>
        } else if (userType === "admin") {
            return <div className={"list__navigator_items"}>
                <div className={"list__navigator_items"}>
                    <a href={"#"} onClick={(ev) => {
                        ev.preventDefault();
                        if (currentSlot !== "active") { navigate("/tests?task_type=active"); changeSlot("active");}
                    }}
                       className={"list__navigatoritem " + (currentSlot === "active" ? "list__navigatoritem_state_active" : "")}>Активные
                        тесты
                    </a>
                    <a href={"#"} onClick={(ev) => {
                        ev.preventDefault();
                        if (currentSlot !== "draft") { navigate("/tests?task_type=draft"); changeSlot("draft");}
                    }}
                       className={"list__navigatoritem " + (currentSlot === "draft" ? "list__navigatoritem_state_active" : "")}>Черновики</a>
                    <a href={"#"} onClick={(ev) => {
                        ev.preventDefault();
                        if (currentSlot !== "admin") { navigate("/tests?task_type=admin"); changeSlot("admin");}
                    }}
                       className={"list__navigatoritem " + (currentSlot === "admin" ? "list__navigatoritem_state_active" : "")}>Админка</a>
                </div>
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
                navigate("/test?id_test=" + test.id);
            }}>
                <div className={"list__testitem_info"}>
                    <div className={"list__testitem_info_title"}>{test.name}</div>
                    <div
                        className={"list__testitem_info_sub"}>{test.discipline + " • " + Math.floor(test.duration / 60 / 10) + Math.floor(test.duration / 60) % 10 +
                        " мин. " + Math.floor(test.duration % 60 / 10) + test.duration % 60 % 10 + " сек."}</div>
                </div>
            </li>
        }
    } else if (userType === "student" && currentSlot === "completed") {
        testsArray = completed;

        renderStudentTestsActive = (test) => {
            return <li className={"list__testitem"} onClick={(ev) => {
                navigate("/test?id_test=" + test.id);
            }}>
                <div className={"list__testitem_info"}>
                    <div className={"list__testitem_info_title"}>{test.name}</div>
                    <div
                        className={"list__testitem_info_sub"}>{test.discipline + " • " + Math.floor(test.duration / 60 / 10) + Math.floor(test.duration / 60) % 10 +
                        " мин. " + Math.floor(test.duration % 60 / 10) + test.duration % 60 % 10 + " сек."}</div>
                </div>
                <Score score={test.result} width={60} height={30} fontSize={20}/>
            </li>
        }
    }

    let mapper = (testss) => {
        if (testss)
            return testss.map(test => renderStudentTestsActive(test));
    }

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