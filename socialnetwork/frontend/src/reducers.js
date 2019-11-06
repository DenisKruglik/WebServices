import { combineReducers } from "redux";
import { flashMessage } from "redux-flash-messages";
import {LOGIN, LOGIN_FAIL, LOGIN_SUCCESS, LOGOUT, RECEIVE_USER, REQUEST_USER, REQUEST_USER_FAIL} from "./actions";

const auth = (state = {
    user: null,
    isUserFetching: false
}, action) => {
    switch (action.type) {
        case LOGIN:
            return Object.assign({}, state, {
                isUserFetching: true
            });
        case LOGIN_SUCCESS:
            localStorage.setItem('authToken', action.token);
            return Object.assign({}, state, {
                user: action.user,
                token: action.token,
                isUserFetching: false
            });
        case LOGIN_FAIL:
            return Object.assign({}, state, {
                isUserFetching: false
            });
        case REQUEST_USER:
            return Object.assign({}, state, {
                isUserFetching: true
            });
        case RECEIVE_USER:
            return Object.assign({}, state, {
                isUserFetching: false,
                user: action.user,
                token: action.token
            });
        case REQUEST_USER_FAIL:
            return Object.assign({}, state, {
                isUserFetching: false
            });
        case LOGOUT:
            localStorage.removeItem('authToken');
            return Object.assign({}, state, {
                user: null,
                token: null
            });
        default:
            return state;
    }
};

const rootReducer = combineReducers({
    auth,
    flashMessage
});

export default rootReducer;