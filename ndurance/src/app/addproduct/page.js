"use client";
import React, { useState } from "react";
import Cookies from 'js-cookie';
import Navigation from "../navigation";
import { Textarea } from "flowbite-react";

export default function AddProduct() {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [price, setPrice] = useState('');
    const [type, setType] = useState('HATS');
    const [images, setImages] = useState([]);
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

        const response = await fetch('http://localhost:8080/product-service/products', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
            },
            body: formData,
        });

        if (response.ok) {
            alert('Product added successfully');
        } else {
            alert('Failed to add product');
        }
    };

    const handleFileChange = (e) => {
        setImages([...e.target.files]);
    };

    return (
        <div>
            <Navigation />
            <div>
                <h1>Add New Product</h1>
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
                        <Textarea
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
                            <option value="TOPS">HATS</option>
                            <option value="OUTERWEAR">OUTERWEAR</option>
                            <option value="FOOTWEAR">FOOTWEAR</option>
                            <option value="MENS">MENS</option>
                            <option value="WOMENS">WOMENS</option>
                        </select>
                    </div>
                    <div>
                        <label>Images:</label>
                        <input
                            type="file"
                            multiple
                            onChange={handleFileChange}
                            required
                        />
                    </div>
                    <button type="submit">Add Product</button>
                </form>
            </div>
        </div>
    );
}
