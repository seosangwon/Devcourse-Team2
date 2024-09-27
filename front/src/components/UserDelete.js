// src/components/UserDelete.js

import React, { useState } from 'react';

function UserDelete({ onDelete }) {
    const [loading, setLoading] = useState(false);

    const handleDelete = async () => {
        if (window.confirm("정말로 회원 탈퇴하시겠습니까?")) {
            setLoading(true);
            try {
                const response = await fetch(`/api/v1/members/`, {
                    method: 'DELETE',
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`,
                    },
                });

                if (response.ok) {
                    alert("회원 탈퇴가 완료되었습니다.");
                    localStorage.removeItem('jwt'); // JWT 토큰 삭제
                    onDelete(); // App.js에서 상태 초기화
                } else {
                    const errorData = await response.json();
                    alert(`회원 탈퇴에 실패했습니다: ${errorData.message || '알 수 없는 오류 발생'}`);
                }
            } catch (error) {
                console.error("회원 탈퇴 에러:", error);
                alert("서버에 문제가 발생했습니다.");
            } finally {
                setLoading(false);
            }
        }
    };

    return (
        <div className="user-delete-container">
            <h2>회원 탈퇴</h2>
            <button className="delete-button" onClick={handleDelete} disabled={loading}>
                {loading ? '탈퇴 중...' : '탈퇴하기'}
            </button>
        </div>
    );
}

export default UserDelete;
