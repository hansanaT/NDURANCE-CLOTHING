"use client";

import { useEffect, useState } from 'react';
import Image from 'next/image';
import Navigation from "@/app/navigation";
import Cookies from 'js-cookie'; // Import js-cookie for cookies
import axios from "axios";

const ProductPage = ({ params }) => {
  const { productId } = params;
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [newComment, setNewComment] = useState('');
  const [commentSuccess, setCommentSuccess] = useState(false);
  const [userDetails, setUserDetails] = useState({});
  const [isAdmin, setIsAdmin] = useState(false);

  const user = async () => {
    const token = Cookies.get('jwt');
    const userId = Cookies.get('userId');

    if (!token || !userId) {
      setError('User not authenticated');
      return;
    }

    try {
      const response = await axios.get(`http://localhost:8080/user-service/users/${userId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setUserDetails(response.data);
      setIsAdmin(response.data.roles.includes('ROLE_ADMIN'));
    } catch (err) {
      console.error(err);
      //setError('Failed to fetch user details');
    }
  };

  useEffect(() => {
    user();
  }, []);

  useEffect(() => {
    if (productId) {
      fetchProduct();
    }
  }, [productId]);

  const fetchProduct = async () => {
    try {
      const res = await fetch(`http://localhost:8080/product-service/products/${productId}`);
      if (!res.ok) {
        throw new Error('Failed to fetch product');
      }
      const data = await res.json();
      setProduct(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleCommentSubmit = async (e) => {
    e.preventDefault();

    const token = Cookies.get('jwt');
    const userId = Cookies.get('userId');

    if (!token || !userId) {
      setError('User not authenticated');
      return;
    }

    try {
      const res = await fetch(`http://localhost:8080/product-service/products/comments/${userId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({
          comment: newComment,
          clothPublicId: productId,
        }),
      });

      if (!res.ok) {
        throw new Error('Failed to post comment');
      }

      setNewComment('');
      setCommentSuccess(true);
      fetchProduct(); // Refresh product data
    } catch (err) {
      alert(err.message);
    }
  };

  const handleDeleteProduct = async () => {
    const token = Cookies.get('jwt');

    if (!token) {
      setError('User not authenticated');
      return;
    }

    try {
      const res = await fetch(`http://localhost:8080/product-service/products/${productId}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      console.log(res)
      if (!res.ok) {
        throw new Error('Failed to delete product');
      }

      alert('Product deleted successfully');
    } catch (err) {
      alert(err.message);
    }
  };

  const handleUpdateProduct = () => {
    window.location.href = `/update-product/${productId}`;
  };

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error}</p>;

  return (
    <div className="container mx-auto p-4">
      <Navigation />
      <div className="mt-8">
        {product && (
          <>
            <h1 className="text-4xl font-bold mb-4">{product.name}</h1>
            <div className="flex space-x-4">
              {product.images.map((img, index) => (
                <Image
                  key={index}
                  src={`http://localhost:8080/product-service/products/images/${img}`}
                  alt={product.name}
                  width={500}
                  height={500}
                  className="rounded-lg"
                />
              ))}
            </div>
            <p className="text-lg mt-4">{product.description}</p>
            <p className="text-2xl font-semibold mt-2">Price: ${product.price}</p>
            <p className="text-lg text-gray-600 mt-2">Category: {product.type}</p>

            {isAdmin && (
              <div className="mt-6">
                <button
                  onClick={handleUpdateProduct}
                  className="bg-yellow-500 text-white px-4 py-2 rounded-lg mr-2"
                >
                  Update Product
                </button>
                <button
                  onClick={handleDeleteProduct}
                  className="bg-red-500 text-white px-4 py-2 rounded-lg"
                >
                  Delete Product
                </button>
              </div>
            )}

            <h3 className="text-2xl mt-8">Comments</h3>
            <div className="mt-4 space-y-4">
              {product.comments.length > 0 ? (
                product.comments.map((comment) => (
                  <div key={comment.id} className="flex items-center space-x-4">
                    <Image
                      src={`http://localhost:8080/product-service/products/images/${comment.pic}`}
                      alt={comment.email}
                      width={50}
                      height={50}
                      className="rounded-full"
                    />
                    <div>
                      <p className="text-lg">{comment.comment}</p>
                      <p className="text-sm text-gray-500">{comment.email}</p>
                    </div>
                  </div>
                ))
              ) : (
                <p>No comments available.</p>
              )}
            </div>

            <form onSubmit={handleCommentSubmit} className="mt-6">
              <textarea
                value={newComment}
                onChange={(e) => setNewComment(e.target.value)}
                className="w-full p-3 border border-gray-300 rounded-lg"
                placeholder="Add a comment..."
              />
              <button
                type="submit"
                className="mt-4 bg-blue-500 text-white px-4 py-2 rounded-lg"
              >
                Submit Comment
              </button>
              {commentSuccess && (
                <p className="text-green-500 mt-2">Comment posted successfully!</p>
              )}
            </form>
          </>
        )}
      </div>
    </div>
  );
};

export default ProductPage;
