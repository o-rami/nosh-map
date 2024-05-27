import { React, useEffect, useContext, useRef, useState } from "react";
import AuthContext from '../../../../contexts/AuthContext';
import './favorite.css';
import { BiRightArrowAlt } from 'react-icons/bi';
import { BsFillSuitHeartFill } from 'react-icons/bs';
import { Link } from 'react-router-dom';


export default function FavoriteList() {
  const restaurants = useRef([]);
  const [restaurantList, setRestaurantList] = useState([]);
  const mealsMap = useRef(new Map());
  const commentsMap = useRef(new Map());
  const ratingMap = useRef(new Map());

  const auth = useContext(AuthContext);

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

      const fetchDataForRestaurant = async (restaurant) => {
        try {
          response = await fetch(`http://localhost:8080/api/meal/restaurantId/${restaurant.restaurantId}`, init);
          if (!response.ok) {
            throw new Error('Error fetching meals data');
          }
          const meals = await response.json();
          mealsMap.current.set(`${restaurant.restaurantId}`, meals);

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
        } catch (error) {
          console.error(error);
     
        }
      };

      for (let i = 0; i < userRestaurants.length; i++) {
        await fetchDataForRestaurant(userRestaurants[i]);
      }

      setRestaurantList(userRestaurants);
    };

    fetchData();
  }, [auth.user.username]);

  const getTopRatedMeals = () => {

    const mealsWithRatings = [];


    mealsMap.current.forEach((meals, restaurantId) => {
      meals.forEach((meal) => {
        const rating = ratingMap.current.get(restaurantId);
        mealsWithRatings.push({ meal, rating });
      });
    });


    mealsWithRatings.sort((a, b) => b.rating.score - a.rating.score);

    return mealsWithRatings.slice(0, 3);
  };

  return (
    <div className="listingSection">
      <div className="heading flex">
        <h1 className="text-xl font-bold px-4 py-4 mt-4">My Favorite Meals</h1>
        {/* <Link to="/userFavorites"
          title="userFavorites">
          <button className="flex justify-center items-center mr-14 h-[38px] w-30 px-4 mt-4 bg-stone font-bold rounded-lg hover:bg-orange">
            See All <BiRightArrowAlt className="icon" />
          </button>
        </Link> */}
      </div>
      <div className="secContainer flex">
        {getTopRatedMeals().map((item, index) => (
          <div key={index} className="singleItem">
            <BsFillSuitHeartFill className="icon" />
            <img src={item.meal.imageUrl} alt="img" className="rounded-lg" />
            {console.log(item.meal)}
            <h3 className="text-md px-2 font-semibold">{item.meal.title}</h3>
            <p className="text-primary">Rating: {item.rating.score}</p>
          </div>
        ))}
      </div>
    </div>
  );
};