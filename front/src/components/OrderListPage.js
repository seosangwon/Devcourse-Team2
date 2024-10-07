import React, { useEffect, useState } from 'react';
import OrderTable from './OrderTable';
import axiosInstance from "../axiosInstance";

const OrderListPage = () => {
    const [orders, setOrders] = useState([]);
    const [products, setProducts] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0); // 총 페이지 수

    const ordersPerPage = 10; // 페이지당 주문 수

    useEffect(() => {
        const fetchOrderList = async (page = 1) => {
            try {
                const response = await axiosInstance.get(`/api/v1/orders/list?page=${page}&size=${ordersPerPage}`);
                setOrders(response.data.content);
                setTotalPages(response.data.totalPages); // 총 페이지 수
            } catch (error) {
                console.error('주문 목록 조회 실패:', error);
            }
        };

        const fetchProducts = async () => {
            try {
                const response = await axiosInstance.get('/api/v1/products');
                setProducts(response.data.content);
            } catch (error) {
                console.error('상품 목록 조회 실패:', error);
            }
        };

        fetchOrderList(currentPage);
        fetchProducts();
    }, [currentPage]);

    return (
        <div className="table-container">
            <h2>주문 목록</h2>
            <OrderTable orders={orders} products={products} />

            {/* 페이지네이션 */}
            <div className="pagination">
                {Array.from({ length: totalPages }, (_, index) => (
                    <button
                        key={index + 1}
                        onClick={() => setCurrentPage(index + 1)}
                        style={{
                            margin: '0 5px',
                            backgroundColor: currentPage === index + 1 ? '#5680b5' : '#fff',
                            color: currentPage === index + 1 ? '#fff' : '#000',
                        }}
                    >
                        {index + 1}
                    </button>
                ))}
            </div>
        </div>
    );
};

export default OrderListPage;
