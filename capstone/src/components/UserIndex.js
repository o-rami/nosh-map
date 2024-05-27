import { React, useEffect, useContext, useState } from "react";
import AuthContext from "../contexts/AuthContext";
import Dashboard from "./Dashboard";
import Top from "./Dashboard/Body/Top/Top";
import '../styles/userfavorites.css';
import { Link } from 'react-router-dom';

export default function UserIndex() {
    const [restaurantList, setRestaurantList] = useState([]);
    const [mealsMap, setMealsMap] = useState(new Map());
    const [commentsMap, setCommentsMap] = useState(new Map());
    const [ratingMap, setRatingMap] = useState(new Map());


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
            let response = await fetch(
                `http://localhost:8080/api/appUser/${auth.user.username}`,
                init
            );
            const user = await response.json();
            console.log("user: ", user);

            response = await fetch(
                `http://localhost:8080/api/restaurant/listId/${user.appUserId}`,
                init
            );
            const restaurants = await response.json();
            console.log("restaurants right after assignment in userIndex: ", restaurants);

            for (let i = 0; i < restaurants.length; i++) {
                const restaurantId = restaurants[i].restaurantId;

                response = await fetch(
                    `http://localhost:8080/api/meal/restaurantId/${restaurantId}`,
                    init
                );
                let meals = await response.json();
                setMealsMap((prevMealsMap) => prevMealsMap.set(restaurantId, meals));


                response = await fetch(
                    `http://localhost:8080/api/comment/restaurantId/${restaurantId}`,
                    init
                );
                if (response.status === 200) {
                let comments = await response.json();
                setCommentsMap((prevCommentsMap) => prevCommentsMap.set(restaurantId, comments));
                console.log("commentsMap", commentsMap);
                }


                response = await fetch(
                    `http://localhost:8080/api/rating/${restaurantId}/${user.appUserId}`,
                    init
                );
                if (response.status === 200) {
                let rating = await response.json();
                setRatingMap((prevRatingMap) => prevRatingMap.set(restaurantId, rating));
                }

            }

            setRestaurantList(restaurants);
        };

        fetchData();
    }, [auth.user.username]);


    return (<>
        <div className="flex mt-36">
            <Dashboard />
            <div className="ml-5">
                <h1 className="text-3xl font-bold px-4 mt-4">User Index</h1>
                <div className="listingSection justify-start flex">
                    <div className="favoritesPage justify-start mt-20">
                    </div>
                    <div className="indexContainer justify-center mt-6 grid grid-cols-4 gap-4">
                        {restaurantList.map((restaurant) => (
                            <div key={restaurant.restaurantId} className="singleIndex px-4 bg-amber rounded-lg hover:bg-primary cursor-pointer">
                                <Link to={`/restaurantInfo/${restaurant.restaurantId}`}
                                title="restrauntInfo">
                                <h3 className="text-xl font-bold px-4 mt-4 text-center">{restaurant.name}</h3>
                                <h3 className="text-md font-bold px-4 text-center">{restaurant.city}, {restaurant.state}</h3>
                                <p className="text-md font-semibold text-lime text-center pt-4">Meals Count: {mealsMap.get(restaurant.restaurantId)?.length}</p>
                                <p className="text-md font-semibold text-white text-center py-2">Comments Count: {commentsMap.get(restaurant.restaurantId)?.length}</p>
                                </Link>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    </>
    );
}



