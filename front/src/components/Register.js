import React, { useState } from 'react';

function Register({ onRegister }) {
    const [loginId, setLoginId] = useState('');
    const [pw, setPassword] = useState('');
    const [name, setName] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    const handleRegister = async (e) => {
        e.preventDefault();
        const dataToSend = {
            loginId,
            pw,
            name,
        };

        try {
            const response = await fetch('/api/v1/members/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(dataToSend),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || '회원가입 실패');
            }

            const responseData = await response.json(); // JSON 응답을 파싱

            // 성공적으로 응답을 받았을 때 처리
            console.log('회원가입 성공:', responseData);
            const { id, name, accessToken, loginId } = responseData;

            // 토큰을 로컬 스토리지에 저장
            localStorage.setItem('accessToken', accessToken);
            localStorage.setItem('id', id);
            localStorage.setItem('LoginId', loginId);

            // 사용자의 이름이나 ID를 상태에 저장하거나 다른 작업 수행
            setSuccessMessage(`환영합니다, ${name}!`);

        } catch (error) {
            setErrorMessage('회원가입 실패: ' + error.message);
        }
    };


    return (
        <form onSubmit={handleRegister}>
            <div>
                <label>이름:</label>
                <input type="text" value={name} onChange={(e) => setName(e.target.value)} required />
            </div>
            <div>
                <label>로그인 ID:</label>
                <input type="text" value={loginId} onChange={(e) => setLoginId(e.target.value)} required />
            </div>
            <div>
                <label>비밀번호:</label>
                <input type="password" value={pw} onChange={(e) => setPassword(e.target.value)} required />
            </div>
            {errorMessage && <div style={{ color: 'red' }}>{errorMessage}</div>}
            {successMessage && <div style={{ color: 'green' }}>{successMessage}</div>}
            <button type="submit">회원가입</button>
        </form>
    );
}

export default Register;
