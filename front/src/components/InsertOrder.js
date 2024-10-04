import React, { useState, useEffect } from 'react';
import axiosInstance from "../axiosInstance";

function InsertOrder({ memberId }) {
    const [items, setItems] = useState([]);
    const [products, setProducts] = useState([]);
    const [totalPrice, setTotalPrice] = useState('');

    const id = localStorage.getItem('id');

    const handleRegister = async (e) => {
        e.preventDefault();
        const dataToSend = {
            items: items.map(item => ({
                productId: item.productId,
                quantity: item.quantity,
                price: item.price,
            })),
            id,
            totalPrice,
        };

        try {
            console.log('Sending data:', JSON.stringify(dataToSend, null, 2));

            const response = await axiosInstance.post('/api/v1/orders', dataToSend);
            console.log('주문 등록 성공:', response.data);
            alert('주문 등록 성공');
        } catch (error) {
            console.error('주문 등록 실패:', error.response ? error.response.data : error.message);
            alert('주문 등록 실패');
        }
    };

    // 상품 목록 가져오기
    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await axiosInstance.get('/api/v1/products');
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
        const selectedProduct = products.find(product => product.id === Number(value));

        if (field === 'productId' && selectedProduct) {
            const existingItemIndex = newItems.findIndex(item => item.productId === selectedProduct.id);
            if (existingItemIndex > -1) {
                // 이미 있는 상품인 경우
                newItems[existingItemIndex].quantity += 1; // 수량 증가
            } else {
                // 새로운 상품인 경우
                newItems[index] = {
                    productId: selectedProduct.id,
                    quantity: 1,
                    price: selectedProduct.price,
                    productName: selectedProduct.name,
                };
            }
        } else {
            newItems[index][field] = value;
        }

        setItems(newItems);
        calculateTotalPrice(); // 총 가격 재계산
    };

    // 항목 추가
    const addItem = () => {
        setItems([...items, { productId: '', quantity: '', price: '' }]);
    };

    // 항목 제거
    const removeItem = (index) => {
        const newItems = items.filter((_, i) => i !== index);
        setItems(newItems);
        calculateTotalPrice(); // 총 가격 재계산
    };

    return (
        <form onSubmit={handleRegister} >
            {items.map((item, index) => (
                <div key={index}>
                    <select
                        value={item.productId}
                        onChange={(e) => handleItemChange(index, 'productId', e.target.value)} // ID 사용
                        style={{display: 'block'}}
                    >
                        <option value="">Select a product</option>
                        {products.map((product) => (
                            <option key={product.id} value={product.id}> {/* ID로 바인딩 */}
                                {product.name}
                            </option>
                        ))}
                    </select>
                    <label>개수 </label>
                    <input
                        type="number"
                        value={item.quantity || ''}
                        onChange={(e) => handleItemChange(index, 'quantity', Number(e.target.value))} // 수량 업데이트
                        placeholder="개수를 입력하세요"
                        required
                    />
                    <label>가격 </label>
                    <input
                        type="number"
                        value={item.price || ''}
                        onChange={(e) => handleItemChange(index, 'price', Number(e.target.value))} // 가격 업데이트
                        placeholder="가격을 입력하세요"
                        required
                    />
                    <div>상품 이름: {item.productName || 'N/A'}</div>
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
