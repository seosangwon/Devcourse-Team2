import React, { useEffect, useState } from 'react';
import axiosInstance from "../axiosInstance";

function LossControl() {
    const [products, setProducts] = useState([]); // 제품 목록
    const [currentPage, setCurrentPage] = useState(0); // 현재 페이지
    const [totalPages, setTotalPages] = useState(0); // 전체 페이지 수
    const pageSize = 10; // 페이지당 제품 수
    const [searchTerm, setSearchTerm] = useState(''); // 검색어 상태

    // 제품 목록을 가져오는 함수
    const fetchProducts = async (page) => {
        try {
            const response = await axiosInstance.get(`/api/adm/products/all`, {
                params: { page, size: pageSize },
            });

            const updatedProducts = response.data.content.map(product => {
                const updatedLoss = product.loss === null || product.loss === 222 ? "등록된 로스율이 없습니다." : product.loss;
                return { ...product, loss: updatedLoss };
            });

            setProducts(updatedProducts);
            setTotalPages(response.data.totalPages);
        } catch (error) {
            alert('제품 목록을 불러오는 데 실패했습니다: ' + (error.response?.data?.message || '알 수 없는 오류'));
        }
    };

    useEffect(() => {
        fetchProducts(currentPage);
    }, [currentPage]);

    // 검색어가 변경될 때 호출되는 함수
    const handleSearch = (e) => {
        setSearchTerm(e.target.value);
        setCurrentPage(0);
    };

    // 검색된 제품을 필터링하는 함수
    const filteredProducts = products.filter(product =>
        product.name.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div>
            <h2>상품 목록</h2>
            <input
                type="text"
                value={searchTerm}
                onChange={handleSearch}
                placeholder="조회할 상품명을 입력하세요"
                style={{ marginBottom: '16px', padding: '8px', width: '300px' }}
            />
            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                    <tr>
                        <th style={{ width: '25%', padding: '8px', textAlign: 'left' }}>상품 목록</th>
                        <th style={{ width: '25%', padding: '8px', textAlign: 'left' }}>로스율</th>
                    </tr>
                </thead>
                <tbody>
                    {filteredProducts.length > 0 ? (
                        filteredProducts.map((product, index) => (
                            <tr key={index}>
                                <td style={{ padding: '8px', border: '1px solid #ccc' }}>
                                    {product.name}
                                </td>
                                <td style={{ padding: '8px', border: '1px solid #ccc' }}>{product.loss}</td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="2" style={{ textAlign: 'center', padding: '8px' }}>등록된 상품이 없습니다</td>
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

export default LossControl;
