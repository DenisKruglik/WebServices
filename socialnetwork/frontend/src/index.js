import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import * as serviceWorker from './serviceWorker';
import configureStore from "./configureStore";
import {Provider} from "react-redux";
import {configureFlashMessages} from "redux-flash-messages/lib";

const store = configureStore();

configureFlashMessages({
    dispatch: store.dispatch
});

ReactDOM.render(<Provider store={store}>
        <App />
    </Provider>,
    document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
