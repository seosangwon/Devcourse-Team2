import React, { useEffect, useState } from 'react';
import axios from 'axios';
import DetailProduct from './DetailProduct';

function ProductList() {
    const [products, setProducts] = useState([]); // 제품 목록
    const [currentPage, setCurrentPage] = useState(0); // 현재 페이지
    const [totalPages, setTotalPages] = useState(0); // 전체 페이지 수
    const pageSize = 10; // 페이지당 제품 수
    const [searchTerm, setSearchTerm] = useState(''); // 검색어 상태
    const [selectedProduct, setSelectedProduct] = useState(null); // 선택된 상품

    // 관리자용


    // 사용자용 제품 목록을 가져오는 함수
    const fetchProducts = async (page) => {
        const token = localStorage.getItem('accessToken');
        try {
//            const response = await axios.get(`/api/adm/products/all`, {
            const response = await axios.get(`/api/v1/products`, {
                headers: { Authorization: `Bearer ${token}` },
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

    // 상품 이름을 클릭했을 때 실행되는 함수
    const handleProductClick = (product) => {
        setSelectedProduct(product); // 선택된 상품을 상태에 저장
    };

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
            {selectedProduct ? (
                <DetailProduct
                    product={selectedProduct}
                    onBack={() => setSelectedProduct(null)} // 뒤로가기 버튼 클릭 시 상품 선택 해제
                />
            ) : (
                <>
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
                                        <td
                                            style={{ padding: '8px', border: '1px solid #ccc', cursor: 'pointer', color: 'blue' }}
                                            onClick={() => handleProductClick(product)} // 상품 이름 클릭 시 해당 상품을 전달
                                        >
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
                </>
            )}
        </div>
    );
}

export default ProductList;