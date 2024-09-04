const axios = require('axios');

const fetchUserData = async () => {
    try {
        const response = await axios.get(`http://localhost:8080/db-api/users`);

        console.log(response.data);

        if (!response.data) {
            throw new Error("User not found");
        }
    } catch (error) {
        throw new Error("Failed to fetch user: " + error.message);
    }
};
fetchUserData().then(r => console.log(r)).catch(e => console.error(e));