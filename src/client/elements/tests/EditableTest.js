import React, {useEffect} from 'react';
import Header from '../Header'
import {useNavigate} from "react-router-dom";
import Score from "../Score"
import {TagPicker} from 'rsuite';
import TimePicker from 'react-time-picker';
import 'react-time-picker/dist/TimePicker.css';
import 'react-clock/dist/Clock.css';

let EditableTest = props => {
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
        score,
        groupsAll,
        groups,
        idDiscipline,
        disciplines,
        indicators
    } = props.testData;

    let {
        setTestFetching,
        setTestData,
        setTestAnswer,
        clearTimerID,
        setTimerID,
        updateTimerVal,
        setTestFetchError,
        setInputTimerID,
        setTestName,
        setGroups,
        setDiscipline,
        setDuration,
        setTaskDescription,
        setTaskAnswer,
        setIndicators,
        changeRadioButtonVal
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
        clearTimeout(inputDataTimerID);
        setInputTimerID(setTimeout(() => {
            clearTimeout(inputDataTimerID);
            setTestFetching(true);
            setInputTimerID(null);

            let updates = {
                tasks: tasks.map((task) => {
                    return task.taskType === "text" ? {
                            taskId: task.id,
                            description: task.description,
                            taskType: task.taskType,
                            answerCorrect: task.answerCorrect,
                            indicators: task.indicators
                        } :
                        task.taskType === "one_in_many" ? {
                            taskId: task.id,
                            description: task.description,
                            taskType: task.taskType,
                            idAnswerCorrect: task.idAnswerCorrect,
                            indicators: task.indicators,
                            questionPull: task.questionPull
                        } : null;
                }).filter(el => el !== null),
                name, duration, groups, idDiscipline, idTest: new URL(window.location.href).searchParams.get("id_test")
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
    }, [tasks, name, duration, groups, idDiscipline]);

    let tasksIterator = (tasks) => {
        if (tasks)
            return tasks.map((task) => {
                return <div className={"test__task test__task_type_text"} key={task.id}>
                    <div className={"test__task_label"}>Описание:</div>
                    <textarea className={"test__task_textarea"}
                              onChange={(ev) => setTaskDescription(task.id, ev.target.value)}
                              value={task.description}></textarea>
                    {task.taskType === "text" ? (
                        <div>
                            <div className={"test__task_label"}>Ответ:</div>
                            <input className={"test__task_answer"} value={task.answerCorrect ? task.answerCorrect : ""}
                                   onChange={(ev) => setTaskAnswer(task.id, ev.target.value)}></input>
                        </div>
                    ) : task.taskType === "one_in_many" ?
                        <div className={"test__task_one_in_many"}>
                            <div className={"test__task_label"}>Варианты ответов:</div>

                            {task.questionPull.map((question) =>
                                <div className={"test__task_question"}>
                                    <input type={"radio"} name={"one_in_many" + task.id}
                                           checked={question.id === task.idAnswerCorrect} onClick={
                                        (ev) => setTaskAnswer(task.id, question.id)
                                    } key={question.id}/>
                                    <label htmlFor={"one_in_many" + task.id}><input type={"text"}
                                                                                    value={question.text}
                                    onChange={(ev) => changeRadioButtonVal(task.id, question.id, ev.target.value)}/>
                                        <button className={"button test__task_delete_variant"} onClick={() => {
                                            clearTimeout(inputDataTimerID);
                                            setInputTimerID(null);
                                            setListFetching(true);

                                            fetch(baseURL + "/api/tests/tasks/oneinmany/delete", {
                                                method: 'POST',
                                                headers: {
                                                    'Accept': 'application/json',
                                                    'Content-Type': 'application/json'
                                                },
                                                credentials: "include",
                                                body: JSON.stringify({
                                                    idTest: +new URL(window.location.href).searchParams.get("id_test"),
                                                    idTask: task.id,
                                                    idQuestion: question.id
                                                })
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
                                        }}>Удалить
                                        </button>
                                    </label>
                                </div>
                            )}
                            <button className={"button test__task_add_variant"} onClick={() => {
                                clearTimeout(inputDataTimerID);
                                setInputTimerID(null);
                                setListFetching(true);

                                fetch(baseURL + "/api/tests/tasks/oneinmany/create", {
                                    method: 'POST',
                                    headers: {
                                        'Accept': 'application/json',
                                        'Content-Type': 'application/json'
                                    },
                                    credentials: "include",
                                    body: JSON.stringify({
                                        idTest: +new URL(window.location.href).searchParams.get("id_test"),
                                        idTask: task.id
                                    })
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
                            }}>Добавить
                            </button>
                        </div>

                        :
                        <div>Unrecognizeable</div>
                    }
                    <div className={"test__task_label"}>Индикаторы:</div>
                    <TagPicker placeholder={"Выбрать индикаторы"} style={{
                        width: "300px", borderRadius: "20px",
                        padding: "10px",
                        backgroundColor: "white",
                        borderWidth: "1px",
                        borderColor: "#24292e",
                        borderWidth: "1px"
                    }} data={indicators.map(
                        item => {
                            return {
                                label: `УК-${item.competenceId}.${item.subId} ${item.name}`, value: item.id
                            }
                        }
                    )} value={task.indicators} onChange={(values) => {
                        setIndicators(task.id, values)
                    }}/>
                    <button className={"button test__task_deletebutton"} onClick={() => {
                        clearTimeout(inputDataTimerID);
                        setInputTimerID(null);
                        setListFetching(true);

                        fetch(baseURL + "/api/tests/tasks/delete", {
                            method: 'POST',
                            headers: {
                                'Accept': 'application/json',
                                'Content-Type': 'application/json'
                            },
                            credentials: "include",
                            body: JSON.stringify({
                                idTest: +new URL(window.location.href).searchParams.get("id_test"),
                                idTask: task.id
                            })
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
                    }}>Удалить
                    </button>
                </div>

            });

        return <div></div>
    }

    return <div>
        <Header showBackButton={true} showAddButton={false} showExportButton={false}
                userInfo={headerName}
                onUserClick={() => {
                    navigate("/profile");
                }} onBackClicked={() => {
            navigate("/tests");
        }}/>

        <section className={"test"}>
            <div className={"test__fetch_status"}>
                {inputDataTimerID !== null ? <div>Вы печатаете</div> :
                    isFetchingError ? <div>Ошибка загрузки</div> :
                        isFetching ? <div>Загрузка</div> :
                            <div>ОК</div>
                }
            </div>
            <div className={"container"}>
                <div className={"test__task"}>
                    <div className={"test__task_label"}>Название теста:</div>
                    <input className={"test__task_input"} type={"text"} value={name}
                           onChange={(ev) => setTestName(ev.target.value)}/>
                    <div className={"test__task_label"}>Время выполнения:</div>
                    <TimePicker
                        value={"" + Math.floor(duration / 60 / 60 / 10) + Math.floor(duration / 60 / 60) % 10 + ":" + Math.floor(Math.floor(duration / 60) % 60 / 10) + Math.floor(duration / 60) % 60 % 10 +
                            ":" + Math.floor(duration % 60 / 10) + duration % 60 % 10} format={"HH:mm:ss"}
                        maxDetail={"second"}
                        disableClock={true} onChange={(ev) => setDuration(ev)}/>
                    <div className={"test__task_label"}>Группы:</div>
                    <div>
                        <TagPicker placeholder={"Выбрать группы"} style={{
                            width: "300px", borderRadius: "20px",
                            padding: "10px",
                            backgroundColor: "white",
                            borderWidth: "1px",
                            borderColor: "#24292e",
                            borderWidth: "1px"
                        }} data={groupsAll.map(
                            item => ({label: item.name, value: item.id})
                        )} value={groups} onChange={(values) => {
                            setGroups(values)
                        }}/>
                    </div>
                    <div className={"test__task_label"}>Дисциплина:</div>
                    <select className={"test__task_input"} value={idDiscipline}
                            onChange={(ev) => setDiscipline(ev.target.value)}>
                        {disciplines.map(discipline => {
                            return <option value={discipline.id}>{discipline.name}</option>
                        })}
                    </select>

                </div>

                {tasksIterator(tasks)}
                <div className={"test__task_buttons"}>
                    <button className={"button test__task_button test__task_button_send"} onClick={() => {
                    }}>Отправить
                    </button>
                    <button className={"button test__task_button test__task_button_save"} onClick={() => {
                        clearTimeout(inputDataTimerID);
                        setTestFetching(true);
                        setInputTimerID(null);

                        let updates = {
                            tasks: tasks.map((task) => {
                                return task.taskType === "text" ? {
                                        taskId: task.id,
                                        description: task.description,
                                        taskType: task.taskType,
                                        answerCorrect: task.answerCorrect,
                                        indicators: task.indicators
                                    } :
                                    task.taskType === "one_in_many" ? {
                                        taskId: task.id,
                                        description: task.description,
                                        taskType: task.taskType,
                                        idAnswerCorrect: task.idAnswerCorrect,
                                        indicators: task.indicators,
                                        questionPull: task.questionPull
                                    } : null;
                            }).filter(el => el !== null),
                            name, duration, groups, idDiscipline, idTest: new URL(window.location.href).searchParams.get("id_test")
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
                    }}>Сохранить
                    </button>
                    <button className={"button test__task_button"} onClick={() => {
                        clearTimeout(inputDataTimerID);
                        setInputTimerID(null);
                        setListFetching(true);

                        fetch(baseURL + "/api/tests/tasks/create", {
                            method: 'POST',
                            headers: {
                                'Accept': 'application/json',
                                'Content-Type': 'application/json'
                            },
                            credentials: "include",
                            body: JSON.stringify({
                                idTest: +new URL(window.location.href).searchParams.get("id_test"),
                                testType: "one_in_many"
                            })
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
                    }}>Добавить задание "один из"
                    </button>
                    <button className={"button test__task_button"} onClick={() => {
                        clearTimeout(inputDataTimerID);
                        setInputTimerID(null);
                        setListFetching(true);

                        fetch(baseURL + "/api/tests/tasks/create", {
                            method: 'POST',
                            headers: {
                                'Accept': 'application/json',
                                'Content-Type': 'application/json'
                            },
                            credentials: "include",
                            body: JSON.stringify({
                                idTest: +new URL(window.location.href).searchParams.get("id_test"),
                                testType: "text"
                            })
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
                    }}>Добавить задание открытого типа
                    </button>
                </div>
            </div>
        </section>
    </div>
}

export default EditableTest;