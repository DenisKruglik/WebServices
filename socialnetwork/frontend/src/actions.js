import {addError, addSuccess} from "redux-flash-messages/lib";

export const LOGIN = 'LOGIN';
export const LOGIN_SUCCESS = 'LOGIN_SUCCESS';
export const LOGIN_FAIL = 'LOGIN_FAIL';
export const REQUEST_USER = 'REQUEST_USER';
export const RECEIVE_USER = 'RECEIVE_USER';
export const REQUEST_USER_FAIL = 'REQUEST_USER_FAIL';
export const LOGOUT = 'LOGOUT';

export function login(username, password) {
    return dispatch => {
        dispatch(loginAction());
        const requestBody = JSON.stringify({
            login: username,
            password: password
        });
        return fetch('/token-auth/', {
            method: 'POST',
            body: requestBody
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Invalid credentials provided for login');
        }).then(json => {
            dispatch(loginSuccess(json));
            addSuccess({ text: 'You logged in successfully!' });
        }).catch(error => {
            dispatch(loginFail(error));
            addError({ text: error.message });
        });
    };
}

function shouldLogin(state) {
    return !!state.auth.user;
}

export function loginIfNeeded(username, password) {
    return (dispatch, getState) => {
        if (shouldLogin(getState())) {
            return dispatch(login(username, password));
        }
    };
}

function loginAction() {
    return {
        type: LOGIN
    };
}

function loginSuccess(json) {
    return {
        type: LOGIN_SUCCESS,
        user: json.user,
        token: json.token
    }
}

function loginFail(error) {
    return {
        type: LOGIN_FAIL,
        message: error.message
    }
}

function requestUser() {
    return {
        type: REQUEST_USER
    };
}

function receiveUser(json, token) {
    return {
        type: RECEIVE_USER,
        user: json,
        token
    };
}

function shouldFetchUser(state) {
    return !state.auth.user && (!!state.auth.token || !!localStorage.getItem('authToken'));
}

function requestUserFail() {
    return {
        type: REQUEST_USER_FAIL
    };
}

function fetchUser(token) {
    return dispatch => {
        dispatch(requestUser());
        return fetch('/current_user/', {
            headers: {
                Authorization: `JWT ${token}`
            }
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Failed to get user data');
        }).then(json => dispatch(receiveUser(json, token)))
            .catch(error => dispatch(requestUserFail()));
    };
}

export function fetchUserIfNeeded() {
    return (dispatch, getState) => {
        const state = getState();
        if (shouldFetchUser(state)) {
            const token = state.auth.token || localStorage.getItem('authToken');
            return dispatch(fetchUser(token));
        }
    };
}

export function logout() {
    return {
        type: LOGOUT
    };
}