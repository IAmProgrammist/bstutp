import React from 'react';
import style from '../assets/header.css'

let Header = props => {
    let {showBackButton, showAddButton, showExportButton} = props;
    let {onBackClicked, onAddClicked, onExportClicked} = props;
    let {userInfo, onUserClick} = props;

    return <header className={"header"}>
        <div className={"container"}>
            {<a className={"header__button_type_back header__button"} href={"#"} onClick={ev => {
                ev.preventDefault();
                onBackClicked()
            } }>{showBackButton ? "Назад" : ""}</a>}
            {showAddButton && <a className={"header__button_type_add header__button"} href={"#"} onClick={ev => {
                ev.preventDefault();
                onAddClicked()
            } }>Добавить</a>}

            <a className={"header__button_type_user header__button"} href={"#"} onClick={ev => {
                ev.preventDefault();
                onUserClick()
            } }>{userInfo}</a>
            {showExportButton && <a className={"header__button_type_export header__button"} href={"#"} onClick={ev => {
                ev.preventDefault();
                onExportClicked()
            } }>Экспорт</a>}
        </div>
    </header>
}

export default Header;