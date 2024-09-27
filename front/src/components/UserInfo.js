import React, { useEffect, useState } from 'react';
import axios from 'axios';

function UserProfile() {
    const [userInfo, setUserInfo] = useState(null);
    const [name, setName] = useState('');
    const [loginId, setLoginId] = useState('');
    const [pw, setPassword] = useState('');
    const [isEditing, setIsEditing] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        const fetchUserInfo = async () => {
            const token = localStorage.getItem('token');

            try {
                const response = await axios.get('/api/v1/members/', {
                    headers: { Authorization: `Bearer ${token}` }
                });
                setUserInfo(response.data);
                setName(response.data.name);
                setLoginId(response.data.loginId);
            } catch (error) {
                console.error('사용자 정보 조회 실패:', error);
            }
        };

        fetchUserInfo();
    }, []);

    const handleEditToggle = () => {
        setIsEditing(!isEditing);
        setErrorMessage(''); // 에러 메시지 초기화
    };

    const handleUpdate = async (e) => {
        e.preventDefault();
        const token = localStorage.getItem('token');

        try {
            const response = await axios.put('/api/v1/members/', { name, loginId, pw }, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setUserInfo(response.data);
            setIsEditing(false); // 수정 모드 종료
        } catch (error) {
            setErrorMessage('정보 수정 실패: ' + (error.response.data.message || '알 수 없는 오류'));
        }
    };

    if (!userInfo) return <div>로딩 중...</div>;

    return (
        <div>
            <h2>사용자 정보</h2>
            {isEditing ? (
                <form onSubmit={handleUpdate}>
                    <div>
                        <label>이름:</label>
                        <input
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />
                    </div>
                    <div>
                        <label>로그인 ID:</label>
                        <input
                            type="text"
                            value={loginId}
                            onChange={(e) => setLoginId(e.target.value)}
                            required
                        />
                    </div>
                    <div>
                        <label>비밀번호:</label>
                        <input
                            type="text"
                            value={pw}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    {errorMessage && <div className="error-message">{errorMessage}</div>}
                    <button type="submit" className="button">수정하기</button>
                    <button type="button" className="button" onClick={handleEditToggle}>취소</button>
                </form>
            ) : (
                <div>
                    <p>이름: {userInfo.name}</p>
                    <p>로그인 ID: {userInfo.loginId}</p>
                    <button className="button" onClick={handleEditToggle}>수정하기</button>
                </div>
            )}
        </div>
    );
}

export default UserProfile;
