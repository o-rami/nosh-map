const API_URL = 'http://localhost:8080/api/rating';


function makeRatingInit(method, rating) {
    const init = {
        method: method,
        headers: {
            'Content-type': 'application/json',
            'Accept': 'application/json',
        },
        body: JSON.stringify(rating)
    };

    return init;
};

export async function findByRatingId(ratingId) {
    const response = await fetch(`${API_URL}/${ratingId}`);
    if (response.status === 200) {
        return response.json();
      } else {
        return Promise.reject(`Rating: ${ratingId} was not found.`);
      }

}

export async function addRating(rating){
    const init = makeRatingInit('POST', rating);
    const response = await fetch(API_URL, init);

    if (response.status === 201) {
        return response.json();
    } else {
        const errors = await response.json();
        return Promise.reject(errors);
    }
}

export async function updateRating(rating) {
    const init = makeRatingInit('PUT', rating);
    const url = `${API_URL}/${rating.ratingId}`;
    const response = await fetch(url, init);

    if (response.status === 404) {
        return Promise.reject(`rating: ${rating.ratingId} was not found.`);
    } else if (response.status === 400) {
        const errors = await response.json();
        return Promise.reject(errors);
    } else if (response.status === 409) {
        return Promise.reject('Cannot be found');
    }
}

export async function deleteRatingById(ratingId) {
    const response = await fetch(`${API_URL}/${ratingId}`, { method: 'DELETE' });

    if (response.status === 404) {
        return Promise.reject(`rating: ${ratingId} was not found.`);
    }
}
