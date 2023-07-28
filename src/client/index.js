import React from 'react';
import ReactDOM from 'react-dom';
import store from "./slices/index"
import {connect} from "react-redux";
import {makeGet} from "./slices/reducer"

let App = props => {
    return <div><p>Асьм есть сервером будучи заявляю: {props.getMessage}</p>
        <button onClick={() => {
            const baseUrl = window.location.origin;

            fetch(baseUrl + "/api/hello-world")
                .then(res => res.text())
                .then(res => props.makeGet(res));
        }}>Сделать заявление
        </button>
    </div>
}

let mapStateToProps = (state) => {
    return {
        getMessage: state.base.getMessage
    }
}

let mapDispatchToProps = (dispatch) => {
    return {
        makeGet: (text) => dispatch(makeGet(text))
    }
}

let ConnectedApp = connect(mapStateToProps, mapDispatchToProps)(App)

const root = ReactDOM.createRoot(document.querySelector("body"));
root.render(<ConnectedApp store={store}/>)