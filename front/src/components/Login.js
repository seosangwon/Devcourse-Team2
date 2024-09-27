import React, { useState } from 'react';
import axios from 'axios';

function Login({ onLogin }) {
    const [loginId, setLoginId] = useState('');
    const [pw, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();
        setErrorMessage('');

        const dataToSend = { loginId, pw };

        try {
            const response = await axios.post('/api/v1/members/login', dataToSend);
            const { accessToken, name } = response.data;

            localStorage.setItem('token', accessToken); // JWT 저장
            onLogin(name); // 로그인 후 사용자 이름 전달
        } catch (error) {
            setErrorMessage('로그인 실패: ' + (error.response?.data?.message || '서버에 문제가 발생했습니다.'));
        }
    };

    return (
        <form onSubmit={handleLogin}>
            <div>
                <label>로그인 ID:</label>
                <input type="text" value={loginId} onChange={(e) => setLoginId(e.target.value)} required />
            </div>
            <div>
                <label>비밀번호:</label>
                <input type="password" value={pw} onChange={(e) => setPassword(e.target.value)} required />
            </div>
            {errorMessage && <div style={{ color: 'red' }}>{errorMessage}</div>}
            <button type="submit">로그인</button>
        </form>
    );
}

export default Login;
