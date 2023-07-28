import loginReducer from "./loginReducer"
import {configureStore} from "@reduxjs/toolkit";

export default configureStore({
        reducer: {
            login: loginReducer
        }
    }
)