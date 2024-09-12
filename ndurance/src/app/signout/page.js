"use client";

import { useEffect, useState } from 'react';
import Cookies from 'js-cookie';
import {useRouter} from "next/navigation";

const SignOut = () => {
  const [message, setMessage] = useState('');
  const router = useRouter();

  useEffect(() => {
    // Clear the cookies
    Cookies.remove('jwt');
    Cookies.remove('userId');

    // Set a success message
    setMessage('You have successfully signed out.');

    // Redirect to home page after a delay
    setTimeout(() => {
      router.push('/');
    }, 2000); // Redirect after 2 seconds
  }, [router]);

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="p-6 bg-white rounded shadow-lg">
        <h1 className="text-2xl font-bold text-center mb-4">Sign Out</h1>
        <p className="text-center text-green-500">{message}</p>
      </div>
    </div>
  );
};

export default SignOut;
