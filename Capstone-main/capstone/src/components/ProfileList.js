import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { findAllProfile, findByUserId } from "../services/profileApi";
import { FaEdit } from 'react-icons/fa'


function ProfileList() {

    const [profiles, setProfiles] = useState([]);

    // const [user, setUser] = useState([]);

    useEffect(() => {
        findAllProfile()
            .then(data => setProfiles(data));
    }, []);



    return (
        <div className="row">
            {profiles.length > 0 ? (
                profiles.map(p => {
                    return (
                        <div className="col" key={p.appUserId} >
                            <div className="card" style={{ width: '18rem' }}>
                                <div className="card-body">
                                    <h6 className="main-text">Name: {p.firstName} {p.lastName}</h6>
                                    <h3 className="address">Address: {p.address}</h3>
                                    <div className="icons">
                                        <br />
                                        <Link
                                            to={`/editProfile/${p.appUserId}`}
                                            title="Edit"
                                            className="btn btn-primary btn-sm rounded-pill"
                                        ><FaEdit />
                                        </Link>
                                    </div>
                                </div>
                            </div>
                        </div>
                    )
                })
            ) : <div className="no-profile">No Profiles</div>
            }

        </div>
    )


}

export default ProfileList;