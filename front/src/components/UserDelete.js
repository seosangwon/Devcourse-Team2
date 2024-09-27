// src/components/UserDelete.js

import React from 'react';

function UserDelete({ userId, onDelete }) {
    const handleDelete = async () => {
        if (window.confirm("정말로 회원 탈퇴하시겠습니까?")) {
            try {
                const response = await fetch(`/api/v1/members`, {
                    method: 'DELETE',
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('jwt')}`,
                    },
                });

                if (response.ok) {
                    alert("회원 탈퇴가 완료되었습니다.");
                    onDelete(); // App.js에서 상태 초기화
                } else {
                    alert("회원 탈퇴에 실패했습니다.");
                }
            } catch (error) {
                console.error("회원 탈퇴 에러:", error);
                alert("서버에 문제가 발생했습니다.");
            }
        }
    };

    return (
        <div>
            <h2>회원 탈퇴</h2>
            <button className="back-button" onClick={handleDelete}>탈퇴하기</button>
        </div>
    );
}

export default UserDelete;