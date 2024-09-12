"use client";

import React, { useEffect, useState } from "react";
import axios from "axios";
import Resizer from "react-image-file-resizer";
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

  const categories = [
    "TOPS", "BOTTOMS", "DRESSES", "OUTERWEAR", "FOOTWEAR", "ACCESSORIES",
    "UNDERGARMENTS", "ACTIVEWEAR", "SLEEPWEAR", "SWIMWEAR"
  ];

  // Fetch products on component mount
  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await axios.get('http://localhost:8080/product-service/products');
        const productData = response.data;
        setProducts(productData);
        setFilteredProducts(productData); // Initially, display all products
        resizeProductImages(productData); // Resize images for the initial load
      } catch (err) {
        setError("Failed to fetch products.");
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  // Resize images
  const resizeProductImages = (productData) => {
    productData.forEach((product) => {
      const imageUrl = `http://localhost:8080/product-service/products/images/${product.images[0]}`;
      getOriginalImageSize(imageUrl).then((dimensions) => {
        const { width, height } = dimensions;
        let newMaxWidth = 300;
        let newMaxHeight = 300;

        if (width > 1500 || height > 1500) {
          newMaxWidth = 600;
          newMaxHeight = 600;
        } else if (width > 1000 || height > 1000) {
          newMaxWidth = 400;
          newMaxHeight = 400;
        } else if (width > 500 || height > 500) {
          newMaxWidth = 300;
          newMaxHeight = 300;
        }

        resizeFile(imageUrl, newMaxWidth, newMaxHeight).then((resizedImage) => {
          setResizedImages((prev) => ({
            ...prev,
            [product.id]: resizedImage,
          }));
        });
      });
    });
  };

  const getOriginalImageSize = (imageUrl) => {
    return new Promise((resolve) => {
      const img = new Image();
      img.src = imageUrl;
      img.onload = () => {
        resolve({ width: img.width, height: img.height });
      };
    });
  };

  const resizeFile = (imageUrl, maxWidth, maxHeight) =>
    new Promise(async (resolve, reject) => {
      try {
        const response = await fetch(imageUrl);
        const blob = await response.blob();

        Resizer.imageFileResizer(
          blob,
          maxWidth,
          maxHeight,
          "JPEG",
          100,
          0,
          (uri) => {
            resolve(uri);
          },
          "base64",
          (err) => {
            reject(err);
          }
        );
      } catch (error) {
        reject(error);
      }
    });

  // Handle the search input
  const handleSearch = (e) => {
    const value = e.target.value.toLowerCase();
    setSearchTerm(value);
  };

  // Handle category change
  const handleCategoryChange = (e) => {
    const category = e.target.value;
    setSelectedCategory(category);
  };

  // Handle price range change
  const handlePriceChange = (min, max) => {
    setMinPrice(min);
    setMaxPrice(max);
  };

  // Filtering products based on all criteria
  useEffect(() => {
    const applyFilters = () => {
      let updatedList = [...products]; // Start with the full list of products

      // Filter by search term
      if (searchTerm) {
        updatedList = updatedList.filter(
          (product) =>
            new RegExp(searchTerm, "i").test(product.name) ||
            new RegExp(searchTerm, "i").test(product.description)
        );
      }

      // Filter by category
      if (selectedCategory) {
        updatedList = updatedList.filter(
          (product) => product.type.toUpperCase() === selectedCategory
        );
      }

      // Filter by price range
      updatedList = updatedList.filter(
        (product) => product.price >= minPrice && product.price <= maxPrice
      );

      setFilteredProducts(updatedList);
    };

    applyFilters();
  }, [searchTerm, selectedCategory, minPrice, maxPrice, products]);

  if (loading) return <p className="text-center text-lg">Loading products...</p>;
  if (error) return <p className="text-center text-lg text-red-500">{error}</p>;

  return (
    <div>
      <Navigation />
      <div className="container mx-auto py-8">
        <h1 className="text-4xl font-bold text-center mb-6">Products</h1>

        {/* Search Bar */}
        <div className="mb-6 flex justify-center">
          <input
            type="text"
            value={searchTerm}
            onChange={handleSearch}
            placeholder="Search products..."
            className="border-2 border-gray-300 p-2 rounded-md w-80"
          />
        </div>

        {/* Category Filter */}
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

        {/* Price Range */}
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

        {/* Products Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredProducts.map((product) => (
            <div key={product.id} className="bg-white shadow-lg rounded-lg overflow-hidden transition-transform hover:scale-105 duration-300">
              <img
                src={resizedImages[product.id] || `http://localhost:8080/product-service/products/images/${product.images[0]}`}
                alt={product.name}
                className="object-cover h-48 w-full"
              />
              <div className="p-4">
                <h2 className="text-xl font-semibold mb-2 text-gray-800">{product.name}</h2>
                <p className="text-gray-600 mb-4">{product.description}</p>
                <p className="text-green-500 text-lg font-bold mb-4">Price: ${product.price}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default ProductsPage;
