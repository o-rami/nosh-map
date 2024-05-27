const API_URL = 'http://localhost:8080/api/profile';


function makeProfileInit(method, profile) {
    const init = {
        method: method,
        headers: {
            'Content-type': 'application/json',
            'Accept': 'application/json',
        },
        body: JSON.stringify(profile)
    };

    return init;
};

export async function findAllProfile() {
    const init = {
        method: 'GET',
        headers: {
            'Content-type': 'application/json',
            'Accept': 'application/json',
        },
    };
    const response = await fetch(API_URL, init);
    if(response.status === 200) {
        return response.json();
    }
};

export async function findByUserId(appUserId) {
    const init = {
        method: 'GET',
        headers: {
            'Content-type': 'application/json',
            'Accept': 'application/json',
        },
    };
    const response = await fetch(`${API_URL}/${appUserId}`, init);
    if(response.status === 200) {
        return response.json();
    } else {
        return Promise.reject(`User ID: ${appUserId} was not found`)
    }
};

export async function createProfile(profile) {

    const init = makeProfileInit('POST', profile);
    const response = await fetch(API_URL, init);

    if (response.status === 201) {
        return response.json();
    } else {
        const errors = await response.json();
        return Promise.reject(errors);
    }
};

export async function updateProfile(profile) {

    const init = makeProfileInit('PUT', profile);
    const url = `${API_URL}/${profile.appUserId}`;
    const response = await fetch(url, init);

    if (response.status === 404) {
        return Promise.reject(`UserId: ${profile.appUserId} was not found.`);
    } else if (response.status === 400) {
        const errors = await response.json();
        return Promise.reject(errors);
    } else if (response.status === 409) {
        return Promise.reject('Cannot be found');
    }
};


export async function deleteById(appUserId) {
    const response = await fetch(`${API_URL}/${appUserId}`, { method: 'DELETE' });

    if (response.status === 404) {
        return Promise.reject(`UserID: ${appUserId} was not found.`);
    }
};
