"use client";

import {Avatar, Dropdown, Navbar, NavbarCollapse, Sidebar} from "flowbite-react";
import React, {useState} from "react";
import {
    HiCash,
    HiChartPie, HiClipboardList,
    HiCog,
    HiCube,
    HiHeart,
    HiInbox,
    HiLogout,
    HiMenuAlt1,
    HiShoppingBag,
    HiViewGrid
} from "react-icons/hi";

export function DashNav() {
    const [sidebarOpen, setSidebarOpen] = useState(false);

    const handleToggleSidebar = () => {
        setSidebarOpen(!sidebarOpen);
    };
    return (
        <div>
            <Navbar fluid rounded>
                <Navbar.Toggle barIcon={HiMenuAlt1} onClick={handleToggleSidebar} className="sm:block md:block lg:block" />
                <Navbar.Brand href="/">
                    <img src="/next.svg" className="mr-3 h-6 sm:h-9" alt="NEXT Logo" />
                </Navbar.Brand>
                <div className="flex md:order-2">
                    <Dropdown
                        arrowIcon={false}
                        inline
                        label={
                            <Avatar alt="User settings" img="https://flowbite.com/docs/images/people/profile-picture-5.jpg" rounded />
                        }
                    >
                        <Dropdown.Header>
                            <span className="block text-sm">Bonnie Green</span>
                            <span className="block truncate text-sm font-medium">name@flowbite.com</span>
                        </Dropdown.Header>
                        <Dropdown.Item>Dashboard</Dropdown.Item>
                        <Dropdown.Item>Settings</Dropdown.Item>
                        <Dropdown.Item>Wishlist</Dropdown.Item>
                        <Dropdown.Divider />
                        <Dropdown.Item>Sign out</Dropdown.Item>
                    </Dropdown>
                </div>
            </Navbar>
            {sidebarOpen && (
                <Sidebar className="absolute h-screen pt-3 z-50" aria-label="Sidebar with multi-level dropdown example">
                    <Sidebar.Items>
                        <Sidebar.ItemGroup>
                            <Sidebar.Item href="/dashboard" icon={HiChartPie}>
                                Dashboard
                            </Sidebar.Item>
                            <Sidebar.Collapse icon={HiShoppingBag} label="E-commerce">
                                <Sidebar.Item href="#" icon={HiCube}>Products</Sidebar.Item>
                                <Sidebar.Item href="#" icon={HiCash}>Refunds</Sidebar.Item>
                                <Sidebar.Item href="#" icon={HiClipboardList}>Purchase History</Sidebar.Item>
                            </Sidebar.Collapse>
                            <Sidebar.Item href="#" icon={HiInbox}>
                                Inbox
                            </Sidebar.Item>
                            <Sidebar.Item href="#" icon={HiViewGrid}>
                                Categories
                            </Sidebar.Item>
                            <Sidebar.Item href="#" icon={HiHeart}>
                                Wishlist
                            </Sidebar.Item>
                            <Sidebar.Item href="#" icon={HiCog}>
                                Settings
                            </Sidebar.Item>
                            <Sidebar.Item href="#" icon={HiLogout}>
                                Sign Out
                            </Sidebar.Item>
                        </Sidebar.ItemGroup>
                    </Sidebar.Items>
                </Sidebar>
            )}
        </div>
    );
}
