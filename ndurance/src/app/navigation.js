"use client";

import React from "react";
import Link from "next/link";
import "./globals.css";
import { Button, Avatar,
    Dropdown,
    DropdownDivider,
    DropdownHeader,
    DropdownItem,
    Navbar,
    NavbarBrand, } from "flowbite-react";

    export default function Navigation({cartCount}) {
    return (
        <Navbar fluid className="bg-transparent">
            <NavbarBrand href="http://localhost:3000/">
                <img src="/next.svg" className="mr-3 h-6 sm:h-9" alt="Flowbite React Logo"/>
            </NavbarBrand>
            <div className="flex md:items-end sm:items-center gap-3 py-2 md:order-2">
                <div className="flex py-2 px-5 gap-8 text-sm text-gray-500 font-normal">
                    <Link className="hover:text-gray-800" href={"/"}>HOME</Link>
                    <Link className="hover:text-gray-800" href={"/category"}>CATEGORIES</Link>
                </div>
                <div>
                    <Button color="gray">
                        <svg className="w-[20px] h-[20px] text-black" aria-hidden="true"
                             xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24">
                            <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="1"
                                  d="M5 4h1.5L9 16m0 0h8m-8 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4Zm8 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4Zm-8.5-3h9.25L19 7H7.312"/>
                        </svg>
                        <div id="cart-count"
                            className="absolute inline-flex items-center justify-center w-6 h-6 text-xs font-bold text-white
                            bg-[#155e75] border-2 border-white rounded-full -top-2 -end-2 dark:border-gray-900">{cartCount}
                        </div>
                    </Button>
                </div>
                <div>
                    <Button className="hidden sm:block md:block lg:block xl:block 2xl:block">Checkout</Button>
                </div>
                <div>
                    <Dropdown
                        arrowIcon={false}
                        inline
                        label={
                            <Avatar alt="User settings"
                                    img="https://flowbite.com/docs/images/people/profile-picture-5.jpg"
                                    rounded/>
                        }
                    >
                        <DropdownHeader>
                            <span className="block text-sm">Bonnie Green</span>
                            <span className="block truncate text-sm font-medium">name@flowbite.com</span>
                        </DropdownHeader>
                        <DropdownItem>Dashboard</DropdownItem>
                        <DropdownItem>Settings</DropdownItem>
                        <DropdownItem>Wishlist</DropdownItem>
                        <DropdownDivider/>
                        <DropdownItem>Sign out</DropdownItem>
                    </Dropdown>
                </div>
            </div>
        </Navbar>
    );
}