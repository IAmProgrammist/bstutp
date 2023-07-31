import {createSlice} from "@reduxjs/toolkit";

const initialState = {
    name: "",
    endTime: 0,
    duration: 0,
    discipline: "",
    tasks: [],
    state: "available", //"running", "ended"
    isFetching: false,
    timerID: null,
    timerVal: "00:00",
    isFetchingError: false,
    inputDataTimerID: null,
    score: 0.0,
    idDiscipline: null,
    disciplines: [],
    groups: [],
    groupsAll: [],
    indicators: []
};

const testSlice = createSlice({
    name: "test",
    initialState,
    reducers: {
        setTestFetching: (state, action) => {
            if (action.payload)
                state.isFetchingError = false;
            state.isFetching = action.payload;
        },
        setTestFetchError: (state, action) => {
            state.isFetchingError = action.payload;
        },
        setTestData: (state, action) => {
            state.state = action.payload.state;
            switch (state.state) {
                case "available":
                    state.name = action.payload.test.name;
                    state.duration = action.payload.test.duration;
                    state.discipline = action.payload.test.discipline;
                    break;
                case "running":
                    state.name = action.payload.test.name;
                    state.duration = action.payload.test.duration;
                    state.discipline = action.payload.test.discipline;
                    if (JSON.stringify(state.tasks) !== JSON.stringify(action.payload.test.tasks))
                        state.tasks = action.payload.test.tasks;
                    state.endTime = action.payload.test.completionTime;
                    let duration = (state.endTime - new Date().getTime()) / 1000;
                    state.timerVal = new String(Math.floor(duration / 60 / 10)) + new String(Math.floor(duration / 60) % 10) +
                        ":" + new String(Math.floor(Math.floor(duration) % 60 / 10)) + new String(Math.floor(duration) % 60 % 10);
                    break;
                case "completed":
                    state.name = action.payload.test.name;
                    state.duration = action.payload.test.duration;
                    state.discipline = action.payload.test.discipline;
                    if (JSON.stringify(state.tasks) !== JSON.stringify(action.payload.test.tasks))
                        state.tasks = action.payload.test.tasks;
                    state.endTime = action.payload.test.completionTime;
                    state.score = action.payload.test.result;
                    break;
                case "draft":
                    if (state.name !== action.payload.test.name)
                        state.name = action.payload.test.name;
                    if (state.duration !== action.payload.test.duration)
                        state.duration = action.payload.test.duration;
                    state.discipline = action.payload.test.discipline;
                    if (state.idDiscipline !== action.payload.test.idDiscipline)
                        state.idDiscipline = action.payload.test.idDiscipline;
                    if (JSON.stringify(state.tasks) !== JSON.stringify(action.payload.test.tasks))
                        state.tasks = action.payload.test.tasks;
                    state.disciplines = action.payload.test.disciplines;
                    if (JSON.stringify(state.groups) !== JSON.stringify(action.payload.test.groups))
                        state.groups = action.payload.test.groups;
                    state.groupsAll = action.payload.test.groupsAll;
                    state.indicators = action.payload.test.indicators;

                    break;
            }
        },
        setTestAnswer: (state, action) => {
            let taskIndex = state.tasks.findIndex((item) => item.id == action.payload.taskId);
            if (taskIndex || taskIndex !== -1) {
                if (state.tasks[taskIndex].taskType === "text")
                    state.tasks[taskIndex].answer = action.payload.data;
                else if (state.tasks[taskIndex].taskType === "one_in_many")
                    state.tasks[taskIndex].idAnswer = action.payload.data;
            }
        },
        clearTimerID: (state) => {
            clearInterval(state.timerID);
            state.timerID = null;
        },
        setTimerID: (state, action) => {
            clearInterval(state.timerID);
            state.timerID = action.payload;
        },
        updateTimerVal: (state) => {
            let duration = (state.endTime - new Date().getTime()) / 1000;
            state.timerVal = new String(Math.floor(duration / 60 / 10)) + new String(Math.floor(duration / 60) % 10) +
                ":" + new String(Math.floor(Math.floor(duration) % 60 / 10)) + new String(Math.floor(duration) % 60 % 10);
        },
        setInputTimerID: (state, action) => {
            state.inputDataTimerID = action.payload;
        },
        setTestName: (state, action) => {
            state.name = action.payload;
        },
        setGroups: (state, action) => {
            state.groups = action.payload;
        },
        setDiscipline: (state, action) => {
            state.idDiscipline = action.payload;
        },
        setDuration: (state, action) => {
            state.duration = new Number(action.payload.split(":")[0]) * 3600 +
                new Number(action.payload.split(":")[1]) * 60 +
                new Number(action.payload.split(":")[2]);
        },
        setTaskAnswer: (state, action) => {
            let taskIndex = state.tasks.findIndex((item) => item.id == action.payload.taskId);
            if (taskIndex || taskIndex !== -1) {
                if (state.tasks[taskIndex].taskType === "text")
                    state.tasks[taskIndex].answerCorrect = action.payload.data;
                else if (state.tasks[taskIndex].taskType === "one_in_many")
                    state.tasks[taskIndex].idAnswerCorrect = action.payload.data;
            }
        },
        setTaskDescription: (state, action) => {
            let taskIndex = state.tasks.findIndex((item) => item.id == action.payload.taskId);
            if (taskIndex || taskIndex !== -1) {
                state.tasks[taskIndex].description = action.payload.data;
            }
        },
        setIndicators: (state, action) => {
            let taskIndex = state.tasks.findIndex((item) => item.id === action.payload.taskId);
            if (taskIndex || taskIndex !== -1) {
                state.tasks[taskIndex].indicators = action.payload.data;
            }
        },
        changeRadioButtonVal: (state, action) => {
            let taskIndex = state.tasks.findIndex((item) => item.id === action.payload.taskId);
            if (taskIndex || taskIndex !== -1) {
                let questionIndex = state.tasks[taskIndex].questionPull.findIndex((item) => item.id === action.payload.questionId);
                if (questionIndex || questionIndex !== -1) {
                    state.tasks[taskIndex].questionPull[questionIndex].text = action.payload.data;
                }
            }
        }
    }
});

export const {
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
    setTaskAnswer,
    setTaskDescription,
    setIndicators,
    changeRadioButtonVal
} = testSlice.actions;

export default testSlice.reducer;