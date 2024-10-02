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
            const response = await axiosInstance.post('/api/v1/orders', dataToSend);
            alert('주문 등록 성공');
        } catch (error) {
            alert('주문 등록 실패');
        }
    };

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

    const calculateTotalPrice = () => {
        const total = items.reduce((acc, item) => acc + item.price * item.quantity, 0);
        setTotalPrice(total);
    };

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

    const addItem = () => {
        setItems([...items, { productId: '', quantity: '', price: '' }]);
    };

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
                    />
                    <input
                        type="number"
                        value={item.price || ''}
                        onChange={(e) => handleItemChange(index, 'price', Number(e.target.value))}
                        placeholder="가격을 입력하세요"
                        required
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
