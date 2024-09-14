"use client";

import Navigation from "@/app/navigation";
import axios from "axios";
import Cookies from "js-cookie";
import {useEffect,useState} from "react";

export default function OrdersPage() {
    const[orders, setOrders] = useState([]);
    const fetchOrder = async () => {
        return await axios.get(`http://localhost:8080/order-service/orders`,{
            headers:{
                'Authorization': `Bearer ${Cookies.get('jwt')}`
            }
        });
    }
    useEffect(() => {
        fetchOrder().then((response) => {
            setOrders(response.data);
        }).catch((error) => {
            console.log(error);
        });
    }, []);
    useEffect(() => {

    }, [orders]);
    return (
        <div>
            <Navigation/>
            <section className="bg-white py-8 antialiased dark:bg-gray-900 md:py-16">
                <div className="mx-auto max-w-screen-xl px-4 2xl:px-0">
                    <div className="mx-auto max-w-5xl">
                        <div className="gap-4 sm:flex sm:items-center sm:justify-between">
                            <h2 className="text-xl font-semibold text-gray-900 dark:text-white sm:text-2xl">My orders</h2>
                        </div>

                        <div className="mt-6 flow-root sm:mt-8">
                            <div className="divide-y divide-gray-200 dark:divide-gray-700">
                                {orders.map((order) => (
                                    <div className="flex flex-wrap items-center gap-y-4 gap-x-4 py-6">
                                        <dl className="w-1/2 sm:w-1/4 lg:w-auto lg:flex-1">
                                            <dt className="text-base font-medium text-gray-500 dark:text-gray-400">Order
                                                ID:
                                            </dt>
                                            <dd className="mt-1.5 text-base font-semibold text-gray-900 dark:text-white">
                                                <a href="#" className="hover:underline">#{order.orderId}</a>
                                            </dd>
                                        </dl>

                                        <dl className="w-1/2 sm:w-1/4 lg:w-auto lg:flex-1">
                                            <dt className="text-base font-medium text-gray-500 dark:text-gray-400">Date:</dt>
                                            <dd className="mt-1.5 text-base font-semibold text-gray-900 dark:text-white">{order.orderDate}</dd>
                                        </dl>

                                        <dl className="w-1/2 sm:w-1/4 lg:w-auto lg:flex-1">
                                            <dt className="text-base font-medium text-gray-500 dark:text-gray-400">Price:</dt>
                                            <dd className="mt-1.5 text-base font-semibold text-gray-900 dark:text-white">${order.price}</dd>
                                        </dl>

                                    </div>
                                ))}
                            </div>
                        </div>

                        <nav className="mt-6 flex items-center justify-center sm:mt-8"
                             aria-label="Page navigation example">
                            <ul className="flex h-8 items-center -space-x-px text-sm">
                                <li>
                                    <a href="#"
                                       className="ms-0 flex h-8 items-center justify-center rounded-s-lg border border-e-0 border-gray-300 bg-white px-3 leading-tight text-gray-500 hover:bg-gray-100 hover:text-gray-700 dark:border-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white">
                                        <span className="sr-only">Previous</span>
                                        <svg className="h-4 w-4 rtl:rotate-180" aria-hidden="true"
                                             xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none"
                                             viewBox="0 0 24 24">
                                            <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round"
                                                  strokeWidth="2" d="m15 19-7-7 7-7"/>
                                        </svg>
                                    </a>
                                </li>
                                <li>
                                    <a href="#"
                                       className="flex h-8 items-center justify-center border border-gray-300 bg-white px-3 leading-tight text-gray-500 hover:bg-gray-100 hover:text-gray-700 dark:border-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white">1</a>
                                </li>
                                <li>
                                    <a href="#"
                                       className="flex h-8 items-center justify-center border border-gray-300 bg-white px-3 leading-tight text-gray-500 hover:bg-gray-100 hover:text-gray-700 dark:border-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white">2</a>
                                </li>
                                <li>
                                    <a href="#" aria-current="page"
                                       className="z-10 flex h-8 items-center justify-center border border-primary-300 bg-primary-50 px-3 leading-tight text-primary-600 hover:bg-primary-100 hover:text-primary-700 dark:border-gray-700 dark:bg-gray-700 dark:text-white">3</a>
                                </li>
                                <li>
                                    <a href="#"
                                       className="flex h-8 items-center justify-center border border-gray-300 bg-white px-3 leading-tight text-gray-500 hover:bg-gray-100 hover:text-gray-700 dark:border-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white">...</a>
                                </li>
                                <li>
                                    <a href="#"
                                       className="flex h-8 items-center justify-center border border-gray-300 bg-white px-3 leading-tight text-gray-500 hover:bg-gray-100 hover:text-gray-700 dark:border-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white">100</a>
                                </li>
                                <li>
                                    <a href="#"
                                       className="flex h-8 items-center justify-center rounded-e-lg border border-gray-300 bg-white px-3 leading-tight text-gray-500 hover:bg-gray-100 hover:text-gray-700 dark:border-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white">
                                        <span className="sr-only">Next</span>
                                        <svg className="h-4 w-4 rtl:rotate-180" aria-hidden="true"
                                             xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none"
                                             viewBox="0 0 24 24">
                                            <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round"
                                                  strokeWidth="2" d="m9 5 7 7-7 7"/>
                                        </svg>
                                    </a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>
            </section>
        </div>
    );
}