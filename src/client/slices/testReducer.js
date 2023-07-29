import {createSlice} from "@reduxjs/toolkit";

const initialState = {
    name: "",
    duration: 0,
    discipline: "",
    tests: [],
    state: "available", //"running", "ended"
    isFetching: false
};

const testSlice = createSlice({
    name: "test",
    initialState,
    reducers: {
        setTestFetching: (state, action) => {
            state.isFetching = action.payload;
        },
        setTestData: (state, action) => {
            state.state = action.payload.state;
            switch (state.state) {
                case "available":
                    state.name = action.payload.test.name;
                    state.duration = action.payload.test.duration;
                    state.discipline = action.payload.test.discipline;
            }
        },
        setTestAnswer: (state, action) => {

        }
    }
});

export const {
    setTestFetching, setTestData, setTestAnswer
} = testSlice.actions;

export default testSlice.reducer;