import React, {Component, Fragment} from "react";

class MessageForm extends Component {
    constructor(props) {
        super(props);
        this.onSubmit = this.onSubmit.bind(this);
        this.onChange = this.onChange.bind(this);
        this.state = {
            message: '',
            notificationText: ''
        };
    }

    render() {
        return (
            <Fragment>
                <form onSubmit={this.onSubmit}>
                    <textarea name={"message"} onChange={this.onChange} placeholder={"Enter your message here"} required={true}/>
                    <button type={"submit"}>Send message</button>
                </form>
                <div>{this.state.notificationText}</div>
            </Fragment>
        );
    }

    onSubmit(e) {
        e.preventDefault();
        const data = {
            message: this.state.message
        };
        fetch('/message/send/', {
            method: 'POST',
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(data => this.notify(data.message));
    }

    onChange(e) {
        const name = e.target.name;
        const value = e.target.value;
        this.setState(prevState => {
            const newState = { ...prevState };
            newState[name] = value;
            return newState;
        });
    }

    notify(message) {
        this.setState(prevState => {
            const newState = {...prevState};
            newState.notificationText = message;
            return newState;
        });
        setTimeout(() => {
            this.setState(prevState => {
                const newState = {...prevState};
                newState.notificationText = '';
                return newState;
            });
        }, 3000);
    }
}

export default MessageForm;