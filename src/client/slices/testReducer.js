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
    score: 0.0
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
        }
    }
});

export const {
    setTestFetching, setTestData, setTestAnswer, clearTimerID, setTimerID, updateTimerVal, setTestFetchError, setInputTimerID
} = testSlice.actions;

export default testSlice.reducer;