import React, { useContext, useEffect, useState, useRef } from "react";
import './sidebar.css';
import logo from '../../../images/logo.png';
import { BsSpeedometer2 } from 'react-icons/bs';
import { RiUserHeartLine, RiUserSettingsLine } from 'react-icons/ri';
import { MdOutlineExplore, MdComputer } from 'react-icons/md';
import { Link } from 'react-router-dom';
import AuthContext from "../../../contexts/AuthContext";



export default function SideBar() {

    const auth = useContext(AuthContext);

    let userId = useRef(0);
    const [appUserId, setAppUserId] = useState(0);

    useEffect(() => {
        getAppUserId();
    })

    const getAppUserId = async () => {
        const userInit = {
            headers: {
                'Content-type': 'application/json',
                'Accept': 'application/json',
            },
            method: 'GET',
        };
        let response = await fetch(`http://localhost:8080/api/appUser/${auth.user.username}`, userInit);
        const user = await response.json();
        console.log('USER IN GETUSERAPP BEFORE RETURNING ID', user);
        userId.current = user.appUserId;
        setAppUserId(user.appUserId);
    }


    return (
        <div className="sideBar grid min-h-screen">
            <div className="logoDiv flex">
                <img src={logo} alt="Image Name" />
            </div>

            <div className="menuDiv">
                <h3 className="divTitle">
                    QUICK MENU
                </h3>
                <ul className="menuLists grid">
                    <li className="listItem">
                        <Link to="/userLoggedIn" title="dashboard" className="menuLink flex">
                            <BsSpeedometer2 className="icon" />
                            <span className="smallTxt">
                                Dash board
                            </span>
                        </Link>
                    </li>

                    <li className="listItem">
                        <Link to="/userFavorites" title="userFavorites" className="menuLink flex">
                            <RiUserHeartLine className="icon" />
                            <span className="smallTxt">
                                My Favorites
                            </span>
                        </Link>
                    </li>

                    <li className="listItem">
                        <a href="#" className="menuLink flex">
                            <MdOutlineExplore className="icon" />
                            <span className="smallTxt">
                                Explore
                            </span>
                        </a>
                    </li>
                </ul>
            </div>
            <div className="settingsDiv">
                <h3 className="divTitle">
                    SETTINGS
                </h3>
                <ul className="menuLists grid">
                    <li className="listItem">
                        <Link to={`/editProfile/${userId.current}`} title="editProfie" className="menuLink flex">
                            <RiUserSettingsLine className="icon" />
                            <span className="smallTxt">
                                Profile
                            </span>
                        </Link>
                    </li>
                </ul>
            </div>

            <div className="devsCard">
            
            </div>
        </div>
    )
}