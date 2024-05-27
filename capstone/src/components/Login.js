import React, { useContext, useState } from 'react';
import LoginCard from './LoginCard';
import '../styles/Login.css';
import { Link, useNavigate } from 'react-router-dom';
import AuthContext from '../contexts/AuthContext';
import Errors from './Errors';
import Header from './Header';

export default function Login() {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [errors, setErrors] = useState([]);

    const auth = useContext(AuthContext);

    // const errors = {
    //     username: "Invalid username",
    //     password: "Invalid password",
    //     noUsername: "Username cannot be empty. Please enter username",
    //     noPassword: "Password cannot be empty. Please enter password",
    // };

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();



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
            navigate("/userLoggedIn");
        } else if (response.status === 403) {
            setErrors(["Login failed"]);
        } else {
            setErrors(["Unknown error"]);
        }
    };

    return <>
    <LoginCard>
        <div className="flex flex-col items-center justify-center mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" className="w-12 h-12" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
                <path strokeLinecap="round" strokeLinejoin="round" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
            </svg>
            <h2 className="text-2xl font-bold">Login</h2>
        </div>
        <p className='subtitle'>Please log in using your username and password!</p>
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
                <div className='err_msg'>{errors.map((error, i) => (
                    <Errors key={i} msg={error} />
                ))}</div>
            </div>
            <br />
            <button type='submit' className='h-[38px] w-full bg-stone font-bold rounded-lg hover:bg-orange'>Login</button>
        </form>
        <div className='px-4 mt-10 text-center font-bold'>
            Don't have an account? <Link to="/signup" title="signup" className='hover:underline'>Signup</Link>
        </div>

    </LoginCard>
    </>
}
