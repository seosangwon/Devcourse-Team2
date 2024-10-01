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
            const { id, accessToken, name, mimage } = response.data; // mImage 추가

            localStorage.setItem('token', accessToken); // JWT 저장
            localStorage.setItem('id', id); // JWT 저장
            onLogin(name, mimage); // 로그인 후 사용자 이름과 이미지 전달
        } catch (error) {
            setErrorMessage('로그인 실패: ' + (error.response?.data?.message || '서버에 문제가 발생했습니다.'));
        }
    };

    return (
        <form onSubmit={handleLogin}>
            <div className="inputform">
            <div>
                <label>ID:</label>
                <input type="text" value={loginId} onChange={(e) => setLoginId(e.target.value)} placeholder="ID를 입력하세요" required />
            </div>
            <div>
                <label>비밀번호:</label>
                <input type="password" value={pw} onChange={(e) => setPassword(e.target.value)} placeholder="비밀번호를 입력하세요" required />
            </div>
            </div>
            {errorMessage && <div style={{ color: 'red' }}>{errorMessage}</div>}
            <button type="submit">로그인</button>
        </form>
    );
}

export default Login;
