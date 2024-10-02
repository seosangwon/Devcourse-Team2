import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './Order.css';
import axiosInstance from "../axiosInstance"; // CSS 파일 임포트

function OrderListPage() {
    const [orders, setOrders] = useState([]);
    const [products, setProducts] = useState([]);

    useEffect(() => {
        const fetchOrderList = async () => {
            try {
                const response = await axiosInstance().get('/api/v1/orders/list');
                setOrders(response.data.content);
            } catch (error) {
                console.error('주문 목록 조회 실패:', error);
            }
        };

        const fetchProducts = async () => {
            try {
                const response = await axiosInstance().get('/api/v1/products', {
                });
                setProducts(response.data.content);
            } catch (error) {
                console.error('상품 목록 조회 실패:', error);
            }
        };

        fetchOrderList();
        fetchProducts();
    }, []);

    if (orders.length === 0) {
        return <div>주문 항목이 없습니다.</div>;
    }

    const getProductNameById = (productId) => {
        const product = products.find(product => product.id === productId);
        return product ? product.name : 'N/A';
    };

    return (
        <div className="table-container">
            <h2>주문 목록</h2>
            {orders.map(order => (
                <div key={order.id} className="order-table">
                    <h3>주문 ID: {order.id}</h3>
                    <table className="table">
                        <tbody>
                        <tr>
                            <td>Member ID:</td>
                            <td>{order.memberId}</td>
                        </tr>
                        <tr>
                            <td>Total Price:</td>
                            <td>{order.totalPrice}</td>
                        </tr>
                        <tr>
                            <td>Created At:</td>
                            <td>{new Date(order.createdAt).toLocaleString()}</td>
                        </tr>
                        <tr>
                            <td>Modified At:</td>
                            <td>{new Date(order.modifiedAt).toLocaleString()}</td>
                        </tr>
                        </tbody>
                    </table>
                    {order.orderItems && order.orderItems.length > 0 && (
                        <table className="sub-table">
                            <thead>
                            <tr>
                                <th>Product Name</th>
                                <th>Quantity</th>
                                <th>Price</th>
                            </tr>
                            </thead>
                            <tbody>
                            {order.orderItems.map(item => (
                                <tr key={item.productId}>
                                    <td>{getProductNameById(item.productId)}</td>
                                    <td>{item.quantity}</td>
                                    <td>{item.price}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    )}
                </div>
            ))}
        </div>
    );
}

export default OrderListPage;
