import { NextResponse } from 'next/server';

export async function middleware(req) {
    const jwt = req.cookies.get('jwt')?.value;

    if (!jwt) {
        return NextResponse.redirect(new URL('/login', req.url));
    }

    return NextResponse.next();
}

export const config = {
    matcher: ['/'], //protected routes
};