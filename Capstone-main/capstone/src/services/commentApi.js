const API_URL= 'http://localhost:8080/api/comment';



function makeCommentInit(method, comment, token) {
    const init = {
        method: method,
        headers: {
            'Content-type': 'application/json',
            'Accept': 'application/json',
            'Authorization': `Bearer ${token}`,
            
        },
        body: JSON.stringify(comment)
    };

    return init;
};

export async function findbyCommentId(commentId) {
    const init = {
        method: 'GET',
        headers: {
            'Content-type': 'application/json',
            'Accept': 'application/json',
        },
    };
    const response = await fetch(`${API_URL}/${commentId}`, init);
    if(response.status === 200) {
        return response.json();
    } else {
        return Promise.reject(`CommentID: ${commentId} was not found`)
    }
};

/*export async function addComment(comment) {

    const init = makeCommentInit('POST', comment);
    const response = await fetch(API_URL, init);

    if (response.status === 201) {
        return response.json();
    } else {
        const errors = await response.json();
        return Promise.reject(errors);
    }
};*/

/*export async function updateComment(comment) {

    const init = makeCommentInit('PUT', comment);
    const url = `${API_URL}/${comment.commentId}`;
    const response = await fetch(url, init);

    if (response.status === 404) {
        return Promise.reject(`CommentId: ${comment.commentId} was not found.`);
    } else if (response.status === 400) {
        const errors = await response.json();
        return Promise.reject(errors);
    } else if (response.status === 409) {
        return Promise.reject('Cannot be found');
    }
};*/


export async function deleteById(commentId, token) {
    const init = {
        method: "DELETE",
        headers: {
            'Content-type': 'application/json',
            'Accept': 'application/json',
            'Authorization': `Bearer ${token}`,
            
        },
    }
    const response = await fetch(`${API_URL}/${commentId}`, init);

    if (response.status === 404) {
        return Promise.reject(`CommentID: ${commentId} was not found.`);
    }
};
