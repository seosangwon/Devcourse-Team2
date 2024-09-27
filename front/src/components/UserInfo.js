import React, { useEffect, useState } from 'react';
import axios from 'axios';

function UserProfile() {
    const [userInfo, setUserInfo] = useState(null);

    useEffect(() => {
        const fetchUserInfo = async () => {
            const token = localStorage.getItem('token');

            try {
                const response = await axios.get('/api/v1/members/', {
                    headers: { Authorization: `Bearer ${token}` }
                });
                setUserInfo(response.data);
            } catch (error) {
                console.error('사용자 정보 조회 실패:', error);
            }
        };

        fetchUserInfo();
    }, []);

    if (!userInfo) return <div>로딩 중...</div>;

    return (
        <div>
            <h2>사용자 정보</h2>
            <p>이름: {userInfo.name}</p>
            <p>로그인 ID: {userInfo.loginId}</p>
        </div>
    );
}

export default UserProfile;
