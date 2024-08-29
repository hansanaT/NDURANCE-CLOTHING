import Navigation from "./navigation";
import './globals.css';
import {Button} from "flowbite-react";
import SHOP_DATA from "@/shopData";

export const metadata = {
    title: "NDURANCE Clothing | Home",
    description: "Attire on the cutting edge of fashion.",
};

export default function Home() {

    const categoryData = SHOP_DATA;

    return (
        <div>
            <Navigation/>
            <div
                className="items-center text-center justify-center w-full bg-gradient-to-r from-violet-200 to-pink-200 px-5 py-12 h-dvh
                flex z-[100] break-normal">
                <div>
                    <h1 className="text-6xl text-black font-bold">Discover the Must-Have Fashion of the Season!</h1>
                    <p className="break-normal text-2xl py-5">Explore our curated collection of stylish pieces designed
                        to turn heads.
                        From timeless classics to the latest trends,
                        find everything you need to express your unique style. Shop now and step into a world of fashion
                        like never before.</p>
                </div>
            </div>
            <div className="carousel carousel-center rounded-box space-x-4 p-4 overflow-hidden">
                <div className="carousel-track flex space-x-4 animate-scroll">
                    {categoryData.map((category) => (
                        category.items.slice(0, 3).map((item) => (
                            <div className="carousel-item relative">
                                <img
                                    src={item.imageUrl}
                                    alt={item.name}
                                    className="rounded-box"/>
                                <div
                                    className="justify-center absolute bottom-0 rounded-md bg-white bg-opacity-70 p-2 flex gap-3">
                                    <p className="text-md font-bold mt-2">${item.price}</p>
                                    <Button>Add to Cart</Button>
                                </div>
                            </div>
                        ))
                    ))}
                    {categoryData.map((category) => (
                        category.items.slice(0, 3).map((item) => (
                            <div className="carousel-item relative">
                                <img
                                    src={item.imageUrl}
                                    alt={item.name}
                                    className="rounded-box"/>
                                <div
                                    className="justify-center absolute bottom-0 rounded-md bg-white bg-opacity-70 p-2 flex gap-3">
                                    <p className="text-md font-bold mt-2">${item.price}</p>
                                    <Button>Add to Cart</Button>
                                </div>
                            </div>
                        ))
                    ))}
                </div>
            </div>
        </div>
    );
}
