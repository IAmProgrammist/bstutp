import React from 'react';
import style from '../assets/formLoginRegister.css'
import { useNavigate } from "react-router-dom";

let LoginRegisterScreen = props => {
    let baseURL = window.location.origin;

    const navigate = useNavigate();

    let {
        login,
        password,
        rememberMe,
        loginFieldError
    } = props.loginData;

    let {
        changeLogin,
        changePassword,
        changeRememberMe,
        loginRegisterFetching,
        setErrorLogin
    } = props.loginFuncs;

    return <form className={"loginForm loginForm_type_login"}
                 action={"/api/account/login"} method={"post"} onSubmit={ev => {
        loginRegisterFetching(true);
        ev.preventDefault();
        const data = new FormData(ev.target);
        let sendObject = {};

        for (const [name, value] of data) {
            sendObject[name] = value;
        }

        fetch(ev.target.getAttribute("action"),
            {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                credentials: "include",
                body: JSON.stringify(sendObject)
            })
            .then(res => {
                loginRegisterFetching(false);
                if (res.ok)
                    navigate("/records");

                return res.json();
            })
            .then(res => {
                console.log(res);
                loginRegisterFetching(false);

                switch (res.type) {
                    case "user_doesnt_exists":
                        setErrorLogin();
                        break;
                    default:
                        console.error("Vlad please fix this, this event in login/register is not set " + res.type);
                        break;
                }
            })
            .catch(res => {
                loginRegisterFetching(false);
                console.error("Vlad please fix this, something really bad happened");
                console.error(res.stack);
            });
    }}>
        <div className={"loginForm__input-container " +
            (loginFieldError ? "loginForm_input_invalid" : "")}>
            <label className={"loginForm__input-title"}>Логин:</label>
            <input type={"text"} name={"login"} value={login}
                   onChange={(ev) => changeLogin(ev.target.value)}
                   className={"loginForm__input loginForm__input_type_login"}/>
            <p className={"loginForm__input-sub"}>Пользователь не найден</p>
        </div>
        <div className={"loginForm__input-container loginForm_input_invalid"}>
            <label className={"loginForm__input-title"}>Пароль:</label>
            <input type={"password"} name={"password"} value={password}
                   onChange={(ev) => changePassword(ev.target.value)}
                   className={"loginForm__input loginForm__input_type_password"}/>
        </div>
        <div className={"loginForm__input-checkbox-container"}>
            <input id={"rememberMe"} type={"checkbox"} name={"rememberMe"} value={rememberMe}
                   onChange={(ev) => changeRememberMe(ev.target.value)}
                   className={"loginForm__input loginForm__input_type_checkbox"} placeholder={"Запомнить меня"} name={"rememberMe"}/>
            <label htmlFor={"rememberMe"}>Запомнить меня</label>
        </div>

        <button type={"submit"}
                className={"button loginForm__input loginForm__input_type_submit"}>Войти</button>
    </form>
}

export default LoginRegisterScreen;