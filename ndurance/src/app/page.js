"use client";

import Navigation from "./navigation";
import {Button} from "flowbite-react";

export default function Home() {
    return (
        <div>
            <Navigation/>
            <Button>Add to Cart</Button>
            <Button>Remove from Cart</Button>
        </div>
    );
}
