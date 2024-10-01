import React, { useState, useEffect } from 'react';
import axios from 'axios';

function InsertOrder({ memberId }) {
    const [items, setItems] = useState([]);
    const [products, setProducts] = useState([]);
    const [totalPrice, setTotalPrice] = useState(0);

    // 주문 등록 핸들러
    const handleRegister = async (e) => {
        e.preventDefault();
        const dataToSend = {
            items: items.map(item => ({
                productId: item.productId,  // 여기서는 ID를 사용합니다.
                quantity: item.quantity,
                price: item.price,
            })),
            memberId,
            totalPrice,
        };

        const token = localStorage.getItem('token');
        try {
            console.log('Sending data:', JSON.stringify(dataToSend, null, 2));

            const response = await axios.post('/api/v1/orders', dataToSend, {
                headers: { Authorization: `Bearer ${token}` },
            });
            console.log('주문 등록 성공:', response.data);
        } catch (error) {
            console.error('주문 등록 실패:', error.response ? error.response.data : error.message);
        }
    };

    // 상품 목록 가져오기
    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await axios.get('/api/v1/products');
                setProducts(response.data.content);
            } catch (error) {
                console.error('상품 목록 조회 실패:', error);
            }
        };

        fetchProducts();
    }, []);

    // 총 가격 계산
    const calculateTotalPrice = () => {
        const total = items.reduce((acc, item) => acc + item.price * item.quantity, 0);
        setTotalPrice(total);
    };

    // 아이템 변경 시
    const handleItemChange = (index, field, value) => {
        const newItems = [...items];

        if (field === 'productId') {
            const selectedProduct = products.find(product => product.id === value);
            newItems[index] = {
                productId: value,
                quantity: newItems[index]?.quantity || 1,
                price: selectedProduct ? selectedProduct.price : 0,
            };
        } else {
            newItems[index][field] = value;
        }

        setItems(newItems);
        calculateTotalPrice(); // 총 가격 재계산
    };

    // 항목 추가
    const addItem = () => {
        setItems([...items, { productId: '', quantity: 1, price: 0 }]);
    };

    // 항목 제거
    const removeItem = (index) => {
        const newItems = items.filter((_, i) => i !== index);
        setItems(newItems);
        calculateTotalPrice(); // 총 가격 재계산
    };

    return (
        <form onSubmit={handleRegister}>
            {items.map((item, index) => (
                <div key={index}>
                    <select
                        value={item.productId}
                        onChange={(e) => handleItemChange(index, 'productId', e.target.value)}
                    >
                        <option value="">Select a product</option>
                        {products.map((product) => (
                            <option key={product.id} value={product.id}>
                                {product.name}
                            </option>
                        ))}
                    </select>
                    <input
                        type="number"
                        value={item.quantity || 1}
                        onChange={(e) => handleItemChange(index, 'quantity', Number(e.target.value))}
                        placeholder="개수를 입력하세요"
                        required
                    />
                    <input
                        type="number"
                        value={item.price}
                        readOnly // 가격은 자동으로 설정되므로 읽기 전용으로 설정
                    />
                    <button type="button" onClick={() => removeItem(index)}>Remove</button>
                </div>
            ))}
            <button type="button" onClick={addItem}>Add Item</button>
            <h3>Total Price: {totalPrice}</h3>
            <button type="submit">Register Order</button>
        </form>
    );
}

export default InsertOrder;
