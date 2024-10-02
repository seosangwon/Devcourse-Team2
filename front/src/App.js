import React, {useEffect, useState} from 'react';
import './App.css';
import UserInfo from './components/UserInfo';
import Login from './components/Login';
import ProfileImageChange from './components/ProfileImageChange';
import UserDelete from './components/UserDelete';
import Register from './components/Register';
import InsertOrder from "./components/InsertOrder";
import OrderListPage from "./components/OrderListPage";
import Logout from "./components/Logout";
import AddProduct from "./components/AddProduct"
import UpdateProduct from "./components/UpdateProduct"
import ProductList from "./components/ProductList"
import UserControl from "./components/UserControl";


function App() {
    const [userName, setUserName] = useState('');
    const [userId, setUserId] = useState(null);
    const [activeComponent, setActiveComponent] = useState('');
    const [showSubMenuM, setShowSubMenuM] = useState(false);
    const [showSubMenuO, setShowSubMenuO] = useState(false);
    const [showSubMenuP, setShowSubMenuP] = useState(false);
    const [showSubMenuE, setShowSubMenuE] = useState(false);
    const [profileImage, setProfileImage] = useState('');


    const handleLogin = (name, mImage) => {
        setUserName(name);
        setProfileImage(mImage ? `/api/v1/members/upload/${mImage}` : '/api/v1/members/upload/defaultImageUrl.jpg'); // mImage가 없으면 기본 이미지 사용
        setActiveComponent('');
    };

    const handleLogout = () => {
        setUserName('');
        setUserId(null);
        setProfileImage('');
        setActiveComponent('');
    };

    const handleRegister = (name) => {
        setUserName(name);
        setActiveComponent('');
    };

    const handleUserDelete = () => {
        setUserName('');
        setUserId(null);
        setProfileImage(''); // 이미지 초기화
        setActiveComponent('');
    };

    const showComponent = (component) => {
        setActiveComponent(component);
    };

    const handleMouseEnterM = () => {
        setShowSubMenuM(true);
    };

    const handleMouseLeaveM = () => {
        setShowSubMenuM(false);
    };

    const handleMouseEnterP = () => {
        setShowSubMenuP(true);
    };

    const handleMouseLeaveP = () => {
        setShowSubMenuP(false);
    };

    const handleMouseEnterO = () => {
        setShowSubMenuO(true);
    };

    const handleMouseLeaveO = () => {
        setShowSubMenuO(false);
    };

    const handleMouseEnterE = () => {
        setShowSubMenuE(true);
    };

    const handleMouseLeaveE = () => {
        setShowSubMenuE(false);
    };

    const handleBack = () => {
        setActiveComponent('');
    };

    const handleProfileImageChange = (newImage) => {
        setProfileImage(newImage); // 프로필 이미지 업데이트
    };


    return (
        <div className={userName ? 'AppLogAf' : 'AppLogBef'}>
            <h1>발주 관리 통합 솔루션</h1>
            <h2 className="index-info">
                {userName ? (
                    <>
                        <img onChange={handleProfileImageChange}
                            src={profileImage}
                            alt="Profile"
                            style={{ width: '50px', height: '50px', borderRadius: '50%', marginRight: '10px' }}
                        />
                        {userName}
                        <Logout onLogout={handleLogout} /> {/* Logout 컴포넌트 사용 */}
                    </>
                ) : '로그인 해주세요'}
            </h2>

            {!userName ? (
                <div className="auth-container">
                    {activeComponent === '' && (
                        <button className="auth-button" onClick={() => setActiveComponent('login')}>
                            로그인
                        </button>
                    )}
                    {activeComponent === '' && (
                        <button className="auth-button" onClick={() => setActiveComponent('register')}>
                            회원가입
                        </button>
                    )}
                    {activeComponent === 'login' && <Login onLogin={handleLogin} />}
                    {activeComponent === 'register' && <Register onRegister={handleRegister} />}
                    {(activeComponent === 'login' || activeComponent === 'register') && (
                        <button className="back-button" onClick={handleBack}>뒤로가기</button>
                    )}
                </div>
            ) : activeComponent ? (
                <div className="component-container">
                    {activeComponent === 'userInfo' && <UserInfo userId={userId} onUpdate={setUserName} />}
                    {activeComponent === 'profileImageChange' &&
                        <ProfileImageChange
                            userId={userId}
                            onProfileImageChange={handleProfileImageChange}
                            />}
                    {activeComponent === 'userDelete' && <UserDelete userId={userId} onDelete={handleUserDelete} />}
                    {activeComponent === 'insertOrder' && <InsertOrder memberId={userId} />}
                    {activeComponent === 'orderListPage' && <OrderListPage />}
                    {activeComponent === 'addProduct' && <AddProduct />}
                    {activeComponent === 'updateProduct' && <UpdateProduct />}
                    {activeComponent === 'productList' && <ProductList />}
                    {activeComponent === 'userControl' && <UserControl />}
                    {activeComponent === 'lossControl' && <ProductList />}
                    <button className="back-button" onClick={handleBack}>뒤로가기</button>
                </div>
            ) : (
                <div>
                    <div className="container">
                        <button className="box color1" onMouseEnter={handleMouseEnterO}
                                onMouseLeave={handleMouseLeaveO}>
                            {showSubMenuO ? (
                                <div className="submenu">
                                    <ul>
                                        <li onClick={() => showComponent('insertOrder')}>▶ 발주 신청</li>
                                        <li onClick={() => showComponent('orderListPage')}>▶ 발주 목록 확인</li>
                                    </ul>
                                </div>
                            ) : (
                                "발주 관리"
                            )}
                        </button>
                        <button className="box color2" onMouseEnter={handleMouseEnterP}
                                onMouseLeave={handleMouseLeaveP}>
                            {showSubMenuP ? (
                                <div className="submenu">
                                    <ul>
                                        <li onClick={() => showComponent('addProduct')}>▶ 상품 추가</li>
                                        <li onClick={() => showComponent('updateProduct')}>▶ 로스율 관리</li>
                                        <li onClick={() => showComponent('productList')}>▶ 상품 목록</li>
                                    </ul>
                                </div>
                            ) : (
                                "상품 관리"
                            )}
                        </button>
                        <button className="box color3" onMouseEnter={handleMouseEnterM}
                                onMouseLeave={handleMouseLeaveM}>
                            {showSubMenuM ? (
                                userName === 'admin' ? (
                                <div className="submenu">
                                    <ul>
                                        <li onClick={() => showComponent('userControl')}>▶ 회원 정보 조회</li>
                                        <li onClick={() => showComponent('lossControl')}>▶ 식재료 조회</li>
                                    </ul>
                                </div>
                            ) : (
                                <div className="submenu">
                                    <ul>
                                        <li onClick={() => showComponent('userInfo')}>▶ 회원 정보 수정</li>
                                        <li onClick={() => showComponent('profileImageChange')}>▶ 프로필</li>
                                        <li onClick={() => showComponent('userDelete')}>▶ 회원 탈퇴</li>
                                    </ul>
                                </div>
                                )
                            ) : userName === 'admin' ? (
                                "관리자 정보 관리"
                            ) : (
                                "정보 관리"
                            )}
                        </button>
                        <button className="box color4" onMouseEnter={handleMouseEnterE}
                                onMouseLeave={handleMouseLeaveE}>
                            {showSubMenuE ? (
                                <div className="submenu">
                                    <ul>
                                        <li onClick={() => showComponent('ect1')}>▶ 추가 기능</li>
                                        <li onClick={() => showComponent('ect2')}>▶ 추가 기능</li>
                                        <li onClick={() => showComponent('ect3')}>▶ 추가 기능</li>
                                    </ul>
                                </div>
                            ) : (
                                "상품 관리"
                            )}
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
}

export default App;
