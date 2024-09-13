import { Inter } from "next/font/google";
import "./globals.css";
import {CartProvider} from "@/context/CartContext";

const inter = Inter({ subsets: ["latin"] });

export const metadata = {
  title: "NDURANCE Clothing",
  description: "Attire on the cutting edge of fashion.",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body className={inter.className}>
      <CartProvider>
          {children}
      </CartProvider>
      </body>
    </html>
  );
}
