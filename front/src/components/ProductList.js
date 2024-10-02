import React, { useEffect, useState } from 'react';
import axios from 'axios';

function ProductList() {
    const [products, setProducts] = useState([]); // 제품 목록
    const [currentPage, setCurrentPage] = useState(0); // 현재 페이지
    const [totalPages, setTotalPages] = useState(0); // 전체 페이지 수
    const pageSize = 10; // 페이지당 제품 수

    // 제품 목록을 가져오는 함수
    const fetchProducts = async (page) => {
        console.log(`Fetching products from: /api/v1/products?page=${page}&size=${pageSize}`);
        const token = localStorage.getItem('accessToken');
        try {
            const response = await axios.get(`/api/v1/products`, {
                headers: { Authorization: `Bearer ${token}` },
                params: { page, size: pageSize }, // page와 size를 params로 전달
            });

            console.log('Requested Page:', page); // 요청한 페이지 로그
            console.log('API Response:', response.data); // 응답 로그

            setProducts(response.data.content); // 실제 제품 목록
            setTotalPages(response.data.totalPages); // 전체 페이지 수
        } catch (error) {
            alert('제품 목록을 불러오는 데 실패했습니다: ' + (error.response?.data?.message || '알 수 없는 오류'));
        }
    };

    // currentPage가 변경될 때마다 fetchProducts 호출
    useEffect(() => {
        console.log(`Fetching products for page: ${currentPage}`); // 현재 페이지 로그
        fetchProducts(currentPage); // fetchProducts 함수 호출
    }, [currentPage]); // currentPage가 바뀔 때마다 실행


    return (
        <div>
            <h2>식재료 목록</h2>
            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                    <tr>
                        <th style={{ width: '25%', padding: '8px', textAlign: 'left' }}>식재료 이름</th>
                        <th style={{ width: '25%', padding: '8px', textAlign: 'left' }}>로스율</th>
                    </tr>
                </thead>
                <tbody>
                    {products.length > 0 ? (
                        products.map((product, index) => (
                            <tr key={index}>
                                <td style={{ padding: '8px', border: '1px solid #ccc' }}>{product.name}</td>
                                <td style={{ padding: '8px', border: '1px solid #ccc' }}>{product.loss}</td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="2" style={{ textAlign: 'center', padding: '8px' }}>제품이 없습니다</td>
                        </tr>
                    )}
                </tbody>
            </table>
            <div style={{ marginTop: '16px' }}>
                <button
                    onClick={() => setCurrentPage(currentPage - 1)}
                    disabled={currentPage <= 0}
                >
                    이전
                </button>
                <span> 페이지 {currentPage + 1} / {totalPages} </span>
                <button
                    onClick={() => setCurrentPage(currentPage + 1)}
                    disabled={currentPage >= totalPages - 1}
                >
                    다음
                </button>
            </div>
        </div>
    );
}

export default ProductList;
