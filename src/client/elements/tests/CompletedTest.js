import React, {useEffect} from 'react';
import Header from '../Header'
import {useNavigate} from "react-router-dom";
import Score from "../Score"
import {TagPicker} from 'rsuite';

let CompletedTest = props => {
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

export default CompletedTest;