"use client";

import React, {useEffect, useState} from "react";
import Link from "next/link";
import "./globals.css"
import {Avatar, Button, Dropdown, DropdownDivider, DropdownItem, Navbar} from "flowbite-react";
import styled from "styled-components";
import Cookies from 'js-cookie';
import axios from "axios";
import{useRouter} from "next/navigation";

const StyledText = styled.h1`
        font-family: 'Inconsolata', sans-serif;
        font-size: 2rem;
        font-weight: 700;
        `;

const Navigation = () => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const[isSignIn, setIsSignIn] = useState(true);
    const [userDetails, setUserDetails] = useState({});
    const [imageSrc, setImageSrc] = useState(null);
    const router = useRouter();

    const openCart = () => {
        router.push('/cart');
    };

    const user = async ()=>{
        const token = Cookies.get('jwt');
        const userId = Cookies.get('userId');

        if (!token || !userId) {
            return;
        }

        return await axios.get(`http://localhost:8080/user-service/users/${userId}`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
    }

    useEffect(() => {
        const jwtExists = document.cookie.split('; ').find((row) => row.startsWith('jwt='));

        user().then((response) => {
            if(response)
                setUserDetails(response.data);
        });

        if (jwtExists) {
            setIsSignIn(false);
            setIsAuthenticated(true);
        } else {
            setIsAuthenticated(false);
            setIsSignIn(true);
        }
    }, []);
    useEffect(() => {
        const fetchUserProfilePicture = async () => {
            try {
                const token = Cookies.get('jwt');
                const userId = Cookies.get('userId');

                const response = await axios.get(`http://localhost:8080/user-service/users/image/${userId}`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                    responseType: 'blob', // Important: set response type to 'blob' to handle image data
                });

                // Convert the response data (blob) to a URL that can be used as an image source
                const imageUrl = URL.createObjectURL(response.data);
                setImageSrc(imageUrl);
            } catch (error) {
                console.error("Error fetching user profile picture:", error);
            }
        };

        fetchUserProfilePicture();
    }, [userDetails]);

    const handleSignOut = () => {
        Cookies.remove('jwt');
        Cookies.remove('userId');
        setIsAuthenticated(false);
        setIsSignIn(true);
        router.refresh();
    }

    return (
        <div>
            <Navbar fluid rounded>
                <Navbar.Brand href="https://localhost:3000/">
                    <StyledText>NDURANCE</StyledText>
                </Navbar.Brand>
                <div className="flex md:order-2 gap-3">
                    <div>
                        <Button color="gray" onClick={openCart}>
                            <svg className="w-[20px] h-[20px] text-black" aria-hidden="true"
                                 xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24">
                                <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="1"
                                      d="M5 4h1.5L9 16m0 0h8m-8 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4Zm8 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4Zm-8.5-3h9.25L19 7H7.312"/>
                            </svg>
                        </Button>
                    </div>
                    {isAuthenticated && (
                        <Dropdown
                            arrowIcon={false}
                            inline
                            label={
                                <Avatar alt="User settings" img={imageSrc}
                                        rounded/>
                            }
                        >
                            <Dropdown.Header>
                                <span className="block truncate text-sm font-medium">{userDetails.firstName} {userDetails.lastName}</span>
                                <span className="block text-sm">{userDetails.email}</span>
                            </Dropdown.Header>
                            <Dropdown.Item href={"/cart"} >Cart</Dropdown.Item>
                            {/* <Dropdown.Item>Settings</Dropdown.Item> */}
                            <Dropdown.Item href={"/orders"}>Orders</Dropdown.Item>
                            <DropdownItem href={"/user"}>User Profile</DropdownItem>
                            <Dropdown.Divider/>
                            <Dropdown.Item onClick={handleSignOut}>Sign out</Dropdown.Item>
                        </Dropdown>
                    )}
                    <div>
                        {isSignIn && (
                            <Dropdown
                                arrowIcon={false}
                                inline
                                label={
                                    <Link href={""} className="sm:block md:block lg:block xl:block 2xl:block
                            border rounded-lg px-2 py-2 bg-[#155e75] text-white">Sign In</Link>
                                }
                            >
                                <DropdownItem href={"/login"}>Member Login</DropdownItem>
                                <DropdownDivider/>
                                <DropdownItem href={"/login"}>Staff Login</DropdownItem>
                            </Dropdown>
                        )}
                    </div>
                    <Navbar.Toggle/>
                </div>
                <Navbar.Collapse className="text-center">
                    <Navbar.Link href="/">HOME</Navbar.Link>
                    <Navbar.Link href="/category">CATEGORIES</Navbar.Link>
                    <Navbar.Link href="/all">ALL</Navbar.Link>
                </Navbar.Collapse>
            </Navbar>
        </div>
    );
}

export default Navigation;