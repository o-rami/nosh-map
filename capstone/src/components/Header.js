import '../index.css';
import '../styles/Header.css';
import logo from '../images/logo.png';
import { Link } from 'react-router-dom';
import React from 'react';


function Header() {


    return (
        <div className="flex justify-between items-center h-24 max-w-auto mx-auto px-4 text-black font-medium text-base text-transform: uppercase">
            <nav>
                <header>
                    <img className="logo-pic" src={logo} alt="" />
                    <ul className='flex'>
                        <li>
                            <Link
                                to="/"
                                title="home"
                                className="p-2 ml-4 bg-stone font-extrabold hover:bg-primary rounded-lg"><span>Home</span>
                            </Link>
                        </li>
                        <li>
                            <Link
                                to="/about"
                                title="about"
                                className="p-2 ml-4 bg-stone font-extrabold hover:bg-primary rounded-lg"><span>About</span>
                            </Link>
                        </li>
                        <li>
                            <Link
                                to="/guestView"
                                title="guestView"
                                className="p-2 ml-4 bg-stone font-extrabold hover:bg-primary rounded-lg"><span>Explore</span>
                            </Link>
                        </li>
                        <li>
                            <Link
                                to="/devs"
                                title="devs"
                                className="p-2 ml-4 bg-stone font-extrabold hover:bg-primary rounded-lg"><span>Meet The Devs</span>
                            </Link>
                        </li>
                    </ul>
                </header>
            </nav>
        </div>
    )

}

export default Header;