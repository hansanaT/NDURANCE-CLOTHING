"use client";

import React, { useEffect, useState } from "react";
import axios from "axios";
import Link from "next/link"; 
import Navigation from "../navigation";

const ProductsPage = () => {
  const [products, setProducts] = useState([]);
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("");
  const [minPrice, setMinPrice] = useState(0);
  const [maxPrice, setMaxPrice] = useState(1000);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [resizedImages, setResizedImages] = useState({});

  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [size, setSize] = useState(20);

  const categories = [
    "TOPS", "BOTTOMS", "DRESSES", "OUTERWEAR", "FOOTWEAR", "ACCESSORIES",
    "UNDERGARMENTS", "ACTIVEWEAR", "SLEEPWEAR", "SWIMWEAR","WOMENS","HATS","MENS"
  ];

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/product-service/products`, {
          params: { page, size }
        });
        const productData = response.data.content;
        setProducts(productData);
        setFilteredProducts(productData); 
        setTotalPages(response.data.totalPages); 
       
      } catch (err) {
        console.error(err);
        setError("Failed to fetch products.");
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, [page, size]);

  const handleSearch = (e) => {
    const value = e.target.value.toLowerCase();
    setSearchTerm(value);
  };

  const handleCategoryChange = (e) => {
    const category = e.target.value;
    setSelectedCategory(category);
  };

  const handlePriceChange = (min, max) => {
    setMinPrice(min);
    setMaxPrice(max);
  };

  useEffect(() => {
    const applyFilters = () => {
      let updatedList = [...products];

      if (searchTerm) {
        updatedList = updatedList.filter(
          (product) =>
            new RegExp(searchTerm, "i").test(product.name) ||
            new RegExp(searchTerm, "i").test(product.description)
        );
      }

      if (selectedCategory) {
        updatedList = updatedList.filter(
          (product) => product.type.toUpperCase() === selectedCategory
        );
      }

      updatedList = updatedList.filter(
        (product) => product.price >= minPrice && product.price <= maxPrice
      );

      setFilteredProducts(updatedList);
    };

    applyFilters();
  }, [searchTerm, selectedCategory, minPrice, maxPrice, products]);

  const handleNextPage = () => {
    if (page < totalPages - 1) {
      setPage(page + 1);
    }
  };

  const handlePreviousPage = () => {
    if (page > 0) {
      setPage(page - 1);
    }
  };

  if (loading) return <p className="text-center text-lg">Loading products...</p>;
  if (error) return <p className="text-center text-lg text-red-500">{error}</p>;

  return (
    <div>
      <Navigation />
      <div className="container mx-auto py-8">
        <h1 className="text-4xl font-bold text-center mb-6">Products</h1>

        <div className="mb-6 flex justify-center">
          <input
            type="text"
            value={searchTerm}
            onChange={handleSearch}
            placeholder="Search products..."
            className="border-2 border-gray-300 p-2 rounded-md w-80"
          />
        </div>

        <div className="mb-6 flex justify-center">
          <select
            value={selectedCategory}
            onChange={handleCategoryChange}
            className="border-2 border-gray-300 p-2 rounded-md w-80"
          >
            <option value="">All Categories</option>
            {categories.map((category) => (
              <option key={category} value={category}>
                {category}
              </option>
            ))}
          </select>
        </div>

        <div className="mb-6 flex justify-center gap-4">
          <input
            type="number"
            value={minPrice}
            onChange={(e) => handlePriceChange(e.target.value, maxPrice)}
            placeholder="Min Price"
            className="border-2 border-gray-300 p-2 rounded-md w-36"
          />
          <input
            type="number"
            value={maxPrice}
            onChange={(e) => handlePriceChange(minPrice, e.target.value)}
            placeholder="Max Price"
            className="border-2 border-gray-300 p-2 rounded-md w-36"
          />
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredProducts.map((product) => (
            <div key={product.productId} className="bg-white shadow-lg rounded-lg overflow-hidden transition-transform hover:scale-105 duration-300">
              <img
                src={resizedImages[product.productId] || `http://localhost:8080/product-service/products/images/${product.images[0]}`}
                alt={product.name}
                className="object-cover h-48 w-full"
              />
              <div className="p-4">
                <Link href={`/product/${product.productId}`}>
                  <h2 className="text-xl font-semibold mb-2 text-gray-800 cursor-pointer hover:underline">
                    {product.name}
                  </h2>
                </Link>
                <p className="text-gray-600 mb-4">{product.description}</p>
                <p className="text-green-500 text-lg font-bold mb-4">Price: ${product.price}</p>
              </div>
            </div>
          ))}
        </div>

        <div className="flex justify-center mt-6">
          <button
            onClick={handlePreviousPage}
            className="mr-4 px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400"
            disabled={page === 0}
          >
            Previous
          </button>
          <button
            onClick={handleNextPage}
            className="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400"
            disabled={page === totalPages - 1}
          >
            Next
          </button>
        </div>
      </div>
    </div>
  );
};

export default ProductsPage;
