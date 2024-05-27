import { React, useState, useEffect }from "react";
import { GoogleMap, MarkerF, InfoWindow } from "@react-google-maps/api";

  //key: AIzaSyBxLfsU34vWfW7-qkYyOgFME0ialJE6rgo

/*let EMPTY_DETAILS = {
  formatted_address: null,
  formatted_phone_number: null,
  opening_hours: null,
  website: null
}*/

export default function GMapGuest({ onReload }) {

  const [placesList, setPlacesList] = useState([]);
  const [selectedPlace, setSelectedPlace] = useState(null);
  const [placeDetails, setplaceDetails] = useState({});
  const [position, setPosition] = useState(window.google.maps.LatLng(33.9519347, -83.3575670));
  const [mapRef, setMapRef] = useState(null)

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
    radius: 2200,
    type: ['restaurant'],
    keyword: 'restaurant',
  };

  /*let detailRequest = {
    placeId: "placeholder",
    fields: ["formatted_address", "formatted_phone_number", "opening_hours", "website"]
  }*/

  const containerStyle = {
    width: '500px',
    height: '600px'
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
    const gMap = mapRef.state.map;
    let placesAdded = []
    let placeIds = [];
    if (status === window.google.maps.places.PlacesServiceStatus.OK) {
      for (var i = 0; i < results.length; i++) {
        placesAdded.push(results[i]);
        placeIds.push(results[i].place_id)

        console.log(results[i])
      }
    }
    setPlacesList(placesAdded);
    // Get ids to guest view for list rendering
    //onReload(placeIds);
    // Get little restaurant info from nearby search to guest view for list rendering
    onReload(placesAdded, gMap);
    
  }

  /*function detailsCallback(place, status) {
    if (status === window.google.maps.places.PlacesServiceStatus.OK) {
      console.log('place in callback: ', place);
      let details = {};
      if (place.formatted_address) {
        details.formatted_address = place.formatted_address;
      } else {
        details.formatted_address = null;
      }
      if (place.formatted_phone_number) {
        details.formatted_phone_number = place.formatted_phone_number;
      } else {
        details.formatted_phone_number = null;
      }
      if (place.opening_hours && place.opening_hours.weekday_text) {
        details.opening_hours = place.opening_hours;
      } else {
        details.opening_hours = null;
      }
      if (place.website) {
        details.website = place.website;
      } else {
        details.website = null;
      }
      setplaceDetails(details);
    }
  }*/

  const onMarkerLoad = marker => {
    console.log('marker: ', marker);
  }
/*
  function getDetails (place, event) {
    console.log('event: ', event)
    console.log('place: ', place);
    if (place) {
    PlacesService.current = new window.google.maps.places.PlacesService(event.domEvent.srcElement.offsetParent.children[1]);
    detailRequest.placeId = place.place_id;
    PlacesService.current.getDetails(detailRequest, detailsCallback);
    }
  }
  */

  // CHANGE IN THE GUEST VIEW TO HIGHLIGHT LIST OBJECT INSTEAD OF SHOWING INFO MARKER WITH INFO AGAIN

  return (
    <div id='googleMap'>
      <GoogleMap
        ref={ref => setMapRef(ref)}
        onLoad={(map) => {getCurrentLocation(); onMapLoad(map)}}
        mapContainerStyle={containerStyle}
        center={position}
        zoom={14}
        onIdle={() => {onMapReload()}}
        id="guestMap"
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
            {/*getDetails(p, evt)*/}}}
            />
            ))
          }
          {console.log('placeDetails: ', placeDetails)}
        {selectedPlace && (
          <InfoWindow
            onCloseClick={() => { setSelectedPlace(null) }}
            position={{lat: selectedPlace.geometry.location.lat(),
              lng: selectedPlace.geometry.location.lng()}}
          >
            <div>
            <h1>{`${selectedPlace.name}`}</h1>
            <h1>{`${selectedPlace.vicinity}`}</h1>
            {/*}
            {(placeDetails.hasOwnProperty('formatted_address')) && <p>{placeDetails.formatted_address}</p>}
            {(placeDetails.hasOwnProperty('formatted_phone_number')) && <p>{placeDetails.formatted_phone_number}</p>}
            {(placeDetails.hasOwnProperty('website')) && <p><a href={placeDetails.website}>Go to website</a></p>}
            {(placeDetails.hasOwnProperty('opening_hours')) && (placeDetails.opening_hours.hasOwnProperty('weekday_text')) && placeDetails.opening_hours.weekday_text.map((d) => (
            <p>{`${d}`}</p>))}
            */}

            </div>

          </InfoWindow>
        )}
      
      </GoogleMap>
      </div>
  )
}