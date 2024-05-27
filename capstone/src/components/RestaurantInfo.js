import { React, useEffect, useContext, useRef, useState } from "react";
import AuthContext from '../contexts/AuthContext';
import Dashboard from "../components/Dashboard";
import { BsSkipStartCircle } from "react-icons/bs";

import { Link, useParams, useNavigate } from "react-router-dom";



export default function RestaurantInfo() {

  let restaurant = null;
  let meals = useRef([]);
  let comments = useRef([]);
  let rating = useRef(null);
  let userId = useRef(0);
  const [appUserId, setAppUserId] = useState(0);
  const [selectedAction, setSelectedAction] = useState("");
  const [editedComment, setEditedComment] = useState("");
  const [editedCommentId, setEditedCommentId] = useState("");

  const auth = useContext(AuthContext);
  const [restaurantState, setRestaurantState] = useState({});

  const navigate = useNavigate();

  const { restaurantId } = useParams();
  let commentId = 0;

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

  // Get User to then get restuarant based on restuarantId in parameter. Wherever this component is called, add retaurantId to navigation
  // rating.current.score would return rating variable score
  useEffect(() => {
    const fetchData = async () => {
      const init = {
        headers: {
          "Content-Type": "application/json",
          "Accept": "application/json"
        },
        method: "GET"
      };
      let response = await fetch(`http://localhost:8080/api/appUser/${auth.user.username}`, init);
      const user = await response.json();

      response = await fetch(`http://localhost:8080/api/restaurant/restaurantId/${restaurantId}`, init);
      restaurant = await response.json();


      response = await fetch(`http://localhost:8080/api/meal/restaurantId/${restaurant.restaurantId}`, init);
      if (response.status === 200) {
        meals.current = await response.json();
      }

      response = await fetch(`http://localhost:8080/api/comment/restaurantId/${restaurant.restaurantId}`, init);
      if (response.status === 200) {
        comments.current = await response.json();
      }

      response = await fetch(`http://localhost:8080/api/rating/${restaurant.restaurantId}/${user.appUserId}`, init);
      if (response.status === 200) {
        rating.current = await response.json();
      }

      setRestaurantState(restaurant);
    };
    fetchData();
  }, []);

  const splitCuisine = (str) => {
    var arr = str.split(',');
    return arr.map(i => <span key="i" className="">{i}</span>);
  }

  const goToLoggedIn = () => {
    navigate("/userLoggedIn");
  }



  return <>

    <div className="mt-32"></div>
    <div className="flex">

      <button type="button" className="flex justify-center items-center mr-14 h-[38px] w-30 px-4 mt-4 mb-4 bg-stone font-bold rounded-lg hover:bg-orange"
        onClick={() => goToLoggedIn()}>
        <BsSkipStartCircle className="icon" /> Home
      </button>
    </div>
    <div className="min-w-lg bg-[#eacaa9] rounded-lg">

      <div className="justify-center space-x-12 flex">

        <div>
          {restaurantState &&
            <div className="mx-auto justify-center px-5 lg:px-8 xl:px-24">
              <h1 className="text-center text-4xl uppercase font-bold p-4">{restaurantState.name}</h1>
            </div>}
          <div className="bg-[white] flex justify-center font-bold leading-8 rounded-lg p-4">
            <ul className=" text-center">
              {rating.current && <li className="flex justify-center">Rating: {rating.current.score} Stars</li>}
              {restaurantState.cuisineType && <li>{splitCuisine(restaurantState.cuisineType)}</li>}
              {restaurantState.website && <li><a href={restaurantState.website}>{restaurantState.website}</a></li>}
              <li>{restaurantState.address}</li>
              <li>
                {`${restaurantState.city}, `} {`${restaurantState.state} `}
              </li>
              {restaurantState.phone && <li>{restaurantState.phone}</li>}
              {restaurantState.email && <li>{restaurantState.email}</li>}
            </ul>
          </div>

          <div className="justify-center my-4 space-x-2 flex">
            <Link to={`/${appUserId}/${restaurantId}/meal/add`} type="button"
              className=" bg-[#197a0c] text-[white] uppercase px-4 py-2 rounded-full hover:bg-orange">Add Meal</Link>
          </div>
        </div>

      </div>


      <div className="flex justify-center p-4 bg-[#cfc6c6]"></div>

      <div className=" bg-[#eacaa9]">

        <h1 className="flex justify-center text-[black] py-6 text-3xl font-bold uppercase">Meals</h1>
        <div className="flex justify-center">
          {meals.current.length > 0
            ? meals.current.map(m =>
              <div className=" min-w-md max-w-md">
                <div key={m.mealId} className="rounded-lg m-2 overflow-hidden bg-[#cf582d] shadow-lg">
                  <div class>
                    <img className="h-64 rounded-t-lg justify-center" src={m.imageUrl} alt={m.title} />
                  </div>

                  <div className="px-6 py-4">
                    <div className="font-bold text-2xl mb-2 text-center">{m.title}</div>
                    <p className="text-[white] text-center">
                      {m.description}
                    </p>
                  </div>
                  <div className="flex justify-center mb-4 space-x-4">
                  </div>
                </div>
              </div>)

            : <div className="text-center p-6 text-xl font-bold">No Meals</div>}
        </div>
      </div>

      <div className="flex justify-center p-4 bg-[#cfc6c6]"></div>

      <div className="">
        <h1 className="flex justify-center text-[black] py-6 text-3xl font-bold uppercase">Comments</h1>
        <div className="justify-center my-4 space-x-2 flex">
            <Link to={`/commentForm/${appUserId}/${commentId}/${restaurantId}`} type="button"
              className=" bg-[#197a0c] text-[white] uppercase px-4 py-2 rounded-full hover:bg-amber">Add a Comment</Link>
          </div>
        <div className="flex justify-center">
          {comments.current.length > 0
            ? comments.current.map(c =>
              <div className="flex px-4 py-4">
                <div className="flex" key={c.commentId}>
                  <table className="max-w-5xl mx-auto table-auto">
                    <thead className="justify-between">
                      <tr className="bg-primary">

                        <th className="px-16 py-2">
                          <span className="text-gray-100 font-semibold">Date</span>
                        </th>

                        <th className="px-16 py-2">
                          <span className="text-gray-100 font-semibold">Time</span>
                        </th>
                        <th className="px-16 py-2">
                          <span className="text-gray-100 font-semibold">Comments</span>
                        </th>

                        {/* <th className="px-16 py-2">
                          <span className="text-gray-100 font-semibold">Setting</span>
                        </th> */}
                      </tr>
                    </thead>
                    <tbody className="bg-gray-200">
                      <tr className="bg-white border-b-2 border-gray-200">
                        <td className="px-16 py-2">
                          <span>{c.postDate.substring(0, 10)}</span>
                        </td>
                        <td className="px-16 py-2">
                          <span>{c.postDate.substring(11)}</span>
                        </td>
                        <td>
                          <span className="text-center ml-2 font-semibold">"{c.description}"</span>
                        </td>

                        {/* <td className="px-16 py-2">
                          <span className="text-yellow-500 flex">
                            <svg
                              xmlns="http://www.w3.org/2000/svg"
                              class="h-5 w-5 text-green-700 mx-2"
                              viewBox="0 0 20 20"
                              fill="currentColor"
                            >
                              <path
                                d="M17.414 2.586a2 2 0 00-2.828 0L7 10.172V13h2.828l7.586-7.586a2 2 0 000-2.828z"
                              />
                              <path
                                fill-rule="evenodd"
                                d="M2 6a2 2 0 012-2h4a1 1 0 010 2H4v10h10v-4a1 1 0 112 0v4a2 2 0 01-2 2H4a2 2 0 01-2-2V6z"
                                clip-rule="evenodd"
                              />
                            </svg>
                            <svg
                              xmlns="http://www.w3.org/2000/svg"
                              class="h-5 w-5 text-red-700"
                              viewBox="0 0 20 20"
                              fill="currentColor"
                            >
                              <path
                                fill-rule="evenodd"
                                d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
                                clip-rule="evenodd"
                              />
                            </svg>
                          </span>

                        </td>  */}
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>

            )

            : <div className="text-center p-6 text-xl font-bold">No Comments</div>}

        </div>
      </div>
    </div>

  </>

};