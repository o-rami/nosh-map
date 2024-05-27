import { createMeal, updateMeal, findMealById } from "../services/mealApi";
import { Link, useNavigate, useParams } from "react-router-dom";
import { useContext, useEffect, useState } from "react";
import { FaSave, FaBan } from "react-icons/fa";
import AuthContext from "../contexts/AuthContext";

import Errors from "./Errors";
import food4 from "../images/food4.png";

const EMPTY_MEAL = {
  mealId: 0,
  title: "",
  price: 0.0,
  imageUrl: "",
  description: "",
  lastUpdated: null,
  isPublic: true,
  appUserId: 0,
  restaurantId: 0,
};

function MealForm({ }) {

  const [meal, setMeal] = useState(EMPTY_MEAL);
  const [errors, setErrors] = useState([]);

  const { appUserId, mealId, restaurantId } = useParams();

  const auth = useContext(AuthContext);

  const navigate = useNavigate();

  useEffect(() => {
    if (mealId) {
      findMealById(mealId)
        .then((data) => setMeal(data))
        .catch((error) => {
          navigate("/error", {
            state: { msg: error },
          });
        });
    } else {
      setMeal(EMPTY_MEAL);
    }
  }, [mealId, navigate]);

  const handleChange = (event) => {
    const nextMeal = { ...meal };

    if (event.target.type === "checkbox") {
      nextMeal[event.target.name] = event.target.checked;
    } else {
      let nextValue = event.target.value;
      if (event.target.type === "number") {
        nextValue = parseFloat(nextValue, 10);
        if (isNaN(nextValue)) {
          nextValue = event.target.value;
        }
      }
      nextMeal[event.target.name] = nextValue;
    }
    
    setMeal(nextMeal);
  };

  const handleSaveMeal = (event) => {
    event.preventDefault();

    //meal.lastUpdated = getCurrentDate();
    // console.log(meal.lastUpdated);
    console.log(auth.user.token);
    if (meal.imageUrl === "") {
      meal.imageUrl = "https://www.svgrepo.com/download/24765/food.svg";
    }

    if (meal.mealId === 0) {
      meal.appUserId = appUserId;
      meal.restaurantId = restaurantId;
      createMeal(meal, auth.user.token)
        .then((data) => {
          navigate(`/${appUserId}/meals`, {
            state: {
              msg: `${meal.title} was added!`
            },
          });
        })
        .catch((errors) => setErrors(errors));
        console.log(errors);
    } else {
      updateMeal(meal, auth.user.token)
        .then(() => {
          navigate(`/${appUserId}/meals`, {
            state: {
              msg: `${meal.title} was updated!`,
            },
          });
        })
        .catch((errors) => setErrors(errors));
        console.log(errors);
    }
    console.log(errors);
  };

  const getCurrentDate = () => {
    var today = new Date();
    var currentMonth = today.getMonth();
    currentMonth = currentMonth < 10 ? '0' + currentMonth : currentMonth;
    var currentDay = today.getDate();
    currentDay = currentDay < 10 ? '0' + currentDay : currentDay;

    return today.getFullYear() + "-"
      + currentMonth + "-"
      + currentDay + "T"
      + today.getHours() + ":"
      + today.getMinutes() + ":"
      + today.getSeconds();
  }

  return <>
    <div className="mealForm mt-32"></div>
    <div className="flex justify-center my-6">
      <div className="">
        <img src={food4} alt="fish tacos" />
      </div>
      <div className="ml-8 w-1/3">
        <form className="max-w-xl bg-[#d15123] rounded-lg py-6" onSubmit={handleSaveMeal}>
          <h1 className="text-center uppercase text-xl font-bold">Add Meal</h1>

          <div className="flex flex-wrap mx-3 mb-6">
            <div className="w-full px-3">
              <label
                className="block uppercase tracking-wide text-[#0c0c0c] text-sm font-bold mb-2"
                htmlFor="title"
              >
                Title
              </label>
              <input
                className="appearance-none block w-full bg-[#ffffff] border rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-[#fae2d3]"
                id="title" name="title" type="text" value={meal.title} placeholder="What's the name of the meal?"
                onChange={handleChange}
                required
              />
            </div>
          </div>

          <div className="flex flex-wrap mx-3 mb-6">
            <div className="w-full px-3">
              <label
                className="block uppercase tracking-wide text-[#0c0c0c] text-sm font-bold mb-2"
                htmlFor="price"
              >
                Price
              </label>

              <input
                className="appearance-none block w-full bg-[#ffffff] border rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-[#fae2d3]"
                min="0.00" max="1000.00" step="0.01" id="price" name="price" type="number" placeholder="$" value={meal.price}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          <div className="flex flex-wrap mx-3 mb-6">
            <div className="w-full px-3">
              <label
                className="block uppercase tracking-wide text-[#0c0c0c] text-sm font-bold mb-2"
                htmlFor="imageUrl"
              >
                Image URL
              </label>
              <input
                className="appearance-none block w-full bg-[#ffffff] border rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-[#fae2d3]"
                id="imageUrl" name="imageUrl" type="text" value={meal.imageUrl}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          <div className="flex flex-wrap mx-3 mb-6">
            <div className="w-full px-3">
              <label htmlFor="description" className="block mb-2 uppercase text-[#0c0c0c] text-sm font-bold" >Description</label>
              <textarea id="description" name="description" rows="4" className="block p-2.5 leading-tight w-full text-sm rounded focus:outline-none focus:bg-[#fae2d3]"
                placeholder="Describe the dish here..." maxLength="500" value={meal.description} onChange={handleChange} />
            </div>
          </div>

          <div className="flex flex-wrap mx-3 mb-6">
            <div className="w-full px-3">
              <label htmlFor="isPublic" className="block font-bold">
                <input id="isPublic" name="isPublic" className="mr-3 leading-tight" type="checkbox" defaultChecked onChange={handleChange} />Public?</label>
            </div>
          </div>

          <div className="flex justify-center space-x-4">
            <button
              className="px-5 flex shadow bg-[#fff] hover:bg-[#55d849] focus:shadow-outline focus:outline-none font-bold py-3 rounded-full"
              type="button" onClick={handleSaveMeal}><FaSave /><span className="ml-2 uppercase">Save</span></button>
            <Link to={`/${appUserId}/meals`} type="button" title="Cancel"
              className="px-5 flex shadow bg-[#fff] hover:bg-[#cfc6c6] focus:shadow-outline focus:outline-none font-bold py-3  rounded-full"
            ><FaBan /><span className="ml-2 uppercase">Cancel</span></Link>
          </div>
          <div className="errors text-lg p-4">
            <Errors errors={errors} />
          </div>
        </form>
      </div>
    </div >
  </>
}

export default MealForm;
