import React, {useEffect} from 'react';
import Header from '../elements/Header'
import {useNavigate} from "react-router-dom";
import style from "../assets/test.css"
import Score from "../elements/Score"

let Test = props => {

    //TODO: this might look terrible, but most of this code is duplicate.
    //caused by lack of time

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

    useEffect(() => {
        clearTimeout(inputDataTimerID);
        setInputTimerID(setTimeout(() => {
            clearTimeout(inputDataTimerID);
            setTestFetching(true);
            setInputTimerID(null);

            let updates = {
                "tasks": tasks.map((task) => {
                    return task.taskType === "text" ? {
                            taskType: task.taskType,
                            idReportDetailed: task.idReportDetailed,
                            answer: task.answer
                        } :
                        task.taskType === "one_in_many" ? {
                            taskType: task.taskType,
                            idReportDetailed: task.idReportDetailed,
                            idAnswer: task.idAnswer
                        } : null;
                }).filter(el => el !== null)
            };

            fetch(baseURL + "/api/tests/update",
                {
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    credentials: "include",
                    body: JSON.stringify(updates)
                })
                .then(res => {
                    setTestFetching(false);
                    res.json();
                })
                .then(res => {
                    setTestFetching(true);

                    fetch(baseURL + "/api/tests/test?id_test=" + new URL(window.location.href).searchParams.get("id_test"), {
                        method: 'GET',
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        credentials: "include"
                    })
                        .then(res => {
                            setTestFetching(false);

                            return res.json();
                        })
                        .then(res => {
                            setTestFetching(false);
                            setUserData(res);
                            setTestData(res);
                        })
                        .catch(res => {
                            setTestFetching(false);
                            setTestFetchError(true);
                            console.error("Vlad please fix this, something really bad happened");
                            console.error(res.stack);
                        })
                })
                .catch(res => {
                    setTestFetching(false);
                    setTestFetchError(true);
                    console.error("Vlad please fix this, something really bad happened");
                    console.error(res.stack);
                });
        }, 3000));
    }, [tasks]);

    useEffect(() => {
        if (new Date().getTime() > endTime) {
            clearTimeout(inputDataTimerID);
            setInputTimerID(null);
            clearInterval(timerID);
            setTimerID(null);

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
        }
    }, [timerVal]);

    if (state === "available") {
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
    } else if (state === "running") {
        if (timerID === null) {
            let interval = setInterval(() => {
                updateTimerVal();
            }, 500);

            setTimerID(interval);
        }

        let tasksIterator = (tasks) => {
            if (tasks)
                return tasks.map((task) => {
                    if (task.taskType === "text") {
                        return <div className={"test__task test__task_type_text"} key={task.id}>
                            <div className={"test__task_description"}>{task.description}</div>
                            <input type={"text"} className={"test__answer"} placeholder={"Ответ"}
                                   onChange={(ev) => setTestAnswer(task.id, ev.target.value)}
                                   value={task.answer ? task.answer : ""}/>
                        </div>
                    } else if (task.taskType === "one_in_many") {
                        let radios = task.questionPull.map((question) => {
                            return <div>
                                <input type={"radio"} name={"one_in_many" + task.id}
                                       checked={question.id === task.idAnswer} onClick={
                                    (ev) => setTestAnswer(task.id, question.id)
                                } key={question.id}/>
                                <label htmlFor={"one_in_many" + task.id}>{question.text}</label>
                            </div>
                        });

                        return <div className={"test__task test__task_type_oneinmany"} key={task.id}>
                            <div className={"test__task_description"}>{task.description}</div>
                            <div>
                                {radios}
                            </div>
                        </div>
                    }
                });

            return <div></div>
        }

        return <div>
            <Header showBackButton={true} showAddButton={false} showExportButton={userType !== "student"}
                    userInfo={headerName} onUserClick={() => {
                navigate("/profile");
            }} onBackClicked={() => {
                navigate("/tests");
            }}/>

            <section className={"test"}>
                <div className={"test__timer"}>
                    {timerVal}
                </div>
                <div className={"test__fetch_status"}>
                    {inputDataTimerID !== null ? <div>Вы печатаете</div> :
                        isFetchingError ? <div>Ошибка загрузки</div> :
                            isFetching ? <div>Загрузка</div> :
                                <div>ОК</div>
                    }
                </div>
                <div className={"container"}>
                    <div className={"test__running_title"}>{name}</div>
                    {tasksIterator(tasks)}
                    <div className={"test__task_buttons"}>
                        <button className={"button test__task_button test__task_button_send"} onClick={() => {
                            fetch(baseURL + "/api/tests/finish", {
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
                        }}>Отправить</button>
                        <button className={"button test__task_button test__task_button_save"} onClick={() => {
                            setTestFetching(true);
                            setInputTimerID(null);

                            let updates = {
                                "tasks": tasks.map((task) => {
                                    return task.taskType === "text" ? {
                                            taskType: task.taskType,
                                            idReportDetailed: task.idReportDetailed,
                                            answer: task.answer
                                        } :
                                        task.taskType === "one_in_many" ? {
                                            taskType: task.taskType,
                                            idReportDetailed: task.idReportDetailed,
                                            idAnswer: task.idAnswer
                                        } : null;
                                }).filter(el => el !== null)
                            };

                            fetch(baseURL + "/api/tests/update",
                                {
                                    method: 'POST',
                                    headers: {
                                        'Accept': 'application/json',
                                        'Content-Type': 'application/json'
                                    },
                                    credentials: "include",
                                    body: JSON.stringify(updates)
                                })
                                .then(res => {
                                    setTestFetching(false);
                                    res.json();
                                })
                                .then(res => {
                                    setTestFetching(true);

                                    fetch(baseURL + "/api/tests/test?id_test=" + new URL(window.location.href).searchParams.get("id_test"), {
                                        method: 'GET',
                                        headers: {
                                            'Accept': 'application/json',
                                            'Content-Type': 'application/json'
                                        },
                                        credentials: "include"
                                    })
                                        .then(res => {
                                            setTestFetching(false);

                                            return res.json();
                                        })
                                        .then(res => {
                                            setTestFetching(false);
                                            setUserData(res);
                                            setTestData(res);
                                        })
                                        .catch(res => {
                                            setTestFetching(false);
                                            setTestFetchError(true);
                                            console.error("Vlad please fix this, something really bad happened");
                                            console.error(res.stack);
                                        })
                                })
                                .catch(res => {
                                    setTestFetching(false);
                                    setTestFetchError(true);
                                    console.error("Vlad please fix this, something really bad happened");
                                    console.error(res.stack);
                                });
                        }}>Сохранить</button>
                    </div>
                </div>
            </section>
        </div>
    } else if (state === "completed") {
        // TODO: terrible! You should use components!
        let tasksIterator = (tasks) => {
            if (tasks)
                return tasks.map((task) => {
                        if (task.taskType === "text") {
                            if (task.answer !== task.answerCorrect) {
                                return <div className={"test__task test__task_type_text"} key={task.id}>
                                    <div className={"test__task_description"}>{task.description}</div>
                                    Ваш ответ: <input type={"text"} className={"test__answer test__answer_fail"}
                                                      value={task.answer ? task.answer : ""}
                                                      readOnly={true}/>
                                    Правильный ответ: <input type={"text"} className={"test__answer test__answer_correct"}
                                                             value={task.answer ? task.answerCorrect : ""}
                                                             readOnly={true}/>
                                </div>
                            } else {
                                return <div className={"test__task test__task_type_text"} key={task.id}>
                                    <div className={"test__task_description"}>{task.description}</div>
                                    Ваш ответ: <input type={"text"} className={"test__answer test__answer_correct"}
                                                      value={task.answer ? task.answer : ""}
                                                      readOnly={true}/>
                                </div>
                            }
                        } else if (task.taskType === "one_in_many") {
                            let radios = task.questionPull.map((question) => {
                                return <div>
                                    <input type={"radio"} name={"one_in_many" + task.id}
                                           checked={question.id === task.idAnswer} readOnly={true}
                                           key={question.id}/>
                                    <label htmlFor={"one_in_many" + task.id}
                                           className={(task.idAnswerCorrect === question.id ? "test__answer_correct" :
                                               (question.id == task.idAnswer ? "test__answer_fail" : ""))}>
                                        {question.text}
                                    </label>
                                </div>
                            });

                            return <div className={"test__task test__task_type_oneinmany"} key={task.id}>
                                <div className={"test__task_description"}>{task.description}</div>
                                <div>
                                    {radios}
                                </div>
                            </div>
                        }
                    }
                )

            return <div></div>
        }

        return <div>
            <Header showBackButton={true} showAddButton={false} showExportButton={userType !== "student"}
                    userInfo={headerName} onUserClick={() => {
                navigate("/profile");
            }} onBackClicked={() => {
                navigate("/tests");
            }}/>

            <section className={"test"}>
                <div className={"container"}>
                    <div className={"test__running_title"}>{name}<Score score={score} width={60} height={30}
                                                                        fontSize={20}/></div>
                    {tasksIterator(tasks)}
                </div>
            </section>
        </div>
    }
}

export default Test;