import React, { useState } from 'react';

const OrderTable = ({ orders, products }) => {
    const [openOrderId, setOpenOrderId] = useState(null);

    const getProductNameById = (productId) => {
        const product = products.find(product => product.id === productId);
        return product ? product.name : 'N/A';
    };

    const toggleOrderDetails = (orderId) => {
        setOpenOrderId(openOrderId === orderId ? null : orderId);
    };

    const formatDate = (date) => new Date(date).toLocaleString();

    return (
        <table style={tableStyle}>
            <thead>
            <tr>
                <th>상세보기</th>
                <th>주문 ID</th>
                <th>총 가격</th>
                <th>생성일</th>
                <th>수정일</th>
            </tr>
            </thead>
            <tbody>
            {orders.length === 0 ? (
                <tr>
                    <td colSpan="5">주문 항목이 없습니다.</td>
                </tr>
            ) : (
                orders.map(order => (
                    <React.Fragment key={order.id}>
                        <tr onClick={() => toggleOrderDetails(order.id)} style={{cursor: 'pointer'}}>
                            <td>
                                <button style={detailButtonStyle}>상세보기</button>
                            </td>
                            <td>{order.id}</td>
                            <td>{order.totalPrice}</td>
                            <td>{formatDate(order.createdAt)}</td>
                            <td>{formatDate(order.modifiedAt)}</td>
                        </tr>
                        {openOrderId === order.id && order.orderItems && order.orderItems.length > 0 && (
                            <tr>
                                <td colSpan="5">
                                    <table style={nestedTableStyle}>
                                        <thead>
                                        <tr>
                                            <th style={nestedTableHeaderStyle}>상품 이름</th>
                                            <th style={nestedTableHeaderStyle}>수량</th>
                                            <th style={nestedTableHeaderStyle}>가격</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        {order.orderItems.map(item => (
                                            <tr key={item.productId}>
                                                <td style={nestedTableCellStyle}>{getProductNameById(item.productId)}</td>
                                                <td style={nestedTableCellStyle}>{item.quantity}</td>
                                                <td style={nestedTableCellStyle}>{item.price}</td>
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
    backgroundColor: '#f9f9f9',
    border: '1px solid #ddd',
};
const nestedTableHeaderStyle = {
    backgroundColor: '#e0e0e0', // 헤더 배경색
    fontWeight: 'bold',
};
const nestedTableCellStyle = {
    padding: '8px', // 패딩 추가
    border: '1px solid #ddd', // 셀 테두리 추가
};

const detailButtonStyle = {
    backgroundColor: '#5680b5',
    color: '#fff',
    border: 'none',
    borderRadius: '4px',
    padding: '5px 10px',
    cursor: 'pointer',
    margin: '2px',
};

export default OrderTable;
