// src/components/ProfileImageChange.js

import React, { useState } from 'react';

function ProfileImageChange({ userId, onProfileImageChange }) {
    const [file, setFile] = useState(null); // 파일 상태 추가

    const handleFileChange = (e) => {
        setFile(e.target.files[0]); // 파일 선택 시 상태 업데이트
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData();
        if (file) {
            formData.append('mImage', file); // 선택한 파일 추가
        }

        const token = localStorage.getItem('token');

        try {
            const response = await fetch(`/api/v1/members/update-image`, {
                method: 'PUT',
                headers: {
                    Authorization: `Bearer ${token}`,
                },
                body: formData,
            });

            if (response.ok) {
                const updatedImageUrl = `/api/v1/members/upload/${file.name}`; // 예시로 업데이트된 이미지 URL
                onProfileImageChange(updatedImageUrl); // 부모 컴포넌트에 이미지 변경 통지
                alert('프로필 이미지가 업데이트되었습니다.');
            } else {
                alert('프로필 이미지 업데이트에 실패했습니다.');
                const errorData = await response.json();
                console.error('Error:', errorData);
            }
        } catch (error) {
            console.error('프로필 이미지 업데이트 에러:', error);
            alert('서버에 문제가 발생했습니다.');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>새 프로필 이미지:</label>
                <input type="file" onChange={handleFileChange} />
            </div>
            <button type="submit">업데이트</button>
        </form>
    );
}

export default ProfileImageChange;
