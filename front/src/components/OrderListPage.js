import React, { useEffect, useState } from 'react';
import axios from 'axios';

function OrderListPage() {
    const [orders, setOrders] = useState([]);  // 전체 주문 리스트 상태

    useEffect(() => {
        const fetchOrderList = async () => {
            const token = localStorage.getItem('token'); // 로컬 스토리지에서 토큰 값 가져오기

            try {
                const response = await axios.get('/api/v1/orders/list', {
                    headers: {
                        Authorization: `Bearer ${token}`, // Authorization 헤더에 토큰 추가
                    },
                });
                console.log('주문 목록 데이터:', response.data); // 전체 응답 데이터 확인
                setOrders(response.data.content); // 페이지네이션이 있다면 .content 사용
            } catch (error) {
                console.error('주문 목록 조회 실패:', error);
            }
        };

        fetchOrderList();
    }, []);

    if (orders.length === 0) {
        return <div>주문 항목이 없습니다.</div>;
    }

    return (
        <div>
            <h2>주문 목록</h2>
            <table>
                <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Member ID</th>
                    <th>Total Price</th>
                    <th>Created At</th>
                    <th>Modified At</th>
                </tr>
                </thead>
                <tbody>
                {orders.map(order => (
                    <React.Fragment key={order.id}>
                        <tr>
                            <td>{order.id}</td>
                            <td>{order.memberId}</td>
                            <td>{order.totalPrice}</td>
                            <td>{new Date(order.createdAt).toLocaleString()}</td>
                            <td>{new Date(order.modifiedAt).toLocaleString()}</td>
                        </tr>
                        {/* 주문 항목 (orderItems) 출력 */}
                        {order.orderItems && order.orderItems.length > 0 ? (
                            <tr>
                                <td colSpan="5">
                                    <table style={{ width: '100%', marginTop: '10px' }}>
                                        <thead>
                                        <tr>
                                            <th>OrderItem ID</th>
                                            <th>Product Name</th>
                                            <th>Quantity</th>
                                            <th>Price</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        {order.orderItems.map(item => (
                                            <tr key={item.productId}>
                                                <td>{item.productId}</td> {/* productId 출력 */}
                                                <td>{item.productId}</td> {/* productId 사용 */}
                                                <td>{item.quantity}</td> {/* 수량 출력 */}
                                                <td>{item.price}</td> {/* 가격 출력 */}
                                            </tr>
                                        ))}
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                        ) : (
                            <tr>
                                <td colSpan="5">주문 항목이 없습니다.</td>
                            </tr>
                        )}
                    </React.Fragment>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default OrderListPage;