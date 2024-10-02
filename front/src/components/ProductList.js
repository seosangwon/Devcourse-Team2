import React, { useEffect, useState } from 'react';
import axios from 'axios';

function ProductList() {
    const [products, setProducts] = useState([]); // 제품 목록
    const [currentPage, setCurrentPage] = useState(0); // 현재 페이지
    const [totalPages, setTotalPages] = useState(0); // 전체 페이지 수
    const pageSize = 10; // 페이지당 제품 수

    // 제품 목록을 가져오는 함수
    const fetchProducts = async (page) => {
        const token = localStorage.getItem('accessToken');
        try {
            const response = await axios.get(`/api/v1/products`, {
                headers: { Authorization: `Bearer ${token}` },
                params: { page, size: pageSize }, // 페이지와 페이지 크기 전달
            });

            console.log('API Response:', response.data); // 응답을 콘솔에 출력

            setProducts(response.data.content); // 실제 제품 목록
            setTotalPages(response.data.totalPages); // 전체 페이지 수
        } catch (error) {
            alert('제품 목록을 불러오는 데 실패했습니다: ' + (error.response?.data?.message || '알 수 없는 오류'));
        }
    };

    // 컴포넌트가 마운트될 때 제품 목록을 가져옴
    useEffect(() => {
        fetchProducts(currentPage);
    }, [currentPage]); // currentPage가 바뀔 때마다 호출

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
                    {products.map((product, index) => (
                        <tr key={index}>
                            <td style={{ padding: '8px', border: '1px solid #ccc' }}>{product.name}</td>
                            <td style={{ padding: '8px', border: '1px solid #ccc' }}>{product.loss}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <div style={{ marginTop: '16px' }}>
                <button
                    onClick={() => setCurrentPage(Math.max(0, currentPage - 1))}
                    disabled={currentPage === 0}
                >
                    이전
                </button>
                <span> 페이지 {currentPage + 1} / {totalPages} </span>
                <button
                    onClick={() => setCurrentPage(Math.min(totalPages - 1, currentPage + 1))}
                    disabled={currentPage === totalPages - 1}
                >
                    다음
                </button>
            </div>
        </div>
    );
}

export default ProductList;
