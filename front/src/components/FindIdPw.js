import React, { useState } from 'react';
import axios from 'axios';
import axiosInstance from "../axiosInstance";

const FindIdPw = () => {
    const [email, setEmail] = useState('');
    const [loginId, setLoginId] = useState('');
    const [message, setMessage] = useState('');
    const [templatePassword, setTemplatePassword] = useState('');

    const handleFindId = async () => {
        try {
            const response = await axiosInstance.get(`/api/v1/members/findId?email=${email}`);
            setLoginId(response.data);
            setMessage(`아이디가 성공적으로 전송되었습니다: ${response.data}`);
        } catch (error) {
            setMessage('아이디 찾기에 실패하였습니다.');
        }
    };

    const handleFindPw = async () => {
        try {
            const response = await axiosInstance.post('/api/v1/members/findPW', { loginId, email });
            setTemplatePassword(response.data);
            setMessage('임시 비밀번호가 이메일로 전송되었습니다.');
        } catch (error) {
            setMessage('비밀번호 찾기에 실패하였습니다.');
        }
    };

    return (
        <div>
            <h2>ID/PW 찾기</h2>
            <div>
                <label>이메일:</label>
                <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
            </div>
            <div>
                <button onClick={handleFindId}>아이디 찾기</button>
            </div>
            {loginId && <p>찾은 아이디: {loginId}</p>}
            <div>
                <label>아이디:</label>
                <input
                    type="text"
                    value={loginId}
                    onChange={(e) => setLoginId(e.target.value)}
                    required
                />
            </div>
            <div>
                <button onClick={handleFindPw}>비밀번호 찾기</button>
            </div>
            {message && <p>{message}</p>}
        </div>
    );
};

export default FindIdPw;
