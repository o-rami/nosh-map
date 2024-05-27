const RESTAURANT_API_URL = 'http://localhost:8080/api/restaurant';

export async function findAllRestaurants() {
  const response = await fetch(`${RESTAURANT_API_URL}`);
  if (response.status === 200) {
    return response.json();
  }
}

export async function getFavRestaurants(appUserId) {
  const response = await fetch(`${RESTAURANT_API_URL}/appUserId/${appUserId}`);
  if (response.status === 200) {
    return response.json();
  }
}

export async function findRestaurantById(restaurantId) {
  const response = await fetch(`${RESTAURANT_API_URL}/restaurantId/${restaurantId}`);
  if (response.status === 200) {
    return response.json();
  } else {
    return Promise.reject(`Restaurant: ${restaurantId} was not found.`);
  }
}

export async function createRestaurant(restaurant) {

  const init = makeRestaurantInit('POST', restaurant);
  const response = await fetch(RESTAURANT_API_URL, init);

  if (response.status === 201) {
    return response.json();
  } else if (response.status === 403) {
    return Promise.reject('Unauthorized');
  } else {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}

export async function updateRestaurant(restaurant) {

  const init = makeRestaurantInit('PUT', restaurant);
  const response = await fetch(`${RESTAURANT_API_URL}/${restaurant.id}`, init);

  if (response.status === 404) {
    return Promise.reject(`Restaurant: ${restaurant.id} was not found.`);
  } else if (response.status === 400) {
    const errors = await response.json();
    return Promise.reject(errors);
  } else if (response.status === 409) {
    return Promise.reject('Something went wrong... :(');
  } else if (response.status === 403) {
    return Promise.reject('Unauthorized');
  }
}

export async function deleteRestaurantById(restaurantId) {
  const jwtToken = localStorage.getItem('jwt_token');
  const init = {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${jwtToken}`
    },
  }
  const response = await fetch(`${RESTAURANT_API_URL}/${restaurantId}`, init);

  if (response.status === 404) {
    return Promise.reject(`Restaurant: ${restaurantId} was not found.`);
  } else if (response.status === 403) {
    return Promise.reject('Unauthorized');
  }
}

function makeRestaurantInit(method, restaurant) {
  const jwtToken = localStorage.getItem('jwt_token');

  const init = {
    method: method,
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Authorization': `Bearer ${jwtToken}`
    },
    body: JSON.stringify(restaurant)
  };

  return init;
}