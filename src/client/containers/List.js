import React from 'react';
import style from '../assets/list.css'
import Header from '../elements/Header'

let List = props => {
    let baseURL = window.location.origin;

    let {
    } = props.mainlistData;

    let {
    } = props.mainlistFuncs;

    return <div>
        <Header showBackButton={false} showAddButton={false} showExportButton={false}/>
        <section></section>
    </div>
}

export default List;