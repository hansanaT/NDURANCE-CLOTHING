"use client";

import Navigation from "../navigation";
import {useRouter} from "next/navigation";

export default function CategoryHome() {

    const router = useRouter();
    const navigateToWomens = () => {
        router.push('/category/womens');
    }
    const navigateToMens = () => {
        router.push('/category/mens');
    }
    const navigateToSneakers = () => {
        router.push('/category/sneakers');
    }
    const navigateToHats = () => {
        router.push('/category/hats');
    }

    return (
        <div>
            <Navigation/>
            <div className="pb-16 mx-5">
                <div className="flex justify-center items-center">
                    <div className="2xl:mx-auto 2xl:container py-12 px-4 sm:px-6 xl:px-20 2xl:px-0 w-full">
                        <div className="flex flex-col jusitfy-center items-center space-y-10">
                            <div className="flex flex-col justify-center items-center space-y-2">
                                <h1 className="text-3xl xl:text-4xl font-semibold leading-7 xl:leading-9 text-gray-800">Shop
                                    By Category</h1>
                            </div>
                            <div
                                className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 md:gap-x-4 w-full">
                                <div className="relative group flex justify-center items-center h-full w-full">
                                    <img className="object-center object-cover h-full w-full"
                                         src="https://i.ibb.co/ThPFmzv/omid-armin-m-VSb6-PFk-VXw-unsplash-1-1.png"
                                         alt="girl-image"/>
                                    <button
                                        onClick={navigateToWomens}
                                        className="focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-400 bottom-4 z-10 absolute text-base font-medium leading-none text-gray-800 py-3 w-36 bg-white">
                                        Women
                                    </button>
                                    <div
                                        className="absolute opacity-0 group-hover:opacity-100 transition duration-500 bottom-3 py-6 z-0 px-20 w-36 bg-white bg-opacity-50"/>
                                </div>
                                <div className="flex flex-col space-y-4 md:space-y-8 mt-4 md:mt-0">
                                    <div className="relative group flex justify-center items-center h-full w-full">
                                        <img className="object-center object-cover h-full w-full"
                                             src="https://img.freepik.com/premium-photo/shoe-running-line-icon-ai-generated_1185057-4514.jpg?w=826"
                                             alt="shoe-image"/>
                                        <button
                                            onClick={navigateToSneakers}
                                            className="focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-400 bottom-4 z-10 absolute text-base font-medium leading-none text-gray-800 py-3 w-36 bg-white">
                                            Sneakers
                                        </button>
                                        <div
                                            className="absolute opacity-0 group-hover:opacity-100 transition duration-500 bottom-3 py-6 z-0 px-20 w-36 bg-white bg-opacity-50"/>
                                    </div>
                                    <div className="relative group flex justify-center items-center h-full w-full">
                                        <img className="object-center object-cover h-full w-full"
                                             src="https://i.ibb.co/DQDpbYc/fedora-hats-studio-still-life.jpg"
                                             alt="watch-image"/>
                                        <button
                                            onClick={navigateToHats}
                                            className="focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-400 bottom-4 z-10 absolute text-base font-medium leading-none text-gray-800 py-3 w-36 bg-white">
                                            Hats
                                        </button>
                                        <div
                                            className="absolute opacity-0 group-hover:opacity-100 transition duration-500 bottom-3 py-6 z-0 px-20 w-36 bg-white bg-opacity-50"/>
                                    </div>
                                </div>
                                <div
                                    className="relative group justify-center items-center h-full w-full hidden lg:flex">
                                    <img className="object-center object-cover h-full w-full"
                                         src="https://i.ibb.co/3pjQp9B/full-body-studio-portrait-stylish-bearded-male-dressed-suit-sunglasses-dark-grey-background.jpg"
                                         alt="girl-image"/>
                                    <button
                                        onClick={navigateToMens}
                                        className="focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-400 bottom-4 z-10 absolute text-base font-medium leading-none text-gray-800 py-3 w-36 bg-white">
                                        Mens
                                    </button>
                                    <div
                                        className="absolute opacity-0 group-hover:opacity-100 transition duration-500 bottom-3 py-6 z-0 px-20 w-36 bg-white bg-opacity-50"/>
                                </div>
                                <div
                                    className="relative group flex justify-center items-center h-full w-full mt-4 md:hidden md:mt-8 lg:hidden">
                                    <img className="object-center object-cover h-full w-full hidden md:block"
                                         src="https://i.ibb.co/3pjQp9B/full-body-studio-portrait-stylish-bearded-male-dressed-suit-sunglasses-dark-grey-background.jpg"
                                         alt="girl-image"/>
                                    <img className="object-center object-cover h-full w-full md:hidden"
                                         src="https://i.ibb.co/3pjQp9B/full-body-studio-portrait-stylish-bearded-male-dressed-suit-sunglasses-dark-grey-background.jpg"
                                         alt="olive-tatiane-Im-Ez-F9-B91-Mk-unsplash-2"/>
                                    <button
                                        onClick={navigateToMens}
                                        className="focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-400 bottom-4 z-10 absolute text-base font-medium leading-none text-gray-800 py-3 w-36 bg-white">
                                        Mens
                                    </button>
                                    <div
                                        className="absolute opacity-0 group-hover:opacity-100 transition duration-500 bottom-3 py-6 z-0 px-20 w-36 bg-white bg-opacity-50"/>
                                </div>
                            </div>
                            <div
                                className="relative group hidden md:flex justify-center items-center h-full w-full mt-4 md:mt-8 lg:hidden">
                                <img className="object-center object-cover h-full w-full hidden md:block"
                                     src="https://i.ibb.co/3pjQp9B/full-body-studio-portrait-stylish-bearded-male-dressed-suit-sunglasses-dark-grey-background.jpg"
                                     alt="girl-image"/>
                                <img className="object-center object-cover h-full w-full sm:hidden"
                                     src="https://i.ibb.co/3pjQp9B/full-body-studio-portrait-stylish-bearded-male-dressed-suit-sunglasses-dark-grey-background.jpg"
                                     alt="olive-tatiane-Im-Ez-F9-B91-Mk-unsplash-2"/>
                                <button
                                    onClick={navigateToMens}
                                    className="focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-400 bottom-4 z-10 absolute text-base font-medium leading-none text-gray-800 py-3 w-36 bg-white">
                                    Mens
                                </button>
                                <div
                                    className="absolute opacity-0 group-hover:opacity-100 transition duration-500 bottom-3 py-6 z-0 px-20 w-36 bg-white bg-opacity-50"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
