import React, { useState, useEffect } from 'react';
import axios from 'axios';

function InsertOrder({ memberId }) {
    const [items, setItems] = useState([]);
    const [products, setProducts] = useState([]);
    const [totalPrice, setTotalPrice] = useState('');

    // 주문 등록 핸들러
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

        const token = localStorage.getItem('token');
        try {
            console.log('Sending data:', JSON.stringify(dataToSend, null, 2));

            const response = await axios.post('/api/v1/orders', dataToSend, {
                headers: { Authorization: `Bearer ${token}` },
            });
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
            const selectedProduct = products.find(product => product.id === Number(value));
            if (selectedProduct) {
                newItems[index] = {
                    productId: selectedProduct.id,
                    quantity: newItems[index]?.quantity,
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
        <form onSubmit={handleRegister} className="insert-order-container">
            {items.map((item, index) => (
                <div key={index} style={{ marginBottom: '15px' }}>
                    <select
                        value={item.productId}
                        onChange={(e) => handleItemChange(index, 'productId', e.target.value)}
                        style={{ display: 'block' }}
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
                        value={item.quantity || ''}
                        onChange={(e) => handleItemChange(index, 'quantity', Number(e.target.value))}
                        placeholder="개수를 입력하세요"
                        required
                        style={{ display: 'block', marginBottom: '10px' }}
                    />
                    <input
                        type="number"
                        value={item.price || ''}
                        onChange={(e) => handleItemChange(index, 'price', Number(e.target.value))}
                        placeholder="가격을 입력하세요"
                        required
                        style={{ display: 'block', marginBottom: '10px' }}
                    />
                    <div>상품 이름: {item.productName || 'N/A'}</div>
                    <button type="button" onClick={() => removeItem(index)} style={{ marginTop: '10px' }}>Remove</button>
                </div>
            ))}
            <button type="button" onClick={addItem} style={{ marginTop: '20px' }}>Add Item</button>
            <h3>Total Price: {totalPrice}</h3>
            <button type="submit" style={{ marginTop: '20px' }}>Register Order</button>
        </form>
    );
}

export default InsertOrder;
