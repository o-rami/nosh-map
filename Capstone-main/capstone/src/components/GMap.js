import { React, useState, useEffect, useContext }from "react";
import { GoogleMap, MarkerF, InfoWindow } from "@react-google-maps/api";
import AuthContext from '../contexts/AuthContext';

let Empty_Restaurant = {
  restaurantId: 0, // POS OR ZERO
  googlePlaceId: null,
  name: '', // NOT NULL
  address: '', // NOT NULL
  zipCode: '', // NOT NULL, see about breaking up the address
  website: null,
  email: null,
  phone: null,
  latitude: 0.0,
  longitude: 0.0,
  state: '', // NOT NULL
  city: '', // NOT NULL
  hoursInterval: null,
  cuisineType: null
}

  //AIzaSyBxLfsU34vWfW7-qkYyOgFME0ialJE6rgo

export default function GMap(props) {

  const [placesList, setPlacesList] = useState([]);
  const [selectedPlace, setSelectedPlace] = useState(null);
  const [placeDetails, setplaceDetails] = useState({});
  const [position, setPosition] = useState(window.google.maps.LatLng(33.9519347, -83.3575670));
  const [mapRef, setMapRef] = useState(null)
  const [infoWindowRef, setInfoWindowRef] = useState(null);

  const auth = useContext(AuthContext);

  let PlacesService = {};

const getCurrentLocation = () => {
  if ("geolocation" in navigator) {
    // Prompt user for permission to access their location
    navigator.geolocation.getCurrentPosition(
      // Success callback function
      (position) => {
        setPosition(new window.google.maps.LatLng(position.coords.latitude,position.coords.longitude));
      },
      // Error callback function
      (error) => {
        // Handle errors, e.g. user denied location sharing permissions
        console.error("Error getting user location:", error);
      }
    );
  } else {
    // Geolocation is not supported by the browser
    console.error("Geolocation is not supported by this browser.");
  }
}

  //const Athens = new window.google.maps.LatLng(33.9519347, -83.3575670);

  let request = {
    location: position,
    radius: 2500,
    type: ['restaurant'],
    keyword: 'restaurant',
  };

  let detailRequest = {
    placeId: "placeholder",
    fields: ["formatted_address", "address_components", "formatted_phone_number", "opening_hours", "website"]
  }

  const containerStyle = {
    width: '700px',
    height: '700px'
  };

  const onMapLoad = (map) => {
    console.log("onMapLoad", map);
    console.log('position: ', position);
    if (map === null || map === undefined || typeof map === "undefined") {
      map = mapRef.getInstance();
    }
    console.log('map: ', map);
    PlacesService = new window.google.maps.places.PlacesService(map);
    console.log('PlacesServices: ', PlacesService);
    request.location = position;
    if (PlacesService) {
    PlacesService.nearbySearch(request, callback);
    }
  }

  const onMapReload = () => {
    console.log('mapRef = ', mapRef);
    const gMap = mapRef.state.map;
    console.log('map: ', gMap);
    PlacesService = new window.google.maps.places.PlacesService(gMap);
    console.log('PlacesServices: ', PlacesService);
    request.location = mapRef.state.map.center;
    if (PlacesService) {
    PlacesService.nearbySearch(request, callback);
    }
  }

  function callback(results, status) {
    console.log('results: ', results);
    let placesAdded = []
    if (status === window.google.maps.places.PlacesServiceStatus.OK) {
      for (var i = 0; i < results.length; i++) {
        placesAdded.push(results[i]);
        console.log(results[i])
      }
    }
    setPlacesList(placesAdded);
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
      if (place.hasOwnProperty('address_components')) {
        details.address_components = place.address_components;
      }
      setplaceDetails(details);
    }
  }

  const onMarkerLoad = marker => {
    console.log('marker: ', marker);
  }

  function getDetails (place, event) {
    console.log('event: ', event)
    console.log('place: ', place);
    if (place) {
    PlacesService.current = new window.google.maps.places.PlacesService(event.domEvent.srcElement.offsetParent.children[1]);
    detailRequest.placeId = place.place_id;
    PlacesService.current.getDetails(detailRequest, detailsCallback);
    }
  }

  const fetchCalls = async (addedRestaurant) => {
    console.log('auth', auth);
    const init = {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${auth.user.token}`,
      },
      body: JSON.stringify(addedRestaurant),
      method: "POST",
    };

    const userInit = {
      headers: {
        'Content-Type': 'application/json', 
        'Accept': 'application/json',
      }, 
      method: "GET",
    };

    const aurInit = {
      headers: {
        'Content-Type': 'application/json', 
        'Accept': 'application/json',
        'Authorization': `Bearer ${auth.user.token}`,
      }, 
      body: "", 
      method: "POST",
    };

    let response = await fetch('http://localhost:8080/api/restaurant', init);
    const restaurant = await response.json();
    response = await fetch(`http://localhost:8080/api/appUser/${auth.user.username}`, userInit);
    const user = await response.json();
    aurInit.body = JSON.stringify({
      appUserId: user.appUserId, 
      restaurantId: restaurant.restaurantId, 
      identifier: `${user.appUserId}-${restaurant.restaurantId}`
    });
    response = await fetch(`http://localhost:8080/api/restaurant/appUser`, aurInit);
    const aur = await response.json();
  }

  const getAddress = (details) => {
    let addressComponents = {
      address: '',
      city: '',
      state: '',
      zipCode: '', 
    };
    console.log('placeDetails in getAddress: ', details);
    if (details.hasOwnProperty('address_components')) {
    for (let i = 0; i < details.address_components.length; i++) {
      if (details.address_components[i].types.includes('subpremise') || 
      details.address_components[i].types.includes('street_number')) {
        addressComponents.address +=`${details.address_components[i].long_name} `;
      }
      if (details.address_components[i].types.includes('route')) {
        addressComponents.address +=`${details.address_components[i].short_name}`;
      }
      if (details.address_components[i].types.includes('locality') || details.address_components[i].types.includes('administrative_area_level_3')) {
        addressComponents.city += details.address_components[i].long_name;
      }
      if (details.address_components[i].types.includes('administrative_area_level_1')) {
        addressComponents.state += details.address_components[i].short_name;
      }
      if (details.address_components[i].types.includes('postal_code')) {
        addressComponents.zipCode += details.address_components[i].long_name;
      }
      if (details.address_components[i].types.includes('postal_code_suffix')) {
        addressComponents.zipCode += details.address_components[i].long_name;
      }
    }
  }
  console.log('addessComponents: ', addressComponents);
    return addressComponents;
  }

  const addToIndex = (details, place) => {
    // HAS ACCESS TO BOTH SELECTEDPLACE AND PLACEDETAILS
    // MAKE INTO A NEW RESTARUNT OBJECT BASED ON WHAT EACH HAS AND THEN ADD IT

    if (details.hasOwnProperty('address_components')) {
    let address = getAddress(details);

    let addedRestaurant = Empty_Restaurant;
    console.log('place in addToIndex: ', place);
    console.log('details in addToIndex: ', details);

      addedRestaurant.googlePlaceId = place.place_id;
      addedRestaurant.name = place.name; // NOT NULL
      addedRestaurant.address = address.address; // NOT NULL
      addedRestaurant.zipCode = address.zipCode; // NOT NULL
      if (details.hasOwnProperty('website')) {
        addedRestaurant.website = details.website}
      //email: '';
      if (details.hasOwnProperty('formatted_phone_number')) {
        addedRestaurant.phone = details.formatted_phone_number;
      }
      console.log('LAT', place.geometry.location.lat());
      console.log('LNG', place.geometry.location.lng());
      addedRestaurant.latitude = place.geometry.location.lat();
      addedRestaurant.longitude = place.geometry.location.lng();
      addedRestaurant.state = address.state; // NOT NULL
      addedRestaurant.city = address.city; // NOT NULL
      if ((details.hasOwnProperty('opening_hours')) && (details.opening_hours.hasOwnProperty('weekday_text'))) {
        addedRestaurant.hoursInterval = details.opening_hours.weekday_text.join(',');
      }
      //cuisineType: ''
    
      console.log('addedRestaurant: ', addedRestaurant);

      fetchCalls(addedRestaurant);
      //infoWindowRef.props.children.props.children[5] = {type: "h1", children: "added to list!!!!"};
      infoWindowRef.props.onCloseClick.apply();
      console.log(infoWindowRef);
      props.setMessage(`${selectedPlace.name} was added to your index!`);
    }
  }

  return (
    <div id='googleMap'>
      <GoogleMap
        ref={ref => setMapRef(ref)}
        onLoad={(map) => {getCurrentLocation(); onMapLoad(map)}}
        mapContainerStyle={containerStyle}
        center={position}
        zoom={13}
        onIdle={() => {onMapReload()}}
        id="mainMap"
      >
            {placesList.map((p) => (
            <MarkerF
            key={p.place_id}
            onLoad={onMarkerLoad}
            position={{lat: p.geometry.location.lat(),
            lng: p.geometry.location.lng()}}
            title={ `${p.place_id}-${p.name}` }
            onClick={(evt) => {
              setSelectedPlace(p);
            getDetails(p, evt)}}
            />
            ))
          }
          {console.log('placeDetails: ', placeDetails)}
        {selectedPlace && (
          <InfoWindow
          ref={ref => setInfoWindowRef(ref)}
            onCloseClick={() => { setSelectedPlace(null) }}
            position={{lat: selectedPlace.geometry.location.lat(),
              lng: selectedPlace.geometry.location.lng()}}
          >
            <div>
            <h1>{`${selectedPlace.name}`}</h1>
            {(placeDetails.hasOwnProperty('formatted_address')) && <h1>{placeDetails.formatted_address}</h1>}
            {(placeDetails.hasOwnProperty('formatted_phone_number')) && <h1>{placeDetails.formatted_phone_number}</h1>}
            {(placeDetails.hasOwnProperty('website')) && <h1><a href={placeDetails.website}>Go to website</a></h1>}
            {(placeDetails.hasOwnProperty('opening_hours')) && (placeDetails.opening_hours.hasOwnProperty('weekday_text')) && placeDetails.opening_hours.weekday_text.map((d) => (
            <h1>{`${d}`}</h1>))}
            <button type='submit' className="h-[38px] w-full bg-stone font-bold rounded-lg hover:bg-orange" onClick={() => addToIndex(placeDetails, selectedPlace)}>Add to Index</button>

            </div>
          </InfoWindow>
          
        )}
      
      </GoogleMap>
      </div>
  )
}