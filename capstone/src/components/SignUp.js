import React, { useContext, useState, useRef } from "react";
import LoginCard from './LoginCard';
import '../styles/Login.css';
import { Link, useNavigate } from 'react-router-dom';
import AuthContext from '../contexts/AuthContext';
import Errors from './Errors';


export default function SignUp() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [errors, setErrors] = useState([]);

    let errorList = useRef([]);

    const auth = useContext(AuthContext);

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        console.log('handleSubmit in SignUp');
        e.preventDefault();
        errorList.current = [];

        if (password !== confirmPassword) {
            setErrors(["Passwords do not match. "]);
            return;
        }

        await createAppUser();
        console.log('errorlist right before if: ', errorList.current);

        if (errorList.current.length !== 0) {
            setErrors(errorList.current);
            return;
        }
        
        const response = await fetch("http://localhost:8080/authenticate", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                username,
                password,
            }),
        });

        if (response.status === 200) {
            const { jwt_token } = await response.json();
            console.log(jwt_token);
            auth.login(jwt_token);
            navigate("/profileForm");
        } else if (response.status === 403) {
            console.log(username, password);
            errorList.current.push("Sign up failed.");
        } else {
            errorList.current.push("Unknown error.");
        };
        console.log('errorList.current: ', errorList);
        setErrors(errorList.current);
    };

    const createAppUser = async () => {
        const init = {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                username,
                password,
            }),
        };

        await fetch("http://localhost:8080/create_account", init)
        .then((response) => {
        if (response.status === 200 || response.status === 201) {
            return null;
        } else if (response.status === 400) {
            //errorList.current.push(response.body);
            console.log( "Status 400. Sign up failed (create_account)");
            return response.json();
        } else if (response.status === 403) {
            //errorList.current.push(response.body);
            console.log("Status 403. Sign up failed (create_account");
            return response.json();
        } else {
            //errorList.current.push("Unknown error in create_account ", response.body);
            console.log("Unknown error in create_account");
            console.log(response.status);
            return response.json();
        }
    })
        .then((data) => {
            console.log(data)
            if (data) {
            errorList.current.push(data)
        }
    })

          .catch(console.log);
    };

    return <LoginCard>
        <div className="register">
            <div className="flex flex-col items-center justify-center mb-4">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="2" stroke="currentColor" className="w-12 h-12">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M17.982 18.725A7.488 7.488 0 0012 15.75a7.488 7.488 0 00-5.982 2.975m11.963 0a9 9 0 10-11.963 0m11.963 0A8.966 8.966 0 0112 21a8.966 8.966 0 01-5.982-2.275M15 9.75a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>

                <h2 className="text-2xl font-bold">Sign Up</h2>
            </div>
            <form onSubmit={handleSubmit}>
                <div className='input_container'>
                    <div className="flex items-center text-md mb-6 md:mb-8 mt-4">
                        <svg className="absolute ml-3" width="24" viewBox="0 0 24 24">
                            <path d="M20.822 18.096c-3.439-.794-6.64-1.49-5.09-4.418 4.72-8.912 1.251-13.678-3.732-13.678-5.082 0-8.464 4.949-3.732 13.678 1.597 2.945-1.725 3.641-5.09 4.418-3.073.71-3.188 2.236-3.178 4.904l.004 1h23.99l.004-.969c.012-2.688-.092-4.222-3.176-4.935z" />
                        </svg>
                        <input type="text"
                            id="username"
                            className="bg-gray-200 pl-12 py-2 md:py-4 focus:outline-none w-full rounded-lg bg-stone"
                            placeholder="Username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)} />
                    </div>
                    <div className="flex items-center text-md mb-6 md:mb-8 mt-2">
                        <svg className="absolute ml-3" viewBox="0 0 24 24" width="24">
                            <path d="m18.75 9h-.75v-3c0-3.309-2.691-6-6-6s-6 2.691-6 6v3h-.75c-1.24 0-2.25 1.009-2.25 2.25v10.5c0 1.241 1.01 2.25 2.25 2.25h13.5c1.24 0 2.25-1.009 2.25-2.25v-10.5c0-1.241-1.01-2.25-2.25-2.25zm-10.75-3c0-2.206 1.794-4 4-4s4 1.794 4 4v3h-8zm5 10.722v2.278c0 .552-.447 1-1 1s-1-.448-1-1v-2.278c-.595-.347-1-.985-1-1.722 0-1.103.897-2 2-2s2 .897 2 2c0 .737-.405 1.375-1 1.722z" />
                        </svg>
                        <input type="password"
                            id="password"
                            className="bg-gray-200 pl-12 py-2 md:py-4 focus:outline-none w-full rounded-lg bg-stone"
                            placeholder="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)} />
                    </div>
                    <div className="flex items-center text-md mb-6 md:mb-8 mt-2">
                        <svg className="absolute ml-3" viewBox="0 0 24 24" width="24">
                            <path d="m18.75 9h-.75v-3c0-3.309-2.691-6-6-6s-6 2.691-6 6v3h-.75c-1.24 0-2.25 1.009-2.25 2.25v10.5c0 1.241 1.01 2.25 2.25 2.25h13.5c1.24 0 2.25-1.009 2.25-2.25v-10.5c0-1.241-1.01-2.25-2.25-2.25zm-10.75-3c0-2.206 1.794-4 4-4s4 1.794 4 4v3h-8zm5 10.722v2.278c0 .552-.447 1-1 1s-1-.448-1-1v-2.278c-.595-.347-1-.985-1-1.722 0-1.103.897-2 2-2s2 .897 2 2c0 .737-.405 1.375-1 1.722z" />
                        </svg>
                        <input type="password"
                            id="confirmPassword"
                            className="bg-gray-200 pl-12 py-2 md:py-4 focus:outline-none w-full rounded-lg bg-stone"
                            placeholder="Confirm Password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)} />
                    </div>
                    <div className='err_msg'>{errors.map((error, i) => (
                        <Errors key={i} msg={error} />
                    ))}</div>
                </div>
                <br />
                <div className="submit border rounded mb-4 bg-blue-600 text-black cursor-pointer bg-stone hover:bg-lime">
                    <div className="wrapper flex w-max mx-auto">
                        <svg xmlns="http://www.w3.org/2000/svg" className="w-5 my-auto" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z" />
                        </svg>
                        <input className="h-[38px] w-full font-bold rounded-lg" type="submit" value="Register" />
                    </div>
                </div>
            </form>
            <div className='px-4 mt-5 text-center font-bold'>
                Already Have an account? <Link to="/login" title="login" className='hover:underline'>Login</Link>
            </div>
        </div>

    </LoginCard>

}