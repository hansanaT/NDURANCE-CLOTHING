"use client";

import {Button, Checkbox, Label, TextInput, Toast,FileInput} from "flowbite-react";
import Link from "next/link";
import {FcGoogle} from "react-icons/fc";
import {HR} from 'flowbite-react';
import {Ftr} from "@/app/footer";
import {useState} from "react";
import {useRouter} from "next/navigation";
import {BiSolidHide, BiSolidShow} from "react-icons/bi";
import axios from "axios";
import {HiCheck, HiX} from "react-icons/hi";


const Register = () => {

    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const router = useRouter();

    const handleSignUp = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.currentTarget);
        try {
            const email = formData.get('email');
            const password = formData.get('password');
            const fname = formData.get('fname');
            const lname = formData.get('lname');

            if (!email || !password || !fname || !lname) {
                console.log("Email, password, first name or last name is missing");
                return;
            }
            const res = await axios.post(`http://localhost:8080/user-service/users`, {
                firstName: formData.get('fname'),
                    lastName: formData.get('lname'),
                email: formData.get('email'),
                password: formData.get('password'),
            },
                {withCredentials: true},
            );
            if(res.status === 200) {
                setSuccess("Account created successfully! Redirecting to login page...");
                setTimeout(() => {
                    router.push("/login");
                }, 2000);
            }
            else {
                setError(res.data.message ||"Unknown Error Occurred");
            }
        } catch (e) {
            setError(error.response?.data?.message || "Unknown Error Occurred");
        }
    }

    return (
        <div>
            <div className="flex flex-col items-center justify-center px-6 py-8 mx-auto lg:py-0">
                <Link href={"/"}>
                    <img src="/next.svg" alt="logo"
                         className="w-20 h-20 cursor-pointer"/>
                </Link>
                <div className="w-full bg-white rounded-lg shadow md:mt-0 sm:max-w-md xl:p-0">
                    <div className="p-6 space-y-4 md:space-y-4 sm:p-8">
                        <h1 className="text-xl text-center font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white">
                            Create Your Account
                        </h1>
                        <Button className="w-full font-semibold" color="gray"><FcGoogle className="w-5 h-5 mx-2"/>
                            Sign Up with Google
                        </Button>
                        <HR.Text text="or"/>
                        <form className="space-y-4 md:space-y-6" onSubmit={handleSignUp}>
                            <div className="flex space-x-3">
                                <div>
                                    <div className="mb-2 block">
                                        <Label htmlFor="FirstName" value="First Name"/>
                                    </div>
                                    <TextInput id="FirstName" name="fname" type="text" placeholder="John" required
                                               shadow/>
                                </div>
                                <div>
                                    <div className="mb-2 block">
                                        <Label htmlFor="LastName" value="Last Name"/>
                                    </div>
                                    <TextInput id="LastName" name="lname" type="text" placeholder="Doe" required
                                               shadow/>
                                </div>
                            </div>
                            <div>
                                <div className="mb-2 block">
                                    <Label htmlFor="emailfield" value="Your email"/>
                                </div>
                                <TextInput id="emailField" name="email" type="email" placeholder="someone@example.com"
                                           required shadow/>
                            </div>
                            <div>
                                <div className="mb-2 block">
                                    <Label htmlFor="passwordField" value="Your password"/>
                                </div>
                                <div className="relative w-full">
                                    <TextInput
                                        id="passField"
                                        name="password"
                                        type={showPassword ? "text" : "password"}
                                        placeholder="*******************"
                                        required
                                        shadow
                                    />
                                    <button
                                        className="absolute right-0 top-0 h-full px-4 flex items-center justify-center transition duration-150 ease"
                                        onClick={(e) => {
                                            e.preventDefault();
                                            setShowPassword(!showPassword);
                                        }}
                                    >
                                        {showPassword ? <BiSolidHide/> : <BiSolidShow/>}
                                    </button>
                                </div>
                            </div>
                            <div className="flex items-center gap-2">
                                <Checkbox id="agree"/>
                                <Label htmlFor="agree" className="flex">
                                    I agree with the&nbsp;
                                    <Link href="#" className="text-cyan-600 hover:underline dark:text-cyan-500">
                                        terms and conditions
                                    </Link>
                                </Label>
                            </div>
                            <Button className="w-full" type="submit">Sign Up</Button>
                        </form>
                        <div className="mb-2 block space-x-1 text-center">
                            <Label htmlFor="signIn" value="Already have an account?"/>
                            <Link id="signIn" href={"/login"}
                                  className="text-sm text-cyan-600 hover:underline dark:text-cyan-500">
                                Sign In
                            </Link>
                        </div>
                    </div>
                </div>
                <div className="flex flex-col gap-4">
                    {success &&
                        <Toast className="fixed z-50 bottom-10 right-5">
                            <div
                                className="inline-flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-green-100
                            text-green-500 dark:bg-green-800 dark:text-green-200">
                                <HiCheck className="h-5 w-5"/>
                            </div>
                            <div className="ml-3 text-sm font-normal">{success}</div>
                            <Toast.Toggle/>
                        </Toast>}
                    {error &&
                        <Toast className="fixed z-50 bottom-10 right-5">
                            <div
                                className="inline-flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-red-100
                            text-red-500 dark:bg-red-800 dark:text-red-200">
                                <HiX className="h-5 w-5"/>
                            </div>
                            <div className="ml-3 text-sm font-normal">{error}</div>
                            <Toast.Toggle/>
                        </Toast>}
                </div>
            </div>
            <Ftr/>
        </div>
    );
}

export default Register;