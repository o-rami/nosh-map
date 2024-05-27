import { React, useState, useEffect } from "react";
import GMapGuest from "./GMapGuest";
import { LoadScript } from "@react-google-maps/api";
import Header from "./Header";

const libs = ["places"];
const key = "AIzaSyBeEVEGLVLMX7OjUXKsZXdsDbg3SAo8-0Q";

export default function GuestView() {

  // CHANGE SO THAT WAY THE DETAILS MAKE SENSE WITH A LIST INSTEAD OF THE SAME SETUP FOR THE SINGLE ONE IN GMAP
  // Both nearby and detaisl have place_id, match with that

  const [restaurantList, setRestaurantList] = useState([]);
  const [placeDetails, setPlaceDetails] = useState({});
  let detailsList = [];

  let detailRequest = {
    placeId: "placeholder",
    fields: ["formatted_address", "formatted_phone_number", "website", "place_id"]
  }

  const getRestaurants = (list, map) => {
    //getDetails(list, map);
    setRestaurantList(list);
    console.log(list);
  }
  /*

  const getDetails = (list, map) => {
      let PlacesService = new window.google.maps.places.PlacesService(map);
      detailsList = [];
      for (let i = 0; i < list.length; i ++) {
          detailRequest.placeId = list[i].place_id;
          PlacesService.getDetails(detailRequest, detailsCallback);
      }
      setPlaceDetails(detailsList);
      
  }

    function detailsCallback(place, status) {
  if (status === window.google.maps.places.PlacesServiceStatus.OK) {
    console.log('place in callback: ', place);
    let details = {};
    if (place.hasOwnProperty('formatted_address')) {
      details.formatted_address = place.formatted_address;
    }
    if (place.hasOwnProperty('formatted_phone_number')) {
      details.formatted_phone_number = place.formatted_phone_number;
    }
    if (place.hasOwnProperty('opening_hours') && place.opening_hours.hasOwnProperty('weekday_text')) {
      details.opening_hours = place.opening_hours;
    }
    if (place.hasOwnProperty('website')) {
      details.website = place.website;
    }
    details.place_id = place.place_id;
        detailsList.push(details);
      }
    }*/

  // ATTRIBUTES AQUIRED WITHOUT AN ADDITIONAL GET DETAILS SEARCH:
  // PLACE_ID (FOR LOOKUP), NAME, OPEN_NOW (BOOLEAN), PRICE_LEVEL, RATING (FROM GOOGLE), USER_RATING_TOTAL

  return (<>
    <div id="guestPage" className="flex mt-20">
      <LoadScript googleMapsApiKey={key} libraries={libs}>
        <GMapGuest onReload={getRestaurants}/>
      </LoadScript>

      <div id="in" className="mt-20 inline-block overflow-x">
        <table className="min-w-full bg-white rounded-lg shadow-md table-auto overflow-scroll w-full">
          <thead>
            <tr>
              <th scope="col" className="px-4 py-4">Name </th>
              <th scope="col" className="px-4 py-4">Open/Closed </th>
              <th scope="col" className="px-4 py-4">Price Level </th>
              <th scope="col" className="px-4 py-4">Rating</th>
              <th scope="col" className="px-4 py-4">User Ratings Total</th>
            </tr>
          </thead>
          <tbody >
            {restaurantList.map((r, index) => (
              <tr key={index}>
                <td className="px-4 py-4 text-center">{r.name}</td>
                <td className="px-4 py-4 text-center">{r.opening_hours?.open_now ? "Open" : "Closed"}</td>
                <td className="px-4 py-4 text-center">{r.price_level}</td>
                <td className="px-4 py-4 text-center">
                  <div className="flex items-center">
                    {[...Array(Math.round(r.rating))].map((_, starIndex) => (
                      <svg
                        key={starIndex}
                        className="w-4 h-4 text-yellow-300 mr-1"
                        aria-hidden="true"
                        xmlns="http://www.w3.org/2000/svg"
                        fill="currentColor"
                        viewBox="0 0 22 20"
                      >
                        <path d="M20.924 7.625a1.523 1.523 0 0 0-1.238-1.044l-5.051-.734-2.259-4.577a1.534 1.534 0 0 0-2.752 0L7.365 5.847l-5.051.734A1.535 1.535 0 0 0 1.463 9.2l3.656 3.563-.863 5.031a1.532 1.532 0 0 0 2.226 1.616L11 17.033l4.518 2.375a1.534 1.534 0 0 0 2.226-1.617l-.863-5.03L20.537 9.2a1.523 1.523 0 0 0 .387-1.575Z" />
                      </svg>
                    ))}
                    <p className="ml-2 text-sm font-bold text-gray-900 text-orange">{r.rating}</p>
                    <span className="w-1 h-1 mx-1.5 bg-gray-500 rounded-full dark:bg-gray-400"></span>
                  </div>
                </td>
                <td className="px-4 py-4 text-center">{r.user_ratings_total}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  </>
  );
}