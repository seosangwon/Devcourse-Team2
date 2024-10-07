import React, { useState } from 'react';
import './App.css';
import UserInfo from './components/UserInfo';
import Login from './components/Login';
import ProfileImageChange from './components/ProfileImageChange';
import UserDelete from './components/UserDelete';
import Register from './components/Register';
import InsertOrder from './components/InsertOrder';
import OrderListPage from './components/OrderListPage';
import Logout from './components/Logout';
import AddProduct from './components/AddProduct';
import UpdateProduct from './components/UpdateProduct';
import ProductList from './components/ProductList';
import UserControl from './components/UserControl';
import OrderSummary from "./components/OrderSummary";

function App() {
    const [userName, setUserName] = useState('');
    const [userId, setUserId] = useState(null);
    const [activeComponent, setActiveComponent] = useState('login');
    const [activeMenu, setActiveMenu] = useState(null);
    const [profileImage, setProfileImage] = useState('');

    const handleLogin = ( name, mImage) => {
        setUserName(name);
        setProfileImage(mImage ? `/api/v1/members/upload/${mImage}` : '/api/v1/members/upload/defaultImageUrl.jpg');
        setActiveComponent('');
        setActiveMenu(null); // 로그인 시 메뉴 초기화
    };

    const handleLogout = () => {
        setUserName('');
        setUserId(null);
        setProfileImage('');
        setActiveComponent('');
        setActiveMenu(null); // 로그아웃 시 메뉴 초기화
    };

    const handleRegister = (name) => {
        setUserName(name);
        setActiveComponent('');
    };

    const handleUserDelete = () => {
        setUserName('');
        setUserId(null);
        setProfileImage('');
        setActiveComponent('');
        setActiveMenu(null); // 삭제 시 메뉴 초기화
    };

    const showComponent = (component) => {
        setActiveComponent(component);
        setActiveMenu(null); // 메뉴 선택 시 서브 메뉴 초기화
    };

    const handleMenuClick = (menu) => {
        setActiveMenu(menu === activeMenu ? null : menu); // 같은 메뉴 클릭 시 숨김
    };

    const handleBack = () => {
        setActiveComponent('');
        setActiveMenu(null);
    };

    const handleProfileImageChange = async (newImageUrl) => {
        setProfileImage(newImageUrl);
    };


    const renderSubMenu = () => {
        if (activeMenu === 'orderManagement') {
            return (
                <ul className="sub-menu">
                    <li onClick={() => showComponent('insertOrder')}> 발주 신청</li>
                    <li onClick={() => showComponent('orderListPage')}> 발주 목록 확인</li>
                    <li onClick={() => showComponent('orderSummary')}> 주문 요약</li>
                </ul>
            );
        }
        if (activeMenu === 'productManagement') {
            return (
                <ul className="sub-menu">
                    <li onClick={() => showComponent('addProduct')}> 상품 추가</li>
                    <li onClick={() => showComponent('updateProduct')}> 로스율 관리</li>
                    <li onClick={() => showComponent('productList')}> 상품 목록</li>
                </ul>
            );
        }
        if (activeMenu === 'infoManagement') {
            if (userName === '운영자') {
                return (
                    <ul className="sub-menu">
                        <li onClick={() => showComponent('userControl')}> 회원 정보 조회</li>
                        <li onClick={() => showComponent('lossControl')}> 식재료 조회</li>
                    </ul>
                );
            } else {
                return (
                    <ul className="sub-menu">
                        <li onClick={() => showComponent('userInfo')}> 회원 정보 수정</li>
                        <li onClick={() => showComponent('profileImageChange')}> 프로필 사진 수정</li>
                        <li onClick={() => showComponent('userDelete')}> 회원 탈퇴</li>
                    </ul>
                );
            }
        }
        return null;
    };


    return (
        <div className={userName ? 'AppLogAf' : 'AppLogBef'}>
            <div className={userName ? 'index-header' : 'login-header'}>
                <h1>발주 관리 통합 솔루션</h1>
                <h2 className="index-info">
                    {userName ? (
                        <>
                            <img
                                src={profileImage}
                                alt="Profile"
                                style={{ width: '50px', height: '50px', borderRadius: '50%', marginRight: '10px' }}
                            />
                            {userName}
                            <Logout onLogout={handleLogout} />
                        </>
                    ) : '로그인 해주세요'}
                </h2>
            </div>
            <div className="main-container">
                {userName ? (
                    <>
                        <div className="sidebar">
                            <div className="sidebar-header">
                                <button
                                    className="auth-header"
                                    onClick={() => {
                                        // 모든 메뉴와 서브 메뉴 초기화
                                        setActiveMenu(null);
                                        setActiveComponent(''); // 필요한 경우 활성 컴포넌트도 초기화
                                    }}
                                >
                                    목록
                                </button>
                            </div>
                            <button className="auth-button" onClick={() => handleMenuClick('orderManagement')}>
                                발주 관리
                            </button>
                            {activeMenu === 'orderManagement' && renderSubMenu()}

                            <button className="auth-button" onClick={() => handleMenuClick('productManagement')}>
                                상품 관리
                            </button>
                            {activeMenu === 'productManagement' && renderSubMenu()}

                            <button className="auth-button" onClick={() => handleMenuClick('infoManagement')}>
                                정보 관리
                            </button>
                            {activeMenu === 'infoManagement' && renderSubMenu()}
                        </div>

                        <div className="component-container">
                            {activeComponent === 'userInfo' && <UserInfo userId={userId} onUpdate={setUserName} />}
                            {activeComponent === 'profileImageChange' &&
                                <ProfileImageChange
                                    userId={userId}
                                    onProfileImageChange={handleProfileImageChange}
                                    onSuccess={(msg) => alert(msg)} // 성공 메시지 처리
                                    onError={(msg) => alert(msg)} // 에러 메시지 처리
                                />
                            }

                            {activeComponent === 'userDelete' && <UserDelete userId={userId} onDelete={handleUserDelete} />}
                            {activeComponent === 'insertOrder' && <InsertOrder memberId={userId} />}
                            {activeComponent === 'orderListPage' && <OrderListPage />}
                            {activeComponent === 'orderSummary' && <OrderSummary />}
                            {activeComponent === 'addProduct' && <AddProduct />}
                            {activeComponent === 'updateProduct' && <UpdateProduct />}
                            {activeComponent === 'productList' && <ProductList />}
                            {activeComponent === 'userControl' && <UserControl />}
                            {activeComponent === 'lossControl' && <ProductList />}
                        </div>
                    </>
                ) : (
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
                        {activeComponent === 'login' && <Login onLogin={handleLogin} handleBack={handleBack}/>}
                        {activeComponent === 'register' && <Register onRegister={handleRegister} handleBack={handleBack}/>}
                    </div>
                )}
            </div>
        </div>
    );
}

export default App;
