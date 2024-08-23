import Navigation from "@/app/navigation";

export default function ({params}) {
    return (
        <div>
            <Navigation/>
            <h1>Category: {params.name}</h1>
        </div>
    );
}