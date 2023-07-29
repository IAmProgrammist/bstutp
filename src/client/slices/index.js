import loginReducer from "./loginReducer"
import {configureStore} from "@reduxjs/toolkit";
import mainlistReducer from "./mainListReducer"

export default configureStore({
        reducer: {
            login: loginReducer,
            mainlist: mainlistReducer
        }
    }
)