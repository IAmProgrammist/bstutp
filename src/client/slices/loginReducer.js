import {createSlice} from "@reduxjs/toolkit";

const initialState = {
    mail: "",
    login: "",
    rememberMe: false,
    loginFieldError: false,
    isFetching: false
};

const loginSlice = createSlice({
    name: "login",
    initialState,
    reducers: {
        changeLogin: (state, action) => {
            state.login = action.payload;
        },
        changePassword: (state, action) => {
            state.password = action.payload;
        },
        changeRememberMe: (state, action) => {
            state.rememberMe = action.payload;
        },
        loginRegisterFetching: (state, action) => {
            if (action.payload) {
                state.loginFieldError = false;
            }
            state.isFetching = action.payload;
        },
        setErrorLogin: (state) => {
            state.loginFieldError = true;
        }
    }
});

export const {
    changeLogin, changePassword,
    changeRememberMe, loginRegisterFetching, setErrorLogin
} = loginSlice.actions;

export default loginSlice.reducer;