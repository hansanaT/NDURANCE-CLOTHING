"use client";

import React, { useState, useEffect } from "react";
import axios from "axios";
import Cookies from "js-cookie";
import Navigation from "../navigation";
import { FcAddressBook } from "react-icons/fc";

const UserSettings = () => {
    const [userDetails, setUserDetails] = useState({});
    const [imageSrc, setImageSrc] = useState(null);
    const [error, setError] = useState(null);

    const [userInfo, setUserInfo] = useState({
        firstName: "",
        lastName: "",
        email: "",
    });

    // Separate state for the address form
    const [addressInfo, setAddressInfo] = useState({
        city: "",
        country: "",
        streetName: "",
        postalCode: ""
    });

    const [passwords, setPasswords] = useState({
        userId: "",
        currentPassword: "",
        newPassword: "",
        confirmPassword: "",
    });

    useEffect(() => {
        const getUserDetails = async () => {
            const token = Cookies.get("jwt");
            const userId = Cookies.get("userId");

            if (!token || !userId) {
                setError("User not authenticated");
                return;
            }

            try {
                const user = await axios.get(
                    `http://localhost:8080/user-service/users/${userId}`,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    }
                );
                setUserDetails(user.data);
                setUserInfo({
                    firstName: user.data.firstName,
                    lastName: user.data.lastName,
                    email: user.data.email,
                });
                setAddressInfo(user.data.addresses[0]); // Assuming only one address
                fetchUserProfilePicture(userId);
                setPasswords({
                    userId: userId,
                    currentPassword: "",
                    newPassword: "",
                    confirmPassword: "",
                });
            } catch (error) {
                setError("Failed to fetch user details");
            }
        };

        const fetchUserProfilePicture = async (userId) => {
            try {
                const token = Cookies.get("jwt");
                const response = await axios.get(
                    `http://localhost:8080/user-service/users/image/${userId}`,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                        responseType: "blob",
                    }
                );
                const imageUrl = URL.createObjectURL(response.data);
                setImageSrc(imageUrl);
            } catch (error) {
                console.log(error);
            }
        };

        getUserDetails();
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setUserInfo({ ...userInfo, [name]: value });
    };

    // Handle changes for the address form
    const handleAddressChange = (e) => {
        const { name, value } = e.target;
        setAddressInfo((prevAddress) => ({
            ...prevAddress,
            [name]: value,
        }));
    };

    const handlePasswordChange = (e) => {
        setPasswords({ ...passwords, [e.target.name]: e.target.value });
    };

    const userPasswordChange = async (e) => {
        e.preventDefault();
        const userId = Cookies.get("userId");
        const token = Cookies.get("jwt");

        try {
            await axios.post(
                `http://localhost:8080/user-service/users/reset-password/${userId}`,
                passwords,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                }
            );
            alert("Password updated successfully");
        } catch (error) {
            setError(`Error updating user details: ${error}`);
        }
    };

    const handleProfilePicChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            const userId = Cookies.get("userId");
            const token = Cookies.get("jwt");
            const formData = new FormData();
            formData.append("image", file);

            axios
                .put(
                    `http://localhost:8080/user-service/users/upload-pic/${userId}`,
                    formData,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            "Content-Type": "multipart/form-data",
                        },
                    }
                )
                .then((response) => {
                    alert("Profile picture updated successfully");
                    const imageUrl = URL.createObjectURL(file);
                    setImageSrc(imageUrl);
                })
                .catch((error) => {
                    setError(`Error uploading profile picture: ${error}`);
                });
        }
    };

    const handleFormSubmit = async (e) => {
        e.preventDefault();
        const userId = Cookies.get("userId");
        const token = Cookies.get("jwt");

        try {
            await axios.put(
                `http://localhost:8080/user-service/users/${userId}`,
                userInfo,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                }
            );
            alert("User details updated successfully");
        } catch (error) {
            setError(`Error updating user details: ${error}`);
        }
    };

    // Handle address form submission
    const handleAddressSubmit = async (e) => {
        e.preventDefault();
        const userId = Cookies.get("userId");
        const token = Cookies.get("jwt");

        try {
            // Wrapping the addressInfo in an array

            const addresses = [addressInfo];

            await axios.put(
                `http://localhost:8080/user-service/users/${userId}`, // Assuming there's an endpoint for updating address
                {
                    ...userInfo,
                    addresses
                }, // Send the address as an array
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                }
            );
            alert("Address updated successfully");
        } catch (error) {
            setError(`Error updating address: ${error}`);
        }
    };


    if (error) return <p className="text-red-500">Error: {error}</p>;

    return (
        <div>
            <Navigation />
            <div className="flex">

                <div className="w-3/4 p-6 bg-white shadow-md">
                    <h2 className="text-2xl font-bold mb-6">Your Account Settings</h2>
                    <form className="space-y-4" onSubmit={handleFormSubmit}>
                        <div className="flex flex-col space-y-2">
                            <label className="font-semibold" htmlFor="firstName">
                                First Name
                            </label>
                            <input
                                id="firstName"
                                className="border p-2 rounded"
                                type="text"
                                value={userInfo.firstName}
                                onChange={handleInputChange}
                                required
                                name="firstName"
                            />
                        </div>
                        <div className="flex flex-col space-y-2">
                            <label className="font-semibold" htmlFor="lastName">
                                Last Name
                            </label>
                            <input
                                id="lastName"
                                className="border p-2 rounded"
                                type="text"
                                value={userInfo.lastName}
                                onChange={handleInputChange}
                                required
                                name="lastName"
                            />
                        </div>
                        <div className="flex flex-col space-y-2">
                            <label className="font-semibold" htmlFor="email">
                                Email Address
                            </label>
                            <input
                                id="email"
                                className="border p-2 rounded"
                                type="email"
                                value={userInfo.email}
                                onChange={handleInputChange}
                                required
                                name="email"
                            />
                        </div>

                        <div className="flex items-center space-x-4">
                            <img
                                src={imageSrc || "https://via.placeholder.com/150"}
                                alt="Profile"
                                className="w-32 h-32 object-cover rounded-full"
                            />
                            <input
                                type="file"
                                accept="image/*"
                                onChange={handleProfilePicChange}
                                className="file-input"
                            />
                        </div>

                        <button
                            className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600"
                            type="submit"
                        >
                            Update Account Settings
                        </button>
                    </form>

                    {/* Address Update Section */}
                    <h2 className="text-2xl font-bold mt-8">Update Address</h2>
                    <form className="space-y-4" onSubmit={handleAddressSubmit}>
                        <div className="flex flex-col space-y-2">
                            <label className="font-semibold" htmlFor="city">
                                City
                            </label>
                            <input
                                id="city"
                                className="border p-2 rounded"
                                type="text"
                                //value={addressInfo.city}
                                onChange={handleAddressChange}
                                required
                                name="city"
                            />
                        </div>
                        <div className="flex flex-col space-y-2">
                            <label className="font-semibold" htmlFor="country">
                                Country
                            </label>
                            <input
                                id="country"
                                className="border p-2 rounded"
                                type="text"
                                //value={addressInfo.country}
                                onChange={handleAddressChange}
                                required
                                name="country"
                            />
                        </div>
                        <div className="flex flex-col space-y-2">
                            <label className="font-semibold" htmlFor="streetName">
                                Street Name
                            </label>
                            <input
                                id="streetName"
                                className="border p-2 rounded"
                                type="text"
                                //value={addressInfo.streetName}
                                onChange={handleAddressChange}
                                required
                                name="streetName"
                            />
                        </div>
                        <div className="flex flex-col space-y-2">
                            <label className="font-semibold" htmlFor="postalCode">
                                Postal Code
                            </label>
                            <input
                                id="postalCode"
                                className="border p-2 rounded"
                                type="text"
                                //value={addressInfo.postalCode}
                                onChange={handleAddressChange}
                                required
                                name="postalCode"
                            />
                        </div>

                        <button
                            className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600"
                            type="submit"
                        >
                            Update Address
                        </button>
                    </form>

                    <h2 className="text-2xl font-bold mt-8">Change Password</h2>
                    <form className="space-y-4" onSubmit={userPasswordChange}>
                        <div className="flex flex-col space-y-2">
                            <label className="font-semibold" htmlFor="currentPassword">
                                Current Password
                            </label>
                            <input
                                id="currentPassword"
                                className="border p-2 rounded"
                                type="password"
                                value={passwords.currentPassword}
                                onChange={handlePasswordChange}
                                required
                                name="currentPassword"
                            />
                        </div>
                        <div className="flex flex-col space-y-2">
                            <label className="font-semibold" htmlFor="newPassword">
                                New Password
                            </label>
                            <input
                                id="newPassword"
                                className="border p-2 rounded"
                                type="password"
                                value={passwords.newPassword}
                                onChange={handlePasswordChange}
                                required
                                name="newPassword"
                            />
                        </div>
                        <div className="flex flex-col space-y-2">
                            <label className="font-semibold" htmlFor="confirmPassword">
                                Confirm New Password
                            </label>
                            <input
                                id="confirmPassword"
                                className="border p-2 rounded"
                                type="password"
                                value={passwords.confirmPassword}
                                onChange={handlePasswordChange}
                                required
                                name="confirmPassword"
                            />
                        </div>

                        <button
                            className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600"
                            type="submit"
                        >
                            Update Password
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default UserSettings;
