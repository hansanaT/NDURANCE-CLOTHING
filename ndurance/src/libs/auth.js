import NextAuth from "next-auth";
import CredentialsProvider from "next-auth/providers/credentials";
import axios from "axios";
import bcrypt from "bcryptjs";

export const authOptions = {
    providers: [
        CredentialsProvider({
            name: "Credentials",
            id: "credentials",
            credentials: {
                email: {label: "Email", type: "text",placeholder:"someone@example.com"},
                password: {label: "Password", type: "password"}
            },
            async authorize(credentials) {
                const {email, password} = credentials;

                try {
                    const response = await axios.post(`${process.env.SPRING_BOOT_USER_SERVICE_URL}/auth/login`, {
                        email,
                        password
                    });

                    const user = response.data;

                    if(!user) {
                        throw new Error("User Not Found for the provided credentials");
                    }
                    else if(!bcrypt.compare(password, user.password)) {
                        throw new Error("Invalid password");
                    }
                    return user;
                }
                catch (error) {
                    console.error("Error during authentication:", error);
                    throw new Error(error.response?.data?.message || "Authentication failed");
                }
            },
        }),
    ],
    pages: {
        signIn: "/login",
        signOut: "logout",
        error: "/error",
        newUser: null,
    },
    session:{
        strategy: "jwt",
    },
    callbacks:{
        async jwt({ token, user, trigger, session }) {
            if (trigger === "update" && session?.name) {
                token.name = session.name;
            }

            if (trigger === "update" && session?.email) {
                token.email = session.email;
            }

            if (user) {
                token.id = user.id;
                token.phone = user.phone;
            }

            return token;
        },
        async session({session, token}) {
            session.user = {
                ...session.user,
                id: token.id,
                name: token.name,
                email: token.email,
            };
            return session;
        },
    },
    secret: process.env.NEXTAUTH_SECRET,
};

export default NextAuth(authOptions);