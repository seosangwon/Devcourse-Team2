import React, { useState } from 'react';
import axios from 'axios';

function UpdateProduct() {
    const [productName, setProductName] = useState('');
    const [productLoss, setProductLoss] = useState('');

    const handleUpdateProduct = async (e) => {
        e.preventDefault();

        const dataToUpdate = {
            name: productName,
            loss: productLoss ? parseInt(productLoss) : null,
        };

        const token = localStorage.getItem('accessToken');
        try {
            console.log('Update product data:', JSON.stringify(dataToUpdate));

            // 여기서는 ID를 사용하지 않고, 이름과 로스율만 업데이트하는 API 호출을 가정합니다.
            await axios.put(`/api/v1/products/`, dataToUpdate, {
                headers: { Authorization: `Bearer ${token}`, 'Content-Type': 'application/json' },
            });

            alert('식재료가 수정되었습니다!');
            // 성공 후 초기화
            setProductName('');
            setProductLoss('');
        } catch (error) {
            alert('식재료 수정 실패: ' + (error.response?.data?.message || '알 수 없는 오류'));
        }
    };

    return (
        <div>
            <h2>식재료 수정</h2>
            <form onSubmit={handleUpdateProduct}>
                <div>
                    <label>식재료 이름:</label>
                    <input
                        type="text"
                        value={productName}
                        onChange={(e) => setProductName(e.target.value)}
                        placeholder="식재료 이름을 입력하세요"
                        required
                    />
                </div>
                <div>
                    <label>로스율:</label>
                    <input
                        type="number"
                        value={productLoss}
                        onChange={(e) => setProductLoss(e.target.value)}
                        placeholder="로스율을 입력하세요"
                    />
                </div>
                <button type="submit" className="button">수정하기</button>
            </form>
        </div>
    );
}

export default UpdateProduct;
