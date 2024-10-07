import React, { useRef } from "react";
import { Line } from 'react-chartjs-2';

const ChartComponent = ({ data }) => {
    const chartRef = useRef(null);

    const chartData = {
        labels: data.map(item => item.totalPrice),
        datasets: [
            {
                label: 'Total Price',
                data: data.map(item => item.totalPrice),
                borderColor: '#8884d8',
                backgroundColor: 'rgba(136, 132, 216, 0.2)',
                fill: true,
            },
        ],
    };

    const chartOptions = {
        responsive: true,
        plugins: {
            legend: {
                position: 'top',
            },
        },
        scales: {
            x: {
                title: {
                    display: true,
                    text: '월별 주문',
                },
            },
            y: {
                title: {
                    display: true,
                    text: '총 수량',
                },
                beginAtZero: true,
            },
        },
    };

    return <Line ref={chartRef} data={chartData} options={chartOptions} />;
};

export default ChartComponent;
