import React, { useState, useContext, useRef, useEffect } from "react";
import AuthContext from "../contexts/AuthContext";
import head from '../images/head.png';
import { PiSkipBackCircleLight } from 'react-icons/pi';
import add from '../images/add.png';
import developers from '../images/developers.png';
import dashboard from '../images/dashboard.png';
import explore from '../images/explore.png';
import favorite from '../images/favorite.png';
import edit from '../images/edit.png';
import { Link, useNavigate } from 'react-router-dom';


export default function Dashboard() {

    const auth = useContext(AuthContext);

    const [open, setOpen] = useState(true);
    let userId = useRef(0);
    const [appUserId, setAppUserId] = useState(0);

    const navigate = useNavigate();

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

    const goToLoggedIn = () => {
        console.log('navigate to userLoggedIn');
        navigate("/userLoggedIn");
    }

    const goToMeals = () => {
        console.log('navigate to meals');
        navigate(`/${appUserId}/meals`);
    }

    const goToAdd = () => {
        console.log('navigate to add');
        navigate(`/add`);
    }

    const goToIndex = () => {
        console.log('navigate to user index');
        navigate('/userIndex');
    }

    const goToEditProfile = () => {
        console.log('navigate to edit profile');
        navigate(`/editProfile/${userId.current}`);
    }

    const goToDevs = () => {
        console.log('navigate to devs');
        navigate('/devs');
    }



    return (<>
        <div className="flex ml-10">
            <div className="flex-grow">
                <div className={`${open ? 'w-72' : 'w-20'} duration-300 h-screen p-4 pt-5 bg-amber rounded-xl relative`}>
                    <PiSkipBackCircleLight className={`absolute cursor-pointer rounded-full -right-3 top-12 w-12 h-12 bg-white border-4 border-amber ${!open && 'rotate-180'}`}
                        onClick={() => setOpen(!open)} />

                    <div className="flex gap-x-4 items-center">
                        <img src={head} alt='imghead' className={`cursor-pointer w-10 h-10 duration-500 
                ${open && "rotate-[360deg]"}`} />
                        <h1 className={`text-black origin-left font-bold text-xl duration-300 ${!open && 'scale-0'}`}>Nosh.Map</h1>
                    </div>
                    <ul className="pt-6">
                            <li
                            className={`text-gray-300 text-sm flex items-center w-12 h-12 mt-9
                    gap-x-4 cursor-pointer p-2 hover:bg-white rounded-md` }>
                            <img src={dashboard} alt="menuimg" onClick={() => goToLoggedIn()} /> <span className={`${!open && 'hidden'} origin-left duration-200`}onClick={() => goToLoggedIn()}>Dashboard</span>
                            </li>
                            
                            <li
                            className='text-gray-300 text-sm flex items-center w-12 h-12
                    gap-x-4 cursor-pointer p-2 hover:bg-white rounded-md mt-9'>
                            <img src={favorite} alt="menuimg" onClick={() => goToMeals()} /><span className={`${!open && 'hidden'} origin-left duration-200`} onClick={() => goToMeals()}>Complete Menu</span>
                            </li>
                  
                        <li
                            className='text-gray-300 text-sm flex items-center w-12 h-12
                    gap-x-4 cursor-pointer p-2 hover:bg-white rounded-md mt-2'>
                        <img src={add} alt="menuimg" onClick={() => goToAdd()} /><span className={`${!open && 'hidden'} origin-left duration-200`} onClick={() => goToAdd()}>Add Restaurant</span>
                        </li>

                            <li
                        className='text-gray-300 text-sm flex items-center w-12 h-12
                    gap-x-4 cursor-pointer p-2 hover:bg-white rounded-md mt-2'>
                            <img src={explore} alt="menuimg" onClick={() => goToIndex()} /><span className={`${!open && 'hidden'} origin-left duration-200`} onClick={() => goToIndex()}>User Index</span>
                            </li>

                            <li
                            className='text-gray-300 text-sm flex items-center w-12 h-12
                    gap-x-4 cursor-pointer p-2 hover:bg-white rounded-md mt-9'>
                            <img src={edit} alt="menuimg" onClick={() => goToEditProfile()}/>
                            <span className={`${!open && 'hidden'} origin-left duration-200`}onClick={() => goToEditProfile()}>Profile</span></li>

                            <li
                            className='text-gray-300 text-sm flex items-center w-12 h-12
                    gap-x-4 cursor-pointer p-2 hover:bg-white rounded-md mt-2'>
                            <img src={developers} alt="menuimg" onClick={() => goToDevs()} />
                                <span className={`${!open && 'hidden'} origin-left duration-200`}onClick={() => goToDevs()}>Developers</span></li>
                    
                    </ul>

                </div>
            </div>
        </div>
    </>

    )
}