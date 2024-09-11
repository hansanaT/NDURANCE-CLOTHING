"use client";

import React, { useState, useEffect } from "react";
import axios from "axios";
import Cookies from "js-cookie";
import { useRouter } from "next/navigation";
import Navigation from "../navigation";

const UserSettings = ({ user, edit }) => {
    const [userDetails, setUserDetails] = useState({});
    const [imageSrc, setImageSrc] = useState(null);
    const router = useRouter();

    const [userInfo, setUserInfo] = useState({
        firstName: '',
        lastName: '',
        email: '',
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
                fetchUserProfilePicture(userId);
                setPasswords({
                    userId: userId,
                    currentPassword: "",
                    newPassword: "",
                    confirmPassword: "",
                })
            } catch (error) {
                console.error("Error fetching user details:", error);
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
                        responseType: "blob", // Important: set response type to 'blob' to handle image data
                    }
                );
                const imageUrl = URL.createObjectURL(response.data);
                setImageSrc(imageUrl);
            } catch (error) {
                console.error("Error fetching user profile picture:", error);
            }
        };

        getUserDetails();
    }, []);

    const handleInputChange = (e) => {
        setUserInfo({ ...userInfo, [e.target.name]: e.target.value });
    };

    const handlePasswordChange = (e) => {
        setPasswords({ ...passwords, [e.target.name]: e.target.value });
    };

    const userPasswordChange = async (e) => {
        e.preventDefault();
        const userId = Cookies.get("userId");
        const token = Cookies.get("jwt");

        console.log(passwords);
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
            console.log("User details updated successfully");
        } catch (error) {
            console.error("Error updating user details:", error);
        }
    }

    const handleProfilePicChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            const userId = Cookies.get("userId");
            const token = Cookies.get("jwt");
            const formData = new FormData();
            formData.append("image", file);

            axios.put(
                `http://localhost:8080/user-service/users/upload-pic/${userId}`,
                formData,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "multipart/form-data",
                    },
                }
            )
                .then(response => {
                    console.log("Profile picture updated successfully");
                    const imageUrl = URL.createObjectURL(file);
                    setImageSrc(imageUrl);
                })
                .catch(error => {
                    console.error("Error uploading profile picture:", error);
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
            console.log("User details updated successfully");
        } catch (error) {
            console.error("Error updating user details:", error);
        }
    };

    return (
        <div>
            <Navigation />
            <div className="user-view">
                <nav className="user-view__menu">
                    <ul className="side-nav">
                        <NavItem link="#" text="Settings" icon="settings" active />
                        <NavItem link="/Billing" text="Billing" icon="credit-card" />
                    </ul>
                </nav>

                <div className="user-view__content">
                    <div
                        className="user-view__form-container"
                        style={
                            edit
                                ? {
                                    display: "grid",
                                    gridTemplateColumns: "repeat(2, 1fr)",
                                    maxWidth: "68rem",
                                    margin: "0 auto",
                                    gridGap: "10px",
                                }
                                : {}
                        }
                    >
                        <h2 className="heading-secondary ma-bt-md">Your account settings</h2>
                        <form className="form form-user-data" onSubmit={handleFormSubmit}>
                            <div className="form__group">
                                <label className="form__label" htmlFor="name">
                                    First Name
                                </label>
                                <input
                                    id="name"
                                    className="form__input"
                                    type="text"
                                    value={userInfo.firstName}
                                    onChange={handleInputChange}
                                    required
                                    name="firstName"
                                />
                            </div>
                            <div className="form__group">
                                <label className="form__label" htmlFor="name">
                                    Last Name
                                </label>
                                <input
                                    id="name"
                                    className="form__input"
                                    type="text"
                                    value={userInfo.lastName}
                                    onChange={handleInputChange}
                                    required
                                    name="lastName"
                                />
                            </div>
                            <div className="form__group ma-bt-md">
                                <label className="form__label" htmlFor="email">
                                    Email address
                                </label>
                                <input
                                    id="email"
                                    className="form__input"
                                    type="email"
                                    value={userInfo.email}
                                    onChange={handleInputChange}
                                    required
                                    name="email"
                                />
                            </div>
                            <div className="form__group form__photo-upload">
                                <img
                                    className="form__user-photo"
                                    src={imageSrc}
                                    alt="User photo"
                                />
                                <input
                                    className="form__upload"
                                    type="file"
                                    accept="image/*"
                                    id="photo"
                                    name="photo"
                                    onChange={handleProfilePicChange}
                                />
                                <label htmlFor="photo">Choose new photo</label>
                            </div>
                            <div className="form__group right">
                                <button className="btn btn--small btn--green" type="submit">
                                    Save settings
                                </button>
                            </div>
                        </form>
                    </div>

                    <div className="line">&nbsp;</div>

                    <div className="user-view__form-container">
                        <h2 className="heading-secondary ma-bt-md">Password change</h2>
                        <form className="form form-user-password" onSubmit={userPasswordChange}>
                            <div className="form__group">
                                <label className="form__label" htmlFor="password-current">
                                    Current password
                                </label>
                                <input
                                    id="password-current"
                                    className="form__input"
                                    type="password"
                                    placeholder="••••••••"
                                    required
                                    minLength="8"
                                    name="currentPassword"
                                    value={passwords.currentPassword}
                                    onChange={handlePasswordChange}
                                />
                            </div>
                            <div className="form__group">
                                <label className="form__label" htmlFor="password">
                                    New password
                                </label>
                                <input
                                    id="password"
                                    className="form__input"
                                    type="password"
                                    placeholder="••••••••"
                                    required
                                    minLength="8"
                                    name="newPassword"
                                    value={passwords.newPassword}
                                    onChange={handlePasswordChange}
                                />
                            </div>
                            <div className="form__group ma-bt-lg">
                                <label className="form__label" htmlFor="password-confirm">
                                    Confirm password
                                </label>
                                <input
                                    id="password-confirm"
                                    className="form__input"
                                    type="password"
                                    placeholder="••••••••"
                                    required
                                    minLength="8"
                                    name="confirmPassword"
                                    value={passwords.confirmPassword}
                                    onChange={handlePasswordChange}
                                />
                            </div>
                            <div className="form__group right">
                                <button
                                    className="btn btn--small btn--green btn--save-password"
                                    type="submit"
                                >
                                    Save password
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
};

const NavItem = ({ link, text, icon, active }) => (
    <li className={`${active ? "side-nav--active" : ""}`}>
        <a href={link}>
            <svg>
                <use xlinkHref={`img/icons.svg#icon-${icon}`} />
            </svg>
            {text}
        </a>
    </li>
);

export default UserSettings;
