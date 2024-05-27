const MEAL_API_URL = 'http://localhost:8080/api/meal';

export async function findAllMealsByUserId(appUserId) {
  const response = await fetch(`${MEAL_API_URL}/appUserId/${appUserId}`);
  if (response.status === 200) {
    return response.json();
  }
}

export async function findMealById(mealId) {
  const response = await fetch(`${MEAL_API_URL}/${mealId}`);
  if (response.status === 200) {
    return response.json();
  } else {
    return Promise.reject(`Meal: ${mealId} was not found.`);
  }
}

export async function findPublicMealsByUserId(appUserId) {
  const response = await fetch(`${MEAL_API_URL}/public/${appUserId}`);
  if (response.status === 200) {
    return response.json();
  }
}

export async function createMeal(meal, token) {

  const init = makeMealInit('POST', meal, token);
  const response = await fetch(MEAL_API_URL, init);

  if (response.status === 201) {
    return response.json();
  } else if (response.status === 403) {
    return Promise.reject('Unauthorized');
  } else {
    const errors = await response.json();
    return Promise.reject(errors);
  }
}

export async function updateMeal(meal, token) {

  const init = makeMealInit('PUT', meal, token);
  const response = await fetch(`${MEAL_API_URL}/${meal.mealId}`, init);

  if (response.status === 404) {
    return Promise.reject(`Meal: ${meal.id} was not found.`);
  } else if (response.status === 400) {
    const errors = await response.json();
    return Promise.reject(errors);
  } else if (response.status === 409) {
    return Promise.reject('Something went wrong... :(');
  } else if (response.status === 403) {
    return Promise.reject('Unauthorized');
  }
}

export async function deleteMealById(mealId, token) {
  const init = {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${token}`
    },
  }
  const response = await fetch(`${MEAL_API_URL}/${mealId}`, init);

  if (response.status === 404) {
    return Promise.reject(`Meal: ${mealId} was not found.`);
  } else if (response.status === 403) {
    return Promise.reject('Unauthorized');
  }
}

function makeMealInit(method, meal, token) {

  const init = {
    method: method,
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(meal)
  };

  return init;
}