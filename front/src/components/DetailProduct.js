import React, { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import { ResponsiveLine } from '@nivo/line';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import Modal from 'react-modal'; // 추가된 부분

Modal.setAppElement('#root'); // 애플리케이션의 루트 요소를 설정합니다.

function DetailProduct({ product }) {
    const [productLoss, setProductLoss] = useState(product.loss);
    const [isUpdating, setIsUpdating] = useState(false); // 수정 모드 상태

    // 통계 관련 상태값
    const [statistics, setStatistics] = useState([]); // 통계 데이터
    const [startDate, setStartDate] = useState(new Date(new Date().setDate(new Date().getDate() - 7))); // 기본 시작일: 7일 전
    const [endDate, setEndDate] = useState(new Date()); // 기본 종료일: 오늘

    useEffect(() => {
        // 로스율 등록 시 로스율 상태 업데이트
        if (product) {
            setProductLoss(product.loss);
        }
    }, [product]); // product가 변경될 때마다 실행

    const handleUpdateProduct = async (e) => {
        e.preventDefault();
        const dataToUpdate = {
            name: product.name, // ProductList에서 받은 식품 이름 사용
            loss: productLoss ? parseInt(productLoss) : null,
        };

        const token = localStorage.getItem('accessToken');
        try {
            console.log('Update product data:', JSON.stringify(dataToUpdate));

            await axios.post(`/api/v1/products/loss`, dataToUpdate, {
                headers: { Authorization: `Bearer ${token}`, 'Content-Type': 'application/json' },
            });

            alert('로스율이 등록되었습니다!');
            product.loss = productLoss; // 입력받은 로스율로 변경
            setIsUpdating(false); // 수정 모드 종료
        } catch (error) {
            alert('로스율 등록 실패: ' + (error.response?.data?.message || '알 수 없는 오류'));
        }
    };

    // 통계 데이터 가져오는 함수 >> 매 랜더링 시 useEffect 재실행
    const fetchStatistics = useCallback(async (selectedStartDate, selectedEndDate) => {
        const token = localStorage.getItem('accessToken');
        try {
            const response = await axios.get(`/api/v1/products/loss/${product.name}`, {
                headers: { Authorization: `Bearer ${token}` },
                params: {
                    startDate: selectedStartDate.toISOString(),
                    endDate: selectedEndDate.toISOString(),
                },
            });
            const data = response.data;

            console.log('응답 데이터:', data);

            // 응답 데이터에서 차트에 필요한 형식으로 변환
            if (data.length > 0) {
                const formattedMyData = [];
                const formattedAllData = [];

                data.forEach(item => {
                    if (item.personalAverage && item.allUsersAverage) {
                        const myData = item.personalAverage.map((avg, index) => ({
                            x: item.dates[index], // 날짜
                            y: avg // 개인 평균 로스율
                        }));

                        const allData = item.allUsersAverage.map((avg, index) => ({
                            x: item.dates[index], // 날짜
                            y: avg // 전체 사용자 평균 로스율
                        }));

                        formattedMyData.push(...myData);
                        formattedAllData.push(...allData);
                    }
                });

                setStatistics([
                    { id: '내 로스율', data: formattedMyData },
                    { id: '전체 평균 로스율', data: formattedAllData },
                ]);

                console.log('Statistics:', [
                    { id: '내 로스율', data: formattedMyData },
                    { id: '전체 평균 로스율', data: formattedAllData },
                ]);
            } else {
                console.error("데이터 구조가 예상과 다릅니다:", data);
                alert('유효한 통계 데이터를 찾을 수 없습니다.');
            }
        } catch (error) {
            console.error("에러 응답 전체:", error); // 전체 에러 객체 로그
            console.error("에러 응답 데이터:", error.response); // 에러 응답 데이터만 출력
            console.error("에러 메시지:", error.message); // 에러 메시지만 출력

            alert('통계 데이터를 불러오는 데 실패했습니다: ' + (error.response?.data?.message || '알 수 없는 오류'));
        }
    }, [product.name]);

    // 통계 조회 버튼 클릭 시 처리
    const handleDateChange = () => {
        console.log('Start Date:', startDate.toISOString());
        console.log('End Date:', endDate.toISOString());
        fetchStatistics(startDate, endDate); // 날짜 변경 시 통계 데이터 재조회
    };

    useEffect(() => {
        // 날짜 변경 시 통계 데이터 가져오기
        fetchStatistics(startDate, endDate);
    }, [fetchStatistics, startDate, endDate, product.name]); // fetchStatistics와 날짜가 변경될 때마다 실행

    const handleEditClick = () => {
        setProductLoss(''); // 수정 모드로 전환 시 로스율 입력란 초기화
        setIsUpdating(true); // 수정 모드 시작
    };

    if (!product) return null; // 선택된 상품이 없으면 null 반환

    const updatedLoss = product.loss === null || product.loss === 222 ? "등록된 로스율이 없습니다." : product.loss;

//    const hardcodedData = [
//        {
//            id: '내 로스율',
//            data: [
//                { x: '2024-10-04', y: 31.6 },
//                { x: '2024-10-05', y: 4 }
//            ]
//        },
//        {
//            id: '전체 평균 로스율',
//            data: [
//                { x: '2024-10-04', y: 31.6 },
//                { x: '2024-10-05', y: 4 }
//            ]
//        }
//    ];

     return (
        <div>
            <h2>상품 상세 정보</h2>
            <h3>{product.name}</h3>
            <p>로스율: {updatedLoss}</p>

            {/* 모달 창 */}
            <Modal
                isOpen={isUpdating}
                onRequestClose={() => setIsUpdating(false)}
                style={{
                    content: {
                        top: '50%',
                        left: '50%',
                        right: 'auto',
                        bottom: 'auto',
                        transform: 'translate(-50%, -50%)',
                        width: '600px', // 너비를 500px로 변경
                        height: '300px', // 높이를 조정합니다.
                        padding: '30px', // 여백을 늘림
                        border: '1px solid #ccc', // 테두리 추가
                        borderRadius: '8px', // 모서리 둥글게
                    },
                }}
            >
                <h2>오늘의 로스율</h2>
                <form onSubmit={handleUpdateProduct}>
                    <div>
                        <label>로스율:</label>
                        <input
                            type="number"
                            value={productLoss}
                            onChange={(e) => setProductLoss(e.target.value)}
                            placeholder="로스율을 입력하세요"
                        />
                    </div>
                    <button type="submit" className="button">등록하기</button>
                    <button type="button" onClick={() => setIsUpdating(false)}>취소</button>
                </form>
            </Modal>

            <button onClick={handleEditClick}>로스율 등록</button>

            {/* 통계 기능 추가 */}
            <div style={{ marginTop: '20px' }}>
                <h3>로스율 통계</h3>
                <div style={{ display: 'flex', alignItems: 'center', marginBottom: '20px' }}>
                    <div style={{ marginRight: '10px' }}>
                        <label>시작 날짜:</label>
                        <DatePicker
                            selected={startDate}
                            onChange={date => setStartDate(date)}
                            dateFormat="yyyy-MM-dd"
                        />
                    </div>
                    <div style={{ marginRight: '10px' }}>
                        <label>종료 날짜:</label>
                        <DatePicker
                            selected={endDate}
                            onChange={date => setEndDate(date)}
                            dateFormat="yyyy-MM-dd"
                        />
                    </div>
                    <button onClick={handleDateChange}>통계 조회</button>
                </div>
            <div style={{ height: '400px', width: '600px' }}>
                <ResponsiveLine
                    data={statistics} // 통계 데이터를 전달
                    margin={{ top: 10, right: 30, bottom: 50, left: 60 }}
                    xScale={{ type: 'point' }}
                    yScale={{ type: 'linear', min: 'auto', max: 'auto', stacked: false, reverse: false }}
                    axisBottom={{
                        orient: 'bottom',
                        legend: '날짜',
                        legendOffset: 36,
                        legendPosition: 'middle',
                        format: (value) => new Date(value).toLocaleDateString(), // 날짜 포맷
                    }}
                    axisLeft={{
                        orient: 'left',
                        legend: '로스율',
                        legendOffset: -40,
                        legendPosition: 'middle',
                    }}
                    enablePoints={false}
                    useMesh={true}
                    enableGridX={false}
                    enableGridY={true}
                    pointSize={10}
                    pointColor={{ theme: 'background' }}
                    pointBorderWidth={2}
                    pointBorderColor={{ from: 'serieColor' }}
                    enableArea={true}
                    areaOpacity={0.1}
                    areaBlendMode="multiply"
                    colors={{ scheme: 'nivo' }}
                />
                {console.log('Statistics data:', statistics)}
                </div>
            </div>
        </div>
    );
}

export default DetailProduct;
