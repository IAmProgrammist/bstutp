import {createSlice} from "@reduxjs/toolkit";

const initialState = {
    getMessage: ""
};

const baseSlice = createSlice({
    name: "base",
    initialState,
    reducers: {
        makeGet: (state, action) => {
            state.getMessage = action.payload;
        }
    }
});

export const {
    makeGet
} = baseSlice.actions;

export default baseSlice.reducer;