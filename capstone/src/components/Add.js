import Map from "./Dashboard/Body/GoogleMap/Map";
import Dashboard from "./Dashboard";
import GMap from "./GMap";
import { LoadScript } from "@react-google-maps/api";
import { FaEdit, FaBan } from 'react-icons/fa';
import { Link } from "react-router-dom";
import { useState } from "react";

const libs = ["places"];
const key = "AIzaSyBeEVEGLVLMX7OjUXKsZXdsDbg3SAo8-0Q";



export default function Add() {


    const [message, setMessage] = useState(" ");

    return (


        <div className="flex mt-36">
            <Dashboard />
            <div id="addHeader" className="ml-10">
                <div className="flex">
                    <h1 className="flex text-5xl font-bold justify-center text-transform: uppercase" > Add Restaurant</h1>
                </div>
                <p className="flex justify-center text-black text-xl">Select a Local Restaurant Pin and Add to Your Index!</p>
                {message.length > 1 && <p className="flex justify-center text-black text-xl">{message}</p>}
                <div id='addPage' className="flex justify-start">
                    <div id="mapContainer" className="flex justify-start mt-10 ml-[-300px]">
                        <div id="addMap" className="flex">

                            <LoadScript googleMapsApiKey={key} libraries={libs}>
                                <GMap setMessage={setMessage}/>
                            </LoadScript>
                        </div>
                        
                    </div>
                    
                </div>

            </div>
            </div>

    )
}