import React from 'react';
import ReactDOM from 'react-dom/client'; // 'react-dom/client'에서 import
import App from './App';
import { Chart, registerables } from 'chart.js';

// Chart.js 구성 요소 등록
Chart.register(...registerables);

// createRoot 사용
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
        <App />
);
