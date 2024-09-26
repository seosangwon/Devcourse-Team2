// src/App.js

import React from 'react';
import './App.css';

function App() {
    return (
        <div className="App">
            <h1>발주 관리 통합 솔루션</h1>
            <div className="container">
                <button className="box color1">발주 관리</button>
                <button className="box color2">상품 관리</button>
                <button className="box color3">정보 관리</button>
                <button className="box color4">추가 기능</button>
            </div>
        </div>
    );
}

export default App;
