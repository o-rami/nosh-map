import { useContext, useEffect, useState, useRef } from 'react';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { findByUserId, createProfile, updateProfile } from '../services/profileApi';
import '../styles/ProfileForm.css';
import Errors from './Errors';
import { FaSave, FaBan } from 'react-icons/fa';
import AuthContext from '../contexts/AuthContext';
import LoginCard from './LoginCard';

const emptyProfile = {
    appUserId: 0,
    firstName: "",
    lastName: "",
    address: "",
}

function ProfileForm() {

    const [profile, setProfile] = useState(emptyProfile);
    const [errors, setErrors] = useState([]);

    const auth = useContext(AuthContext);

    const { appUserId } = useParams();

    const navigate = useNavigate();

    useEffect(() => {
        console.log('editAppUserId from param: ', appUserId);
        if (appUserId) {
            console.log('INSIDE USEEFFECT OF EDITAPPUSERID ')
            findByUserId(appUserId)
                .then(data => {console.log('DATA IN EDITUSERID ', data); setProfile(data)})
                .catch(err => {
                    navigate("/error", {
                        state: { msg: err }
                    });
                });
        } else {
            
        }
    }, [appUserId, navigate]);

    const setAppUserId = async () => {
        console.log('RUNNING ELSE IN USEEFFECT()');
        const userInit = {
            headers: {
                'Content-type': 'application/json',
                'Accept': 'application/json',
            },
            method: 'GET',
        };
        let response = await fetch(`http://localhost:8080/api/appUser/${auth.user.username}`, userInit);
        console.log('RESPONSE: ', response);
        const user = await response.json();
        console.log('user', user);
        return user.appUserId;
    }


    const handleChange = (e) => {
        console.log('change: ', e.target.name);
        const nextProfile = { ...profile };
        nextProfile[e.target.name] = e.target.value;

        setProfile(nextProfile);
    };

    const handleSaveProfile = async (e) => {
        e.preventDefault();
        console.log('PROFILE: ', profile);
        console.log('AUTH: ', auth);


        const init = {
            headers: {
                'Content-type': 'application/json',
                'Accept': 'application/json',
                'Authorization': `Bearer ${auth.user.token}`,
            },
            body: JSON.stringify(profile),
        };

        if (profile.appUserId === 0) {
            console.log('Running POST');
            let newProfile = {...profile};
            newProfile.appUserId = await setAppUserId();
            init.method = 'POST';
            console.log('newProfile right before post init assignment ', newProfile);
            init.body = JSON.stringify(newProfile);
            console.log('init: ', init);
            fetch('http://localhost:8080/api/profile', init)
                .then(data => {
                    navigate("/userLoggedIn", {
                        state: { 
                            msgType: 'success',
                            ms: `${profile.firstName} was added!` }
                    });
                })
                .catch(err => setErrors(err))
        } else {
            console.log('running PUT');
            init.method = "PUT";
            fetch(`http://localhost:8080/api/profile/${profile.appUserId}`, init)
                .then(() => {
                    navigate("/userLoggedIn", {
                        state: {
                            msgType: 'success',
                            msg: `${profile.firstName} was updated!`
                        }
                    })
                })
                .catch(err => setErrors(err));
        }
        console.log('ERRORS', errors);
    };

    return ( 
    
    <LoginCard>
        <div className='flex flex-col items-center justify-center mb-4'>
            <h2 className='text-center mt-3 text-md font-bold text-xl'>User Information</h2>
            <form className='h-52 w-full mt-1 p-4 space-y-4 ' onSubmit={handleSaveProfile}>
                <input type="text" placeholder='first name' 
                id='firstName' 
                name='firstName' 
                className='inputClass bg-stone' 
                value={profile.firstName} 
                onChange={handleChange}/>
                <input type="text" placeholder='last name' 
                id='lastName' 
                name='lastName' 
                className='inputClass bg-stone' 
                value={profile.lastName} 
                onChange={handleChange}/>
                <input type="text" placeholder='address' 
                id='address' 
                name='address' 
                className='inputClass bg-stone' 
                value={profile.address} 
                onChange={handleChange}/>
            
            <div className='flex gap-4 justify-center items-center'>
                <button type="submit" title="Save" className="flex justify-center items-center h-[38px] w-20 bg-stone font-bold rounded-lg hover:bg-orange"><FaSave /></button>
                <Link to="/userLoggedIn" type="button" title="Cancel" className="flex justify-center items-center  h-[38px] w-20 bg-stone font-bold rounded-lg hover:bg-orange"><FaBan /></Link>
            </div>
            </form>
            {/* <div className="errors"><Errors errors={errors} /></div> */}
        </div>
        </LoginCard>
    )

}

export default ProfileForm;