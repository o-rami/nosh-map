import React, { useContext, useEffect, useRef, useState, useLayoutEffect } from "react";
import './top.css';
import AuthContext from "../../../../contexts/AuthContext";
import { Link, useNavigate } from 'react-router-dom';
import GMap from "../../../GMap";
import video from '../../../../images/foodvid.mp4';
import { getAppUserId } from '../../SideBar/Sidebar';

import { AiOutlineArrowRight } from 'react-icons/ai';
import img from '../../../../images/leftCard.png'
import img2 from '../../../../images/leftCard2.png'
import SideBar from "../../SideBar/Sidebar";
import Welcome from "../Welcome/Welcome";


export default function Top() {

    const restaurants = useRef([]);
    const [restaurantList, setRestaurantList] = useState([]);
    const mealsMap = useRef(new Map());
    const commentsMap = useRef(new Map());
    const ratingMap = useRef(new Map());
    let userId = useRef(0);

    const auth = useContext(AuthContext);
    const [totalRatings, setTotalRatings] = useState(0);
    const [totalMeals, setTotalMeals] = useState(0);
    const [appUserId, setAppUserId] = useState(0);


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
        userId.current = user.appUserId;
        setAppUserId(user.appUserId);
    }

    useEffect(() => {
        const fetchData = async () => {
            const init = {
                headers: {
                    "Content-Type": "application/json",
                    Accept: "application/json",
                },
                method: "GET",
            };

            let response = await fetch(`http://localhost:8080/api/appUser/${auth.user.username}`, init);
            const user = await response.json();

            response = await fetch(`http://localhost:8080/api/restaurant/appUser/${user.appUserId}`, init);
            const userRestaurants = await response.json();
            console.log(userRestaurants, "RESTAURANTS IN TOP - INIT CALL")

            const fetchDataForRestaurant = async (restaurant) => {
                try {
                  response = await fetch(`http://localhost:8080/api/meal/restaurantId/${restaurant.restaurantId}`, init);
                  if (!response.ok) {
                    throw new Error('Error fetching meals data');
                  }
                  const meals = await response.json();
                  mealsMap.current.set(`${restaurant.restaurantId}`, meals);
                  setTotalMeals(totalMeals => totalMeals + meals.length);
              
                  response = await fetch(`http://localhost:8080/api/comment/restaurantId/${restaurant.restaurantId}`, init);
                  if (!response.ok) {
                    throw new Error('Error fetching comments data');
                  }
                  const comments = await response.json();
                  commentsMap.current.set(`${restaurant.restaurantId}`, comments);
              
                  response = await fetch(`http://localhost:8080/api/rating/${restaurant.restaurantId}/${user.appUserId}`, init);
                  if (!response.ok) {
                    throw new Error('Error fetching rating data');
                  }
                  const rating = await response.json();
                  ratingMap.current.set(`${restaurant.restaurantId}`, rating);
                  setTotalRatings(totalRatings => totalRatings + rating.score);
                } catch (error) {
                  console.error(error);
                  // Handle the error, such as showing an error message or fallback data
                }
              };

            for (let i = 0; i < userRestaurants.length; i++) {
                await fetchDataForRestaurant(userRestaurants[i]);
            }

            setRestaurantList(userRestaurants);

            getAppUserId();
        };

        fetchData();
    }, [auth.user.username]);


    return (<>
        <Welcome />
        <div className="cardSection flex justify-items-center">
            <div className="rightCard flex sm:flex-grow">
                <h1 className="text-xl font-bold justify-items-center">Rate and Review Restuarants</h1>
                <p className="font-semibold">Where foodies unite, flavors take flight. Discover, rate, and savor the culinary delights that delight your soul.</p>

                <div className="buttons flex">
                    <button className="flex justify-center items-center mr-14 h-[38px] w-35 px-4 bg-stone font-bold rounded-lg hover:bg-orange">
                        <Link to="/add"
                            title="add">
                            Explore Restaurants
                        </Link>
                    </button>
                </div>

                <div className="videoDiv">
                    <video src={video} autoPlay loop muted></video>
                </div>
            </div>

            <div className="leftCard flex sm:flex-grow">
                <div className="main flex">
                    <div className="textDiv">
                        <h1 className="text-xl font-bold">My Stats</h1>

                        <div className="flex">
                            <span>Ratings<br />
                                <small>{totalRatings}</small>
                            </span>
                            <span>Meals <br />
                                <small>{totalMeals}</small>
                            </span>
                        </div>
                        <Link to={`/${appUserId}/meals`}
                            title="addMeal"
                            className="flex link">
                            View Meals <AiOutlineArrowRight className="icon" />
                        </Link>

                    </div>

                    <div className="imgDiv">
                        <img src={img2} alt="left img" className="animate-bounce"/>
                    </div>
                </div>
            </div>
        </div>
    </>
    )
}