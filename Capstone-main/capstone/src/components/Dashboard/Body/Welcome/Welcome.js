import { useContext } from "react"
import AuthContext from "../../../../contexts/AuthContext";
import { Link } from 'react-router-dom';

export default function () {

    const auth = useContext(AuthContext);


    return (<>

        <div className="top">
            <div className='welcome-user'>
                {auth.user && (
                    <div className="flex justify-between font-bold mt-7 text-xl">
                        Welcome {auth.user.username}!
                        <button className="flex justify-center items-center mr-14 h-[38px] w-20 bg-stone font-bold rounded-lg hover:bg-orange" onClick={() => auth.logout()}>
                            <Link
                                to="/"
                                title="logout"
                            >Logout
                            </Link></button>
                    </div>
                )}
            </div>
        </div>

    </>
    )
}