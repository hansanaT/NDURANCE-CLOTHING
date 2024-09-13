"use client";
import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Cookies from 'js-cookie';

export default function updateProduct({ params }) {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [price, setPrice] = useState('');
    const [type, setType] = useState('DRESSES');
    const [images, setImages] = useState([]);
    const [initialImages, setInitialImages] = useState([]);
    const router = useRouter();
    const { productId } = params;
    useEffect(() => {
        if (productId) {
            fetchProductDetails(productId);
        }
    }, [productId]);

    const fetchProductDetails = async (id) => {
        try {
            const response = await fetch(`http://localhost:8080/product-service/products/${productId}`);
            if (response.ok) {
                const product = await response.json();
                setName(product.name);
                setDescription(product.description);
                setPrice(product.price);
                setType(product.type);
                setInitialImages(product.images || []);
            } else {
                alert('Failed to fetch product details');
            }
        } catch (error) {
            console.error('Error fetching product details:', error);
            alert('An error occurred while fetching product details');
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData();
        formData.append('name', name);
        formData.append('description', description);
        formData.append('price', price);
        formData.append('type', type);

        images.forEach((file) => {
            formData.append('images', file);
        });

        const token = Cookies.get('jwt');

        try {
            const response = await fetch(`http://localhost:8080/product-service/products/${productId}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
                body: formData,
            });

            if (response.ok) {
                alert('Product updated successfully');
                router.push('/products'); // Redirect after successful update
            } else {
                alert('Failed to update product');
            }
        } catch (error) {
            console.error('Error updating product:', error);
            alert('An error occurred while updating the product');
        }
    };

    const handleFileChange = (e) => {
        setImages([...e.target.files]);
    };

    return (
        <div>
            <h1>Update Product</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Name:</label>
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Description:</label>
                    <input
                        type="text"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Price:</label>
                    <input
                        type="number"
                        value={price}
                        onChange={(e) => setPrice(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Type:</label>
                    <select
                        value={type}
                        onChange={(e) => setType(e.target.value)}
                    >
                        <option value="HATS">TOPS</option>
                        <option value="OUTERWEAR">OUTERWEAR</option>
                        <option value="FOOTWEAR">FOOTWEAR</option>
                        <option value="WOMENS">ACTIVEWEAR</option>
                        <option value="MENS">SLEEPWEAR</option>
                    </select>
                </div>
                <div>
                    <label>Images:</label>
                    <input
                        type="file"
                        multiple
                        onChange={handleFileChange}
                    />
                    {initialImages.length > 0 && (
                        <div>
                            <h3>Current Images:</h3>
                            {initialImages.map((image, index) => (
                                <img
                                    key={index}
                                    src={`http://localhost:8080/product-service/products/images/${image}`} // Adjust if your backend returns different URL format
                                    alt={`Current image ${index + 1}`}
                                    style={{ width: '100px', height: 'auto', margin: '5px' }}
                                />
                            ))}
                        </div>
                    )}
                </div>
                <button type="submit">Update Product</button>
            </form>
        </div>
    );
}
