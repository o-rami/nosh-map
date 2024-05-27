import React from "react";
import { Link, useNavigate } from 'react-router-dom';
import { useEffect, useState, useContext } from "react";
import AuthContext from "../contexts/AuthContext";
import SideBar from "./Dashboard/SideBar/Sidebar";
import Body from "./Dashboard/Body/Body";
import '../styles/LoggedIn.css';
import GMap from "./GMap";
import { LoadScript } from "@react-google-maps/api";
import Dashboard from "./Dashboard";
import Welcome from "./Dashboard/Body/Welcome/Welcome";


const libs = ["places"];
const key = "AIzaSyBeEVEGLVLMX7OjUXKsZXdsDbg3SAo8-0Q";



function LoggedIn() {

    const showHeader = false;


    const auth = useContext(AuthContext);


    return (<>
        <div className="flex mt-36">
            <Dashboard />
            <div className="container ml-4">
                <Body />
            </div>
        </div>

    </>
    )
}

export default LoggedIn;