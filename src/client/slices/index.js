import baseReducer from "./reducer"
import {configureStore} from "@reduxjs/toolkit";

export default configureStore({
        reducer: {
            base: baseReducer
        }
    }
)