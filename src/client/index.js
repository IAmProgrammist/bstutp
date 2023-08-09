import React from 'react';
import ReactDOM from 'react-dom';
import store from "./slices/index"
import App from "./containers/App";
import {CookiesProvider} from 'react-cookie';

const root = ReactDOM.createRoot(document.querySelector("body"));
root.render(
    <CookiesProvider>
        <App store={store}/>
    </CookiesProvider>
)