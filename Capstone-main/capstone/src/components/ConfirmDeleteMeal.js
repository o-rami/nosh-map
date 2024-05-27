import { useContext, useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { findMealById, deleteMealById } from "../services/mealApi";
import AuthContext from "../contexts/AuthContext";

function ConfirmDeleteMeal() {

  const [meal, setMeal] = useState({ title: '' });

  const { appUserId, mealId } = useParams();

  const auth = useContext(AuthContext);

  const navigate = useNavigate();

  useEffect(() => {
    findMealById(mealId)
      .then(data => setMeal(data))
      .catch(() => {
        navigate("/not-found", {
          state: { msg: `Meal: ${mealId} was not found.` }
        });
      });
  }, [mealId, navigate]);

  const handleDelete = () => {
    deleteMealById(mealId, auth.user.token)
      .then(res => {
        navigate(`/${appUserId}/meals`, {
          state: { msg: `${meal.title} was deleted.` }
        });
      })
      .catch(() => {
        navigate(`/${appUserId}/meals`, {
          state: { msg: `Meal: ${mealId} was not found.` }
        });
      });
  }

  return <>
    <div className="mt-32"></div>

    <h1 className="text-center uppercase text-3xl font-bold p-4">Delete Meal?</h1>
    <div className="justify-center flex">
      <div className="flex-col max-w-lg rounded-lg my-5 pb-4 bg-[#e56c21]">
        <div>
          <img src={meal.imageUrl} className=" rounded-t-lg object-cover" alt={meal.description} />
        </div>

        <div>
          <div className="rounded p-4 min-w-full flex-col justify-center">
            <p className="text-center text-3xl font-medium text-[black]">Are you sure you want to permanently delete this meal? </p>
            <h2 className="text-center font-bold text-[black] py-4"> {meal.title}</h2>
          </div>

          <div className="flex justify-center space-x-10 mb-4">
            <button type="button" className="bg-[red] text-[white] uppercase p-4 rounded-full" onClick={handleDelete}>Delete</button>
            <Link to={`/${appUserId}/meals`} type="button" className=" bg-[grey] text-[white] uppercase p-4 rounded-full">Cancel</Link>
          </div>
        </div>

      </div>
    </div>

  </>
}

export default ConfirmDeleteMeal;