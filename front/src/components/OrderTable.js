import React from 'react';
//주문목록 수정용
const OrderTable = ({ orders }) => {
    return (
        <table style={tableStyle}>
            <thead>
            <tr>
                <th>주문 ID</th>
                <th>회원 ID</th>
                <th>총 가격</th>
                <th>생성일</th>
                <th>수정일</th>
                <th>상세보기</th>
            </tr>
            </thead>
            <tbody>
            {orders.length === 0 ? (
                <tr>
                    <td colSpan="6">주문 항목이 없습니다.</td>
                </tr>
            ) : (
                orders.map(order => (
                    <tr key={order.id}>
                        <td>{order.id}</td>
                        <td>{order.memberId}</td>
                        <td>{order.totalPrice}</td>
                        <td>{new Date(order.createdAt).toLocaleString()}</td>
                        <td>{new Date(order.modifiedAt).toLocaleString()}</td>
                        <td>
                            <button style={detailButtonStyle}>상세보기</button>
                        </td>
                    </tr>
                ))
            )}
            </tbody>
        </table>
    );
};

const tableStyle = {
    width: '100%',
    borderCollapse: 'collapse',
    marginTop: '20px'
};

const detailButtonStyle = {
    backgroundColor: '#28a745',
    color: '#fff',
    border: 'none',
    borderRadius: '4px',
    padding: '5px 10px',
    cursor: 'pointer'
};

export default OrderTable;
