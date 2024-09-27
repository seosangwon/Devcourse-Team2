// src/components/ProfileImageChange.js

import React, { useState } from 'react';

function ProfileImageChange({ userId }) {
    const [imageUrl, setImageUrl] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(`/api/v1/members/update-image`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${localStorage.getItem('jwt')}`,
                },
                body: JSON.stringify({ imageUrl }), // 이미지 URL을 요청 본문에 포함
            });

            if (response.ok) {
                alert('프로필 이미지가 업데이트되었습니다.');
            } else {
                alert('프로필 이미지 업데이트에 실패했습니다.');
            }
        } catch (error) {
            console.error('프로필 이미지 업데이트 에러:', error);
            alert('서버에 문제가 발생했습니다.');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>새 프로필 이미지 URL:</label>
                <input
                    type="text"
                    value={imageUrl}
                    onChange={(e) => setImageUrl(e.target.value)}
                />
            </div>
            <button type="submit">업데이트</button>
        </form>
    );
}

export default ProfileImageChange;