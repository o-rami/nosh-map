import { useEffect, useState } from "react";
import { Link, useLocation, useParams } from "react-router-dom";
import { findAllMealsByUserId } from "../services/mealApi";
import Dashboard from "./Dashboard";

function MealList() {

  const [meals, setMeals] = useState([]);

  const location = useLocation();

  const { appUserId } = useParams();

  useEffect(() => {
    findAllMealsByUserId(appUserId)
      .then(data => setMeals(data));
  }, [appUserId]);

  return <>
    <div className="mt-32"></div>


    <h1 className="text-center text-3xl font-bold text-[black] mb-4 Meals">All Meals</h1>
    <div className="flex justify-center">
      <Link to="/userLoggedIn"
      title='home'
      className="h-[38px] w-20 bg-orange font-bold px-4 py-2 justify-center rounded-xl hover:bg-lime">
        Home
        </Link>
    </div>

    <div className="rounded-lg text-center text-xl justify-center p-4">
      {location.state && <div className="">
        {location.state.msg}
      </div>}
    </div>

    <div className="w-screen px-24 mb-80">

      <div className="xs:flex-col sm:flex flex-wrap py-4 justify-around">
        {meals && meals.length > 0
          ? meals.map(m =>
            <div key={m.mealId} className="w-1/4 min-w-min rounded-lg m-2 overflow-hidden bg-[#e56c21] shadow-lg">
              <div class>
                <img className=" rounded-t-lg justify-center" src={m.imageUrl} alt={m.title} />
              </div>
              <div className="px-6 py-4">
                <div className="font-bold text-xl mb-2 text-center">{m.title}</div>
                <p className="text-[white] text-center">
                  {m.description}
                </p>
              </div>
              <div className="flex justify-center mb-4 p-2 space-x-4">
                <Link to={`/${appUserId}/meals/edit/${m.mealId}`} className="uppercase px-6 shadow bg-[#fff] hover:bg-[#c5c5c5] focus:shadow-outline focus:outline-none font-bold py-3 rounded-full">Edit</Link>
                <Link to={`/${appUserId}/meals/delete/${m.mealId}`} className="uppercase px-6 shadow bg-[#c51f1f] hover:bg-[#830000] focus:shadow-outline focus:outline-none font-bold py-3 rounded-full">Delete</Link>
              </div>
            </div>)

          : <div className="text-center p-6 text-xl font-bold">No Meals</div>}

      </div>
    </div>

  </>;
}

export default MealList;