"use client";

import {Button, Checkbox, Label, TextInput} from "flowbite-react";
import Link from "next/link";
import {FcGoogle} from "react-icons/fc";
import {HR} from 'flowbite-react';
import {Ftr} from "@/app/footer";
import {useState} from "react";
import {useRouter} from "next/navigation";
import axios from "axios";
import { Toast } from "flowbite-react";
import { HiCheck, HiX } from "react-icons/hi";
import {BiSolidHide, BiSolidShow} from "react-icons/bi";

const SignIn = () => {
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const router = useRouter();

    const handleSignIn = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.currentTarget);
        try {
            const email = formData.get('email');
            const password = formData.get('password');

            if (!email || !password) {
                console.log("Email or password is missing");
                return;
            }
            const res = await axios.post(`http://localhost:8080/user-service/users/login`, {
                email: formData.get('email'),
                password: formData.get('password')
            },
                {withCredentials: true}
            );

            const user = document.cookie.includes('userId');

            if(user) {
                setSuccess("Login Successful");
                setTimeout(() => {
                    router.push("/");
                }, 2000);
            }
            else {
                setError(res.data.message || "Login Failed");
            }
        } catch (e) {
            setError(error.response?.data?.message || "Invalid email or password");
        }
    };

    return (
        <div>
            <div className="flex flex-col items-center justify-center px-6 py-8 mx-auto md:h-screen lg:py-0">
                <Link href={"/"}>
                    <img src="/next.svg" alt="logo"
                         className="w-20 h-20 cursor-pointer"/>
                </Link>
                <div className="w-full bg-white rounded-lg shadow md:mt-0 sm:max-w-md xl:p-0">
                    <div className="p-6 space-y-4 md:space-y-4 sm:p-8">
                        <h1 className="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white">
                            Sign in to your account
                        </h1>
                        <Button className="w-full font-semibold" color="gray"><FcGoogle className="w-5 h-5 mx-2"/>
                            Log in with Google
                        </Button>
                        <HR.Text text="or"/>
                        <form className="space-y-4 md:space-y-6" onSubmit={handleSignIn}>
                            <div>
                                <div className="mb-2 block">
                                    <Label htmlFor="emailField" value="Your email"/>
                                </div>
                                <TextInput id="emailField" name="email" type="email" placeholder="someone@example.com"
                                           required shadow/>
                            </div>
                            <div>
                                <div className="mb-2 block">
                                    <Label htmlFor="passField" value="Your password"/>
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
                                <Link href={"/"} className="text-sm text-cyan-600 hover:underline dark:text-cyan-500">
                                    Forgot password?
                                </Link>
                            </div>
                            <div className="flex items-center gap-2">
                                <Checkbox id="agree" required/>
                                <Label htmlFor="agree" className="flex">
                                    I agree with the&nbsp;
                                    <Link href="#" className="text-cyan-600 hover:underline dark:text-cyan-500">
                                        terms and conditions
                                    </Link>
                                </Label>
                            </div>
                            <Button className="w-full" type="submit">Login</Button>
                        </form>
                        <div className="mb-2 block space-x-1 text-center">
                            <Label htmlFor="signIn" value="Don't have an account?"/>
                            <Link id="signIn" href={"/register"}
                                  className="text-sm text-cyan-600 hover:underline dark:text-cyan-500">
                                Sign Up
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

export default SignIn;
