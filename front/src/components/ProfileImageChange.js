import React, { useEffect, useState } from 'react';
import axiosInstance from "../axiosInstance";

function ProfileImageChange({ onProfileImageChange }) {
    const [file, setFile] = useState(null);
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [profileImage, setProfileImage] = useState('');


    // 컴포넌트 마운트 시 프로필 이미지 로드
    useEffect(() => {
        const fetchProfileImage = async () => {
            try {
                const response = await axiosInstance.get('/api/v1/members/profile-image');
                setProfileImage(response.data.mimage); // 이미지 URL 설정
            } catch (error) {
                console.error('프로필 이미지 로드 실패:', error);
            }
        };
        fetchProfileImage();
    }, []); // 빈 배열을 넣어 컴포넌트 마운트 시 한 번만 호출

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
        setErrorMessage(''); // 파일 선택 시 에러 메시지 초기화
        setSuccessMessage(''); // 성공 메시지 초기화
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData();
        if (file) {
            formData.append('mImage', file);
        } else {
            setErrorMessage('파일을 선택해 주세요.');
            return;
        }

        try {
            onProfileImageChange (profileImage);
            await axiosInstance.put(`/api/v1/members/update-image`, formData);
            setSuccessMessage('프로필 이미지가 업데이트되었습니다.');
        } catch (error) {
            setErrorMessage('프로필 이미지 업데이트 에러: ' + (error.response?.data?.message || '알 수 없는 오류'));
        }
    };

    return (
        <div>
            <div>
                <img
                    src={`/api/v1/members/upload/${profileImage}`}
                    alt="현재 프로필"
                    style={{ width: '100px', height: '100px', borderRadius: '50%', marginBottom: '10px' }}
                />
            </div>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>새 프로필 이미지:</label>
                    <input type="file" onChange={handleFileChange} accept="image/*" />
                </div>
                {errorMessage && <div className="error-message">{errorMessage}</div>}
                {successMessage && <div className="success-message">{successMessage}</div>}
                <button type="submit">업데이트</button>
            </form>
            <div>변경사항은 재접속 후 적용됩니다!</div>
        </div>
    );
}

export default ProfileImageChange;
