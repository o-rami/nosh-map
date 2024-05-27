import { React, useEffect, useContext, useState } from "react";
import AuthContext from "../contexts/AuthContext";
import Dashboard from "./Dashboard";
import Top from "./Dashboard/Body/Top/Top";
import '../styles/userfavorites.css';
import '../index.css';

export default function UserFavorites() {
  const [meals, setMeals] = useState([]);
  const [commentsMap, setCommentsMap] = useState(new Map());
  const [ratingMap, setRatingMap] = useState(new Map());

  const [isToggled, setIsToggled] = useState(false);
  const [selectedMeal, setSelectedMeal] = useState(null);

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
        `http://localhost:8080/api/meal/appUserId/${user.appUserId}`,
        init
      );
      const mealsData = await response.json();
      console.log("meals: ", mealsData);

      setMeals(mealsData);

      for (let i = 0; i < mealsData.length; i++) {
        const mealId = mealsData[i].mealId;

        response = await fetch(
          `http://localhost:8080/api/comment/mealId/${mealId}`,
          init
        );
        let comments = await response.json();
        setCommentsMap((prevCommentsMap) => prevCommentsMap.set(mealId, comments));
        console.log("commentsMap", commentsMap);

        try {
          response = await fetch(
            `http://localhost:8080/api/rating/${mealId}/${user.appUserId}`,
            init
          );
          let rating = await response.json();
          setRatingMap((prevRatingMap) => prevRatingMap.set(mealId, rating));
        } catch (error) {
          console.error("Error retrieving rating:", error);
        }
      }
    };

    fetchData();
  }, [auth.user.username]);


  const handleToggle = (meal) => {
    if (isToggled && selectedMeal && selectedMeal.mealId === meal.mealId) {
      setIsToggled(false);
      setSelectedMeal(null);
    } else {
      setIsToggled(true);
      setSelectedMeal(meal);
    }
  }

  

  return (
    <>
      <div className="flex mt-36">
        <Dashboard />
        <div className="ml-8">
          <div className="flex justify-center">
          <h1 className="meals flex text-5xl font-bold text-transform: uppercase" > meals</h1>
          </div>
          <div className="listingSection justify-start flex">
            <div className="favoritesPage justify-start">
            </div>
            <div className="mealContainer justify-center grid grid-cols-4 gap-4">
              {meals.map((meal) => (
                <div key={meal.mealId} className="singleMeal px-4 hover:bg-amber" onClick={() => handleToggle(meal)}>
                  <div className="singleItem text-center">
                    <img src={meal.imageUrl} alt="mealImg" className="mealImg rounded-lg" />
                    <strong className="text-lg mt-9">{meal.title}</strong>
                    <p className="text-primary font-bold">Rating: {ratingMap.get(meal.mealId)?.score}</p>
                  </div>

                  {isToggled && selectedMeal && selectedMeal.mealId === meal.mealId && (
                    <div className="meal-preview">
                      <div className="preview rounded-lg">
                      <img src={meal.imageUrl} alt="preview-image" className="preview-img rounded-lg" />
                        <h3 className="font-bold text-2xl mt-8">{meal.title}</h3>
                        <div className="descriptionTitle font-semibold">Decription:</div>
                        <div className="decription">{meal.description}</div>
                      </div>
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </>
  );
}