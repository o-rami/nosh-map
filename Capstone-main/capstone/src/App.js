import React, { useState, useEffect } from 'react';
import { LoadScript } from "@react-google-maps/api";
import Header from './components/Header';
import Footer from './components/Footer';
import HomePage from './components/HomePage';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { useLocation } from 'react-router-dom';
import Login from './components/Login';
import ProfileForm from './components/ProfileForm';
import LoggedIn from './components/LoggedIn';
import ProfileList from './components/ProfileList';
import MealList from './components/MealList';
import MealForm from './components/MealForm';
import Devs from './components/Devs';
import About from './components/About'
import jwtDecode from "jwt-decode";
import AuthContext from './contexts/AuthContext';
import NotFound from "./components/NotFound";
import Errors from './components/Errors';
import SignUp from './components/SignUp';
import GuestView from './components/GuestView';
import UserIndex from './components/UserIndex';
import UserFavorites from './components/UserFavorites';
import ConfirmDeleteMeal from './components/ConfirmDeleteMeal';
import RestaurantInfo from './components/RestaurantInfo';
import Add from './components/Add';
import CommentForm from './components/CommentForm';
//import Map from './components/Dashboard/Body/GoogleMap/Map';

const LOCAL_STORAGE_TOKEN_KEY = "capstoneToken";

const libs = ["places"];
const key = "AIzaSyBeEVEGLVLMX7OjUXKsZXdsDbg3SAo8-0Q";

function App() {

  const [user, setUser] = useState(null);
  const [restoreLoginAttemptCompleted, setRestoreLoginAttemptCompleted] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
    if (token) {
      login(token);
    }
    setRestoreLoginAttemptCompleted(true);
  }, []);

  const login = (token) => {
    localStorage.setItem(LOCAL_STORAGE_TOKEN_KEY, token);

    const { sub: username, authorities: authoritiesString } = jwtDecode(token);

    const roles = authoritiesString.split(',');

    const user = {
      username,
      roles,
      token,
      hasRole(role) {
        return this.roles.includes(role);
      }
    };

    console.log(user);


    setUser(user);

    return user;
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem(LOCAL_STORAGE_TOKEN_KEY);
  };

  const auth = {
    user: user ? { ...user } : null,
    login,
    logout
  };


  if (!restoreLoginAttemptCompleted) {
    return null;
  }

  // const location = useLocation();

  return (<>
    <AuthContext.Provider value={auth}>
      <Router>
      {((window.location.pathname !== "/userLoggedIn") && (window.location.pathname !== "/add")) ?<Header/>:<></>}
        {/* <Header />
        {location.pathname !== '/userLoggedin' && <Header />} */}

        <Routes>

          <Route path="/"
            element={<HomePage />} />
          <Route path="/login"
            element={<Login />} />
          <Route path="/signup"
            element={<SignUp />} />

          <Route path="/profileList"
            element={<ProfileList />} />

          <Route path="/profileForm"
            element={<ProfileForm />} />

          <Route path="/editProfile/:appUserId"
            element={<ProfileForm />} />

          <Route path="/userLoggedIn"
            element={<LoggedIn />} />
          <Route path="/guestView" element={<GuestView />} />
          <Route path="/userIndex" element={<UserIndex />} />
          <Route path='/userFavorites' element={<UserFavorites />} />
          <Route path='/restaurantInfo/:restaurantId' element={<RestaurantInfo />} />
          <Route path='/add' element={<Add />} />
          <Route path="*" element={<NotFound />} />

          <Route
            path="/devs"
            element={<Devs />} />
          
          <Route
            path="/commentForm/:appUserId/:commentId/:restaurantId"
            element={<CommentForm />} />
          <Route
            path="/about"
            element={<About />} />

          <Route
            path="/:appUserId/:restaurantId/meal/add"
            element={<MealForm />} />
          <Route
            path="/:appUserId/meals/edit/:mealId"
            element={<MealForm />} />
          <Route
            path="/:appUserId/meals/delete/:mealId"
            element={<ConfirmDeleteMeal />} />
          <Route
            path="/:appUserId/meals"
            element={<MealList />} />

          <Route path="/errors" element={<Errors />} />


        </Routes>
        <Footer />
      </Router>
    </AuthContext.Provider>

  </>
  );
};

export default App;
