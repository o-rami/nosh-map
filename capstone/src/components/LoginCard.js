import React from "react";
import '../styles/LoginCard.css';


function LoginCard(props) {

    return (<>
        <div className="loginCard">{props.children}</div>
        </>
    )
}

export default LoginCard;