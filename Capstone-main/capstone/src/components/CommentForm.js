import AuthContext from "../contexts/AuthContext";
import { React, useState , useEffect, useContext } from "react";
import { findbyCommentId, } from "../services/commentApi";
import { Link, useNavigate, useParams } from "react-router-dom";
import LoginCard from "./LoginCard";
import {FaSave, FaBan} from 'react-icons/fa'

const EMPTY_COMMENT = {
    commentId: 0,
    description: "",
    restaurantId: 0,
    appUserId: 0,
    postDate: null,
    isPublic: true,
}

function CommentForm() {

    const [comment, setComment] = useState(EMPTY_COMMENT);
    const [errors, setErrors] = useState([]);

    const auth = useContext(AuthContext);

    const { appUserId, commentId, restaurantId } = useParams();

    const navigate = useNavigate();

    /*useEffect(() => {
        if (commentId) {
          findbyCommentId(commentId)
            .then((data) => setComment(data))
            .catch((error) => {
              navigate("/error", {
                state: { msg: error },
              });
            });
        } else {
          setComment(EMPTY_COMMENT);
        }
      }, [commentId, navigate]);*/
    



    const handleChange = (e) => {
        console.log('change: ', e.target.name);
        const nextComment = { ...comment };
        nextComment[e.target.name] = e.target.value;

        setComment(nextComment);
    };

    const handleSaveComment = async (e) => {
        e.preventDefault();
     
        if (comment.commentId === 0) {
          console.log(appUserId, "APPUSERID");
          console.log(restaurantId, "RESTAURANTID");
            comment.appUserId = appUserId;
            comment.restaurantId = restaurantId;
            comment.postDate = getCurrentDate();
            const init = {
              method: "POST",
              headers: {
                  'Content-type': 'application/json',
                  'Accept': 'application/json',
                  'Authorization': `Bearer ${auth.user.token}`,
              },
              body: JSON.stringify(comment)
              };
              console.log('INIT OF POST', init);
              const response = await fetch("http://localhost:8080/api/comment", init);
                if (response.status === 201) {
                  navigate(`/restaurantInfo/${restaurantId}`, {
                    state: {
                      msg: `${comment.commentId} was added!`
                }});
                } else {
                  let errors = await response.json();
                  setErrors(errors);
                };
          } else {
            let errors = [];
            const init = {
              method: "PUT",
              headers: {
                  'Content-type': 'application/json',
                  'Accept': 'application/json',
                  'Authorization': `Bearer ${auth.user.token}`,
              },
              body: JSON.stringify(comment)
              };
              const response = await fetch(`http://localhost:8080/api/comment/${comment.commentId}`, init);
              if (response.status === 204) {
                navigate(`/restaurantInfo/${restaurantId}`, {
                  state: {
                    msg: `${comment.commentId} was updated!`,
                  },
                });
              } else if (response.status === 404) {
                errors.push((`CommentId: ${comment.commentId} was not found.`));
              } else {
                console.log(response);
                errors.push(await response.json());
              }
            }
        };

        const getCurrentDate = () => {
          let today = new Date();
          let currentMonth = today.getMonth();
          currentMonth = currentMonth < 10 ? `0${currentMonth}`: currentMonth;
          let currentDay = today.getDate();
          currentDay = currentDay < 10 ? `0${currentDay}` : currentDay;
          let currentHour = today.getHours();
          currentHour = currentHour < 10 ? `0${currentHour}` : currentHour;
          let currentMinutes = today.getMinutes();
          currentMinutes = currentMinutes < 10 ? `0${currentMinutes}` : currentMinutes;
          let currentSeconds = today.getSeconds();
          currentSeconds = currentSeconds < 10 ? `0${currentSeconds}` : currentSeconds;
      
          let dateObj = 
              today.getFullYear() + "-"
            + currentMonth + "-"
            + currentDay + "T"
            + currentHour + ":"
            + currentMinutes + ":"
            + currentSeconds;

            return dateObj;
        }

    return ( 
    
    <LoginCard>
        <div className='flex flex-col items-center justify-center mb-4'>
            <h2 className='text-center mt-3 text-md font-bold text-xl'>Add Comment</h2>
            <form className='h-52 w-full mt-1 p-4 space-y-4 ' onSubmit={handleSaveComment}>
                <input type="text" placeholder='Description' 
                id='description' 
                name='description' 
                className='inputClass bg-stone' 
                value={comment.description} 
                onChange={handleChange}/>
    
            
            <div className='flex gap-4 justify-center items-center'>
                <button type="submit" title="Save" className="flex justify-center items-center h-[38px] w-20 bg-stone font-bold rounded-lg hover:bg-orange"><FaSave /></button>
                <Link to={`/restaurantInfo/${restaurantId}`} type="button" title="Cancel" className="flex justify-center items-center  h-[38px] w-20 bg-stone font-bold rounded-lg hover:bg-orange"><FaBan /></Link>
            </div>
            </form>
        
        </div>
        </LoginCard>
    )

}

export default CommentForm;