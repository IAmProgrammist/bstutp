import React from 'react';
import style from '../assets/score.css'

const Score = store => {
    let {score, width, height, fontSize} = store;

    return <div className={"score " + (score > 0.7 ? "score_good" :
        (score > 0.5 ? "score_ok" : "score_bad"))}
                style={{width: `${width}px`, height: `${height}px`, fontSize: `${fontSize}px`}}>
        {Math.floor(score * 100) + "%"}
    </div>
}

export default Score;