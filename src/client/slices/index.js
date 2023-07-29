import loginReducer from "./loginReducer"
import {configureStore} from "@reduxjs/toolkit";
import mainlistReducer from "./mainListReducer"
import testReducer from "./testReducer"

export default configureStore({
        reducer: {
            login: loginReducer,
            mainlist: mainlistReducer,
            test: testReducer
        }
    }
)