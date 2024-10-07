import React, { useEffect, useState } from "react";
import ChartComponent from './ChartComponent'; // ChartComponent를 별도로 분리할 수 있음
import axiosInstance from "../axiosInstance";

const OrderSummary = () => {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [month, setMonth] = useState("10")

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axiosInstance.get(`/api/v1/orders/monthly-summary`);
                setData(response.data);
            } catch (error) {
                console.error('Error fetching monthly summary:', error);
                setError('데이터를 가져오는 데 실패했습니다.');
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    if (loading) return <p>Loading...</p>;
    if (error) return <p>{error}</p>;
    if (!data || data.length === 0) return <p>데이터가 없습니다.</p>;

    return (
        <div>
            <h2>Monthly Order Price</h2>
            <ChartComponent data={data} /> {/* 데이터를 ChartComponent에 전달 */}
        </div>
    );
};

export default OrderSummary;
