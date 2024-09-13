"use client";

import Navigation from "../navigation";
import {useRouter} from "next/navigation";
import {Button} from "flowbite-react";

export default function CategoryHome() {

    const router = useRouter();
    const navigateToHats = () => {
        router.push('/category/hats');
    }
    const navigateToFootwear = () => {
        router.push('/category/footwear');
    }
    const navigateToOuterwear = () => {
        router.push('/category/outerwear');
    }
    const navigateToWomens = () => {
        router.push('/category/womens');
    }
    const navigateToMens = () => {
        router.push('/category/mens');
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
                                className="grid-cols-3 space-x-3 space-y-3 w-full">
                                <div className="flex">
                                    <Button color="gray" onClick={navigateToHats} className="w-full mx-3 border-2">Hats</Button>
                                    <Button color="gray" onClick={navigateToFootwear}
                                            className="w-full mx-3 border-2">Footwear</Button>
                                    <Button color="gray" onClick={navigateToOuterwear}
                                            className="w-full mx-3 border-2">Outerwear</Button>
                                </div>
                                <div className="flex">
                                    <Button color="gray" onClick={navigateToWomens} className="w-full mx-3 border-2">Womens</Button>
                                    <Button color="gray" onClick={navigateToMens} className="w-full mx-3 border-2">Mens</Button>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
