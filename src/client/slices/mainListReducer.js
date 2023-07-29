import {createSlice} from "@reduxjs/toolkit";

const initialState = {
    userType: "student",
    active: {
        currentPage: 0,
        maxPages: 1,
        data: []
    },
    completed: {
        currentPage: 0,
        maxPages: 1,
        data: []
    },
    draft: {
        currentPage: 0,
        maxPages: 1,
        data: []
    },
    isFetching: false,
    currentSlot: "active",
    userName: "",
    userSurname: "",
    userPatronymic: null,
    headerName: ""
};

const mainlistSlice = createSlice({
    name: "mainlist",
    initialState,
    reducers: {
        setListFetching: (state, payload) => {
            state.isFetching = payload;
        },
        setListData: (state, payload) => {
            state.userName = payload.userInfo.name;
            state.userSurname = payload.userInfo.surname;
            state.userPatronymic = payload.userInfo.patronymic;
            state.userType = payload.userInfo.userType;

            state.headerName = state.userSurname + " " +
                state.userName[0] + "." + (state.userPatronymic ? " " + state.userPatronymic[0] + "." : "");


            switch (payload.type) {
                case 'tests_completed_students':
                    state.currentSlot = "completed";
                    break;
                case 'tests_active_teacher':
                case 'tests_active_students':
                    state.currentSlot = "active";
                    break;
                case 'tests_draft_teacher':
                    state.currentSlot = "draft";
                    break;
            }

            state[state.currentSlot].maxPages = payload.tests.totalPages;
            state[state.currentSlot].currentPage = payload.tests.currentPage;
            state[state.currentSlot].data = [...payload.tests.data];
        }
    }
});

export const {
    setListFetching, setListData
} = mainlistSlice.actions;

export default mainlistSlice.reducer;