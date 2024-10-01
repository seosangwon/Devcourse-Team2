import React from 'react';

function OrderListPage({ orderItems }) {
    // orderItems의 타입을 로그에 찍어 확인
    console.log('orderItems:', orderItems);

    // orderItems가 배열인지 확인
    if (!Array.isArray(orderItems)) {
        console.error("orderItems는 배열이어야 합니다.", orderItems);
        return <div>주문 항목이 없습니다.</div>;
    }

    return (
        <div>
            <h2>주문 목록</h2>
            <table>
                <thead>
                <tr>
                    <th>OrderItem ID</th>
                    <th>Orders ID</th>
                    <th>Product Name</th>
                    <th>Quantity</th>
                    <th>Price</th>
                </tr>
                </thead>
                <tbody>
                {orderItems.map(item => (
                    <tr key={item.id}>
                        <td>{item.id || 'N/A'}</td>
                        <td>{item.orders ? item.orders.id : 'N/A'}</td>
                        <td>{item.product ? item.product.name : 'N/A'}</td>
                        <td>{item.quantity || 'N/A'}</td>
                        <td>{item.price || 'N/A'}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default OrderListPage;