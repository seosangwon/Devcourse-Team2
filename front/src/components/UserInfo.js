import React, { useState, useEffect } from 'react';

function UserInfo({ userId }) { // userId를 props로 받음
    const [userInfo, setUserInfo] = useState({ id: userId, loginId: '', name: '', mImage: '' });

    useEffect(() => {
        fetch(`/api/user-info/${userId}`) // login 구현 후 ID를 가져오는 로직으로 완성해야함
            .then(response => response.json())
            .then(data => setUserInfo(data))
            .catch(err => console.error(err));
    }, [userId]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setUserInfo({ ...userInfo, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        fetch(`api/v1/members/15`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userInfo),
        })
            .then(response => {
                if (response.ok) {
                    alert('정보가 업데이트되었습니다.');
                } else {
                    alert('정보 업데이트에 실패했습니다.');
                }
            })
            .catch(err => console.error(err));
    };

    return (
        <div>
            <h2>사용자 정보 관리</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>로그인 ID:</label>
                    <input type="text" name="loginId" value={userInfo.loginId} onChange={handleChange} />
                </div>
                <div>
                    <label>이름:</label>
                    <input type="text" name="name" value={userInfo.name} onChange={handleChange} />
                </div>
                <div>
                    <label>프로필 이미지:</label>
                    <input type="text" name="mImage" value={userInfo.mImage} onChange={handleChange} />
                </div>
                <button type="submit">정보 업데이트</button>
            </form>
        </div>
    );
}

export default UserInfo;
