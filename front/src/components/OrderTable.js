import React, { useState } from 'react'; // React와 useState를 불러옵니다

const OrderTable = ({ orders, products }) => {
    const [openOrderId, setOpenOrderId] = useState(null);

    const getProductNameById = (productId) => {
        const product = products.find(product => product.id === productId);
        return product ? product.name : 'N/A';
    };

    const toggleOrderDetails = (orderId) => {
        setOpenOrderId(openOrderId === orderId ? null : orderId);
    };

    return (
        <table style={tableStyle}>
            <thead>
            <tr>
                <th>주문 번호</th>
                <th>주문일</th>
                <th>총 가격</th>
                <th>상세보기</th>
            </tr>
            </thead>
            <tbody>
            {orders.length === 0 ? (
                <tr>
                    <td colSpan="5">주문 항목이 없습니다.</td>
                </tr>
            ) : (
                orders.map((order, index) => (
                    <React.Fragment key={order.id}>
                        <tr
                            onClick={() => toggleOrderDetails(order.id)}
                            style={{
                                cursor: 'pointer',
                                backgroundColor: index % 2 === 0 ? '#f9f9f9' : '#e0e0e0'  // 홀수/짝수 배경색
                            }}
                        >
                            <td>{order.id}</td>
                            <td>
                                {new Date(order.createdAt).toLocaleString('ko-KR', {
                                    year: 'numeric',
                                    month: '2-digit',
                                    day: '2-digit',
                                    hour: '2-digit',
                                    minute: '2-digit',
                                    second: undefined // 초 단위를 제거
                                })}
                            </td>

                            <td>{order.totalPrice}</td>
                            <td>
                                <button style={detailButtonStyle}>상세보기</button>
                            </td>
                        </tr>
                        {openOrderId === order.id && order.orderItems && order.orderItems.length > 0 && (
                            <tr>
                                <td colSpan="5">
                                    <table style={nestedTableStyle}>
                                        <thead>
                                        <tr>
                                            <th>상품 이름</th>
                                            <th>수량</th>
                                            <th>가격</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        {order.orderItems.map(item => (
                                            <tr key={item.productId}>
                                                <td>{getProductNameById(item.productId)}</td>
                                                <td>{item.quantity}</td>
                                                <td>{item.price}</td>
                                            </tr>
                                        ))}
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                        )}
                    </React.Fragment>
                ))
            )}
            </tbody>
        </table>
    );
};

const tableStyle = {
    width: '100%',
    borderCollapse: 'collapse',
    marginTop: '20px',
};

const nestedTableStyle = {
    width: '100%',
    borderCollapse: 'collapse',
    marginTop: '10px',
    backgroundColor: '#fff1f1', // 배경색 추가
};

const detailButtonStyle = {
    backgroundColor: '#28a745',
    color: '#fff',
    border: 'none',
    borderRadius: '4px',
    padding: '5px 10px',
    cursor: 'pointer',
};

export default OrderTable;
