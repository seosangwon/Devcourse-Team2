import { Line } from 'react-chartjs-2';
import axiosInstance from "../axiosInstance";
import { useEffect, useState } from "react";
import { Chart, registerables } from 'chart.js';

// Chart.js 구성 요소 등록
Chart.register(...registerables);

const MonthlyOrderGraph = () => {
    const [data, setData] = useState([]);

    useEffect(() => {
        axiosInstance.get('/api/orders/monthly-summary')
            .then(response => {
                setData(response.data);
            })
            .catch(error => {
                console.error('Error fetching monthly summary:', error);
            });
    }, []);

    // 차트 데이터 구성
    const chartData = {
        labels: data.map(item => item.orderMonth), // X축 레이블
        datasets: [
            {
                label: 'Total Quantity',
                data: data.map(item => item.totalQuantity), // Y축 데이터
                borderColor: '#8884d8',
                backgroundColor: 'rgba(136, 132, 216, 0.2)',
                fill: true,
            },
        ],
    };

    // 차트 옵션
    const chartOptions = {
        responsive: true,
        plugins: {
            legend: {
                position: 'top',
            },
            tooltip: {
                mode: 'index',
                intersect: false,
            },
        },
        scales: {
            x: {
                title: {
                    display: true,
                    text: 'Order Month',
                },
            },
            y: {
                title: {
                    display: true,
                    text: 'Quantity',
                },
                beginAtZero: true,
            },
        },
    };

    return (
        <div>
            <h2>Monthly Order Quantity</h2>
            <Line data={chartData} options={chartOptions} />
        </div>
    );
};

export default MonthlyOrderGraph;
