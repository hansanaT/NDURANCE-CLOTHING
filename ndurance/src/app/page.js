import Navigation from "./navigation";
import './globals.css';
import {Button, HR, TextInput} from "flowbite-react";
import SHOP_DATA from "@/shopData";
import {Ftr} from "@/app/footer";

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
            <div className="items-center justify-center flex mt-10 mb-10 space-x-4">
                <div>
                    <h2 className="text-4xl font-bold">New Arrivals</h2>
                    <p className="text-2xl break-normal">Discover the latest trends in fashion <br/>and elevate your
                        style with our
                        collection of new arrivals.</p>
                </div>
                <div>
                    <img src="https://i.ibb.co/7CQVJNm/blue-tank.png" alt="New Arrivals" className="rounded-box"/>
                </div>
            </div>
            <div className="items-center justify-center flex mt-10 mb-10 space-x-4">
                <div>
                    <h2 className="text-4xl font-bold">RUN!</h2>
                    <p className="text-2xl break-normal">Discover the latest trends in footwear <br/>and elevate your
                        style with our
                        collection of new arrivals.</p>
                </div>
                <div>
                    <img src="https://i.ibb.co/1RcFPk0/white-nike-high-tops.png" alt="Footwear"
                         className="rounded-box"/>
                </div>
            </div>
            <div className="items-center justify-center flex mt-10 mb-10 space-x-4">
                <div>
                    <h2 className="text-4xl font-bold">Summer's Here</h2>
                    <p className="text-2xl break-normal">Discover the latest trends on Hats <br/>and elevate your
                        style with our
                        collection of new arrivals.</p>
                </div>
                <div>
                    <img src="https://i.ibb.co/QdJwgmp/brown-cowboy.png" alt="Hats"
                         className="rounded-box"/>
                </div>
            </div>
            <div className="items-center justify-center text-center border rounded-lg bg-blue-500 mx-32 my-5">
                <h1 className="text-6xl text-white font-bold py-5">Sign Up to Our Newsletter</h1>
                <p className="break-normal text-2xl text-white">Get the latest updates on new arrivals,exclusive offers, and
                    more.</p>
                <div className="flex items-center justify-center pt-6">
                    <TextInput
                        id="email3"
                        type="email"
                        placeholder="name@example.com"
                        required
                    />
                </div>
                <div className="flex items-center justify-center my-6">
                    <Button>Sign Up</Button>
                </div>
            </div>
            <HR.Trimmed/>
            <Ftr/>
        </div>
    );
}
