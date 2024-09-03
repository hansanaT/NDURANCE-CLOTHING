import {NextResponse} from "next/server";
import axios from "axios";

export async function POST(req) {
    try {
        const {fname,lname,email,password} = await req.json();

        if(password.length < 8) {
            return NextResponse.json({message: "Password must be at least 8 characters long"}, {status: 400});
        }

        const response = await axios.post(`${process.env.SPRING_BOOT_USER_SERVICE_URL}/users`, {
            fname,
            lname,
            email,
            password
        });

        const savedUser = response.data;

        return NextResponse.json(
            {
                fname: savedUser.fname,
                lname: savedUser.lname,
                email: savedUser.email,
                createdAt: savedUser.createdAt,
                updatedAt: savedUser.updatedAt,
            }, {status: 201}
        );
    }
    catch (error) {
        console.error("Error during signup:", error);
        const status = error.response?.status || 500;
        return NextResponse.json({message: error.response?.data?.message || "Failed to signup"}, {status});
    }
}

//start post
export async function PUT(request) {
    try {
        const { userId, name, email, password, phone, address } = await request.json();

        if (password && password.length < 6) {
            return NextResponse.json(
                { message: "Password must be at least 6 characters" },
                { status: 400 }
            );
        }

        const response = await axios.put(
            `${process.env.SPRING_BOOT_USER_SERVICE_URL}/users/${userId}`,
            {
                name,
                email,
                password,
                phone,
                address,
            }
        );

        const updatedUser = response.data;

        return NextResponse.json(
            {
                message: "User updated successfully",
                updatedUser: {
                    id: updatedUser.id,
                    name: updatedUser.name,
                    email: updatedUser.email,
                    createdAt: updatedUser.createdAt,
                    updatedAt: updatedUser.updatedAt,
                },
            },
            { status: 200 }
        );
    } catch (error) {
        console.error("Error during user update:", error);
        const status = error.response?.status || 500;
        return NextResponse.json(
            { message: error.response?.data?.message || "Internal Server Error" },
            { status }
        );
    }
}

//start delete
export async function DELETE(request) {
    try {
        const { userId } = await request.json();

        await axios.delete(
            `${process.env.SPRING_BOOT_USER_SERVICE_URL}/users/${userId}`
        );

        return NextResponse.json(
            { message: "User deleted successfully" },
            { status: 200 }
        );
    } catch (error) {
        console.error("Error during user deletion:", error);
        const status = error.response?.status || 500;
        return NextResponse.json(
            { message: error.response?.data?.message || "Internal Server Error" },
            { status }
        );
    }
}
