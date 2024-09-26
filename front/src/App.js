import React, { useState } from 'react';
import './App.css';
import UserInfo from './components/UserInfo'; // UserInfo import
import Login from './components/Login'; // Login import

function App() {
    const [showUserInfo, setShowUserInfo] = useState(false);
    const [showSubMenu, setShowSubMenu] = useState(false);
    const [userName, setUserName] = useState(''); // 이름을 저장할 상태

    const handleLogin = (name) => {
        setUserName(name);
    };

    const handleUserInfoClick = () => {
        setShowUserInfo(true);
    };

    const handleMouseEnter = () => {
        setShowSubMenu(true);
    };

    const handleMouseLeave = () => {
        setShowSubMenu(false);
    };

    return (
        <div className="App">
            <h1>발주 관리 통합 솔루션</h1>
            <h2>{userName ? `${userName}` : '로그인 해주세요'}</h2>
            {!userName ? ( // 사용자 이름이 없으면 로그인 폼을 보여줌
                <Login onLogin={handleLogin} />
            ) : !showUserInfo ? (
                <div className="container">
                    <button className="box color1">발주 관리</button>
                    <button className="box color2">상품 관리</button>
                    <button
                        className="box color3"
                        onMouseEnter={handleMouseEnter}
                        onMouseLeave={handleMouseLeave}
                        onClick={handleUserInfoClick}
                    >
                        {showSubMenu ? (
                            <div className="submenu">
                                <ul>
                                    <li onClick={handleUserInfoClick}>▶ 회원 정보 수정</li>
                                    <li>▶ 프로필 사진 수정</li>
                                    <li>▶ 회원 탈퇴</li>
                                </ul>
                            </div>
                        ) : (
                            "정보 관리"
                        )}
                    </button>
                    <button className="box color4">추가 기능</button>
                </div>
            ) : (
                <div className="user-info-container">
                    <UserInfo onUpdate={setUserName} />
                </div>
            )}
        </div>
    );
}

export default App;
