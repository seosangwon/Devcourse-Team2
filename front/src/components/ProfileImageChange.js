// src/components/ProfileImageChange.js

import React, { useState } from 'react';

function ProfileImageChange({ userId }) {
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
        console.log(`Authorization: Bearer ${token}`); // 로그 확인

        try {
            const response = await fetch(`/api/v1/members/update-image`, {
                method: 'PUT',
                headers: {
                    Authorization: `Bearer ${token}`, // Content-Type은 자동으로 설정됩니다.
                },
                body: formData, // FormData를 요청 본문에 포함
            });

            if (response.ok) {
                alert('프로필 이미지가 업데이트되었습니다.');
            } else {
                alert('프로필 이미지 업데이트에 실패했습니다.');
                const errorData = await response.json();
                console.error('Error:', errorData); // 서버에서 반환한 에러 메시지 확인
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
                <input type="file" onChange={handleFileChange} /> {/* 파일 입력 추가 */}
            </div>
            <button type="submit">업데이트</button>
        </form>
    );
}

export default ProfileImageChange;
