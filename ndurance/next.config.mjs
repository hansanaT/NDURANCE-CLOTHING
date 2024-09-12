/** @type {import('next').NextConfig} */
const nextConfig = {
    images: {
        remotePatterns: [
          {
            protocol: 'http',
            hostname: 'localhost',
            port: '8080', // Include the port if your API is hosted on a specific port
            pathname: '/product-service/products/images/**',
          },
        ],
      },
};

export default nextConfig;
