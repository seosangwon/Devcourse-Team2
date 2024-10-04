import React, { useState, useEffect } from 'react';
import axios from 'axios';

function DetailProduct({ product }) {
    const [productLoss, setProductLoss] = useState(product.loss);
    const [isUpdating, setIsUpdating] = useState(false); // 수정 모드 상태

    useEffect(() => {
        // 로스율 등록 시 로스율 상태 업데이트
        if (product) {
            setProductLoss(product.loss);
        }
    }, [product]); // product가 변경될 때마다 실행

    const handleUpdateProduct = async (e) => {
        e.preventDefault();

        const dataToUpdate = {
            name: product.name, // ProductList에서 받은 식품 이름 사용
            loss: productLoss ? parseInt(productLoss) : null,
        };

        const token = localStorage.getItem('accessToken');
        try {
            console.log('Update product data:', JSON.stringify(dataToUpdate));

            await axios.post(`/api/v1/products/loss`, dataToUpdate, {
                headers: { Authorization: `Bearer ${token}`, 'Content-Type': 'application/json' },
            });

            alert('로스율이 등록되었습니다!');
            product.loss = productLoss; // 입력받은 로스율로 변경
            setIsUpdating(false); // 수정 모드 종료
        } catch (error) {
            alert('로스율 등록 실패: ' + (error.response?.data?.message || '알 수 없는 오류'));
        }
    };

    const handleEditClick = () => {
            setProductLoss(''); // 수정 모드로 전환 시 로스율 입력란 초기화
            setIsUpdating(true); // 수정 모드 시작
        };

    if (!product) return null; // 선택된 상품이 없으면 null 반환

    const updatedLoss = product.loss === null || product.loss === 222 ? "등록된 로스율이 없습니다." : product.loss;

    return (
        <div>
            <h2>상품 상세 정보</h2>
            <h3>{product.name}</h3>
            <p>로스율: {updatedLoss}</p>

            {isUpdating ? (
                <form onSubmit={handleUpdateProduct}>
                    <h2>로스율 등록</h2>
                    <div>
                        <label>로스율:</label>
                        <input
                            type="number"
                            value={productLoss}
                            onChange={(e) => setProductLoss(e.target.value)}
                            placeholder="로스율을 입력하세요"
                        />
                    </div>
                    <button type="submit" className="button">등록하기</button>
                    <button type="button" onClick={() => setIsUpdating(false)}>취소</button>
                </form>
            ) : (
                <button onClick={handleEditClick}>로스율 등록</button> // 등록 모드로 전환
            )}

        </div>
    );
}

export default DetailProduct;