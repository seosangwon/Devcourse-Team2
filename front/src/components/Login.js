// src/components/Login.js

import React, { useState } from 'react';

function Login({ onLogin }) {
    const [loginId, setLoginId] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = (e) => {
        e.preventDefault();
        // 실제 로그인 로직 구현 (예: API 호출)
        // 여기서는 단순히 예시로 name을 하드코딩합니다.
        const name = "Tester"; // API 호출 후 받은 이름으로 변경
        onLogin(name); // 로그인 성공 시 이름 전달
    };

    return (
        <form onSubmit={handleLogin}>
            <div>
                <label>로그인 ID:</label>
                <input
                    type="text"
                    value={loginId}
                    onChange={(e) => setLoginId(e.target.value)}
                />
            </div>
            <div>
                <label>비밀번호:</label>
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
            </div>
            <button type="submit">로그인</button>
        </form>
    );
}

export default Login;
