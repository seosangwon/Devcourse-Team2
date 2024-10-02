import React, { useState } from 'react';

function Register({ onRegister }) {
    const [loginId, setLoginId] = useState('');
    const [pw, setPassword] = useState('');
    const [confirmPw, setConfirmPassword] = useState(''); // 비밀번호 확인 상태 추가
    const [name, setName] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [loading, setLoading] = useState(false); // 로딩 상태 추가

    const handleRegister = async (e) => {
        e.preventDefault();
        setErrorMessage(''); // 이전 메시지 초기화
        setSuccessMessage(''); // 이전 메시지 초기화
        setLoading(true); // 로딩 시작

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

            if (response.status !== 200) {
                const errorData = await response.json();
                throw new Error(errorData.message || '회원가입 실패');
            }

            const responseData = await response.json();
            console.log('회원가입 성공:', responseData);
            const { id, name, accessToken, loginId } = responseData;
            localStorage.setItem('accessToken', accessToken);
            localStorage.setItem('id', id);
            localStorage.setItem('LoginId', loginId);

            setSuccessMessage(`환영합니다, ${name}!`);
            // 입력 필드 초기화
            setLoginId('');
            setPassword('');
            setConfirmPassword('');
            setName('');

        } catch (error) {
            setErrorMessage('회원가입 실패: ' + error.message);
        } finally {
            setLoading(false); // 로딩 종료
        }
    };

    return (
        <form onSubmit={handleRegister}>
            <div className="inputform">
                <div>
                    <label htmlFor="name">이름:</label>
                    <input
                        type="text"
                        id="name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        placeholder="이름을 입력하세요"
                        required
                    />
                </div>

                <div>
                    <label>ID:</label>
                    <input
                        type="text"
                        value={loginId}
                        onChange={(e) => setLoginId(e.target.value)}
                        placeholder="ID를 입력하세요"
                        required
                    />
                </div>

                <div>
                    <label>비밀번호:</label>
                    <input
                        type="password"
                        value={pw}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="비밀번호를 입력하세요"
                        required
                    />
                </div>

                <div>
                    <label>비밀번호 확인:</label>
                    <input
                        type="password"
                        value={confirmPw}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        placeholder="비밀번호를 다시 입력하세요"
                        required
                    />
                </div>
            </div>

            {errorMessage && <div style={{ color: 'red' }}>{errorMessage}</div>}
            {successMessage && <div style={{ color: 'green' }}>{successMessage}</div>}
            <button type="submit" disabled={loading}>
                {loading ? '로딩 중...' : '회원가입'}
            </button>
        </form>
    );
}

export default Register;
