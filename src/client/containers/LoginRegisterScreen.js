import React from 'react';
import style from '../assets/formLoginRegister.css'
import {useNavigate} from "react-router-dom";
import {
    Container,
    Header,
    Content,
    Footer,
    Form,
    ButtonToolbar,
    Button,
    Navbar,
    Panel,
    FlexboxGrid,
    Checkbox,
    CheckboxGroup
} from 'rsuite';

let LoginRegisterScreen = props => {
    let baseURL = window.location.origin;

    const navigate = useNavigate();

    let {
        login,
        password,
        rememberMe,
        loginFieldError,
        isFetching
    } = props.loginData;

    let {
        changeLogin,
        changePassword,
        changeRememberMe,
        loginRegisterFetching,
        setErrorLogin
    } = props.loginFuncs;

    let {setListFetching, setListData} = props;

    return <div>
        <Container>
            <Content className={"container__loginform"}>
                <Panel header={<h3>Войти</h3>} bordered shaded className={"loginform__panel"}>
                    <Form fluid>
                        <Form.Group controlId="username">
                            <Form.ControlLabel>Логин</Form.ControlLabel>
                            <Form.Control name="username" value={login} onChange={(ev) => changeLogin(ev)}/>
                        </Form.Group>

                        <Form.Group controlId="password">
                            <Form.ControlLabel>Пароль</Form.ControlLabel>
                            <Form.Control name="password" type="password" autoComplete="off" value={password}
                                          onChange={(ev) => changePassword(ev)}/>
                        </Form.Group>

                        <Form.Group controlId="remember-me">
                            <Checkbox name="remember-me" checked={rememberMe}
                                      onChange={ev => changeRememberMe(!rememberMe)}>Запомнить меня</Checkbox>
                        </Form.Group>

                        <Form.Group>
                            <ButtonToolbar>
                                <Button appearance="primary" loading={isFetching} onClick={ev => {
                                    loginRegisterFetching(true);
                                    let sendObject = `username=${login}&password=${password}${rememberMe ? `&rememberMe=on` : ""}`;

                                    fetch(baseURL + "/api/login",
                                        {
                                            method: 'POST',
                                            headers: {
                                                'Accept': 'application/x-www-form-urlencoded',
                                                'Content-Type': 'application/x-www-form-urlencoded'
                                            },
                                            credentials: "include",
                                            body: sendObject
                                        })
                                        .then(res => res.json())
                                        .then(res => {
                                            loginRegisterFetching(false);
                                        })
                                        .catch(res => {
                                            loginRegisterFetching(false);
                                            console.error("Vlad please fix this, something really bad happened");
                                            console.error(res.stack);
                                        });
                                }}>Войти</Button>
                                <Button appearance="link">Забыли пароль?</Button>
                            </ButtonToolbar>
                        </Form.Group>
                    </Form>
                </Panel>
            </Content>
        </Container>
    </div>
}

export default LoginRegisterScreen;