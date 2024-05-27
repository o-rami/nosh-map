import '../styles/HomePage.css';
import '../index.css';
import food3 from '../images/food3.png';
import home2 from '../images/home2.png'
import { Link } from 'react-router-dom';
import video from '../images/homevid.mp4'
import Header from './Header';


function HomePage() {

    return (<>
    <div className='flex mt-10'>
        <div className='content grid grid-flow-col justify-stretch'>
            <div className='textBox justify-center h-screen items-center mt-20'>
                <p className='flex text-4xl font-semibold text-black tracking-wide text-transform: uppercase'>A Taste of  </p>
                <div className='flex'>
                <h1 className="opinions flex text-6xl font-bold text-transform: uppercase" > opinions...</h1>
                    </div>
                <br /><p className='text-3xl font-semibold text-black tracking-wide px-2 flex justify-center'>A Feast of choice</p>
                <p className='text-xl font-semibold text-black tracking-wide px-2 py-4 flex justify-end'>Discover yours.</p>

                <div className='flex justify-end'>
                <button type='submit' className='h-[38px] w-20 bg-orange font-bold mt-3 justify-center rounded-xl hover:bg-lime'>
                    <Link
                        to="/login"
                        title="login"
                    >Login
                    </Link>
                </button>
                </div>
            </div>
            <div className='flex justify-end'>
            <div className='imgBox flex justify-end overflow-hidden bg-no-repeat mr-[-10px] mt-[-100px]'>
                <img src={home2} className='flex justify-end' alt='homeimage' />
            </div>
            </div>
        </div>
        </div>
    </>
    )
}

export default HomePage;