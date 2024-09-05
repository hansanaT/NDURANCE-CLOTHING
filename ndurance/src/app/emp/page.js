"use client";

import {Button, Label, TextInput} from "flowbite-react";
import Link from "next/link";
import {Ftr} from "@/app/footer";

export default function Login() {
    return (
        <div>
            <div className="flex flex-col items-center justify-center px-6 py-8 mx-auto md:h-screen lg:py-0">
                    <img src="/next.svg" alt="logo"
                         className="w-20 h-20 cursor-pointer"/>
                <div className="w-full bg-white rounded-lg shadow md:mt-0 sm:max-w-md xl:p-0">
                    <div className="p-6 space-y-4 md:space-y-4 sm:p-8">
                        <h1 className="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white">
                            Sign in to your account
                        </h1>
                        <form className="space-y-4 md:space-y-6">
                            <div>
                                <div className="mb-2 block">
                                    <Label htmlFor="email2" value="Your email"/>
                                </div>
                                <TextInput id="email2" type="email" placeholder="someone@example.com" required shadow/>
                            </div>
                            <div>
                                <div className="mb-2 block">
                                    <Label htmlFor="password2" value="Your password"/>
                                </div>
                                <TextInput id="password2" type="password" placeholder="*******************" required shadow/>
                                <Link href={"/"} className="text-sm text-cyan-600 hover:underline dark:text-cyan-500">
                                    Forgot password?
                                </Link>
                            </div>
                            <Button className="w-full" type="submit">Login</Button>
                        </form>
                    </div>
                </div>
            </div>
            <Ftr/>
        </div>
    );
}