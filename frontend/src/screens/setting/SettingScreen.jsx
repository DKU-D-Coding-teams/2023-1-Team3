import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Footer from "../../components/Footer";
import { styled } from "styled-components";
import { SettingHeader } from "../../components/Header";
import { LogoutButton } from "../../components/Button";
import { batch, useDispatch, useSelector } from "react-redux";
import { logoutAction } from "../../actions/userAction";
import { getPersonalInfoAction } from "../../actions/securityEditAction";
import { SECURITY_GET_PERSONALINFO_RESET } from "../../constants/securityEditConstants";
const SettingScreen = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  /** 비밀번호 재설정 후 받는 response값 */
  const personalInfo = useSelector((state) => state.personalInfo);
  const { error, loading, personalInfoStatus: user } = personalInfo;
  const logoutHandler = () => {
    dispatch(logoutAction());
  };

  useEffect(() => {
    batch(async () => {
      await dispatch({ type: SECURITY_GET_PERSONALINFO_RESET });
      dispatch(getPersonalInfoAction());
    });
  }, []);

  const [backgroundImageUrl, setBackgroundImageUrl] = useState(
    `url('./default/default-men.png')`
  );

  useEffect(() => {
    if (user?.image) {
      setBackgroundImageUrl(
        user?.image == "DEFAULT" && user?.gender == "MALE"
          ? `url('./default/default-men.png')`
          : user?.image == "DEFAULT" && user?.gender == "FEMALE"
          ? `url('./default/default-women.png')`
          : `url(${user?.image})`
      );
    }
  }, [user?.image, user?.gender]);

  /** 유저 나이 n살로 변경 */
  const age =
    new Date().getFullYear() - new Date(user?.birth).getFullYear() + 1;
  return (
    <Setting>
      <SettingHeader navigate={navigate} name={"마이페이지"} />
      <SettingUserContent>
        <SettingUserImage
          style={{ backgroundImage: backgroundImageUrl }}
        ></SettingUserImage>
        <SettingUserTextWrap>
          <span style={{ fontSize: "1.4rem" }}>
            <span
              style={{
                fontSize: "1.4rem",
                fontWeight: "600",
                marginRight: ".5rem",
              }}
            >
              {user?.name}
            </span>
            {age}
          </span>

          <span>{user?.department}</span>
        </SettingUserTextWrap>
      </SettingUserContent>
      <SettingMiddlContent>
        <span>자기소개</span>
        <p>{user?.introduction}</p>
      </SettingMiddlContent>
      <SettingAccountContent>
        <Link to="/setting/personal-info">프로필 설정</Link>

        <Link to="/setting/account-security">계정보안 설정</Link>
        <Link>개인정보 보호</Link>
        <Link>문의</Link>
      </SettingAccountContent>
      <SettingLastContent>
        <LogoutButton logout={logoutHandler} />
        <button style={{ color: "red" }}>계정에서 탈퇴</button>
      </SettingLastContent>
      <Footer />
    </Setting>
  );
};

const Setting = styled.div`
  height: 100%;
  position: absolute;
  width: 100%;
  display: flex;
  flex-direction: column;
  font-size: 1rem;

  align-self: start;
`;

const SettingUserContent = styled.div`
  height: 12.5%;
  width: 88%;
  border-radius: 8px;
  display: flex;
  box-shadow: 0px 8px 15px 5px rgb(236, 234, 247, 1);
  align-self: center;
  padding: 1rem;
`;

const SettingUserTextWrap = styled.div`
  height: 100%;
  width: auto;
  display: flex;
  flex-direction: column;
  padding: 0.3rem 1rem;

  span {
    margin-bottom: 0.5rem;
    font-size: 0.9rem;
  }
`;
const SettingUserImage = styled.div`
  height: 5.3rem;
  width: 5.3rem;
  border-radius: 8px;
  display: flex;
  align-self: center;
  background-repeat: none;
  background-size: cover;
  background-position: center;
`;

const SettingMiddlContent = styled.div`
  height: 27.5%;
  width: 88%;
  border-radius: 8px;
  margin: 1.5rem 0;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;

  -webkit-box-shadow: -1px 0px 15px 7px rgb(236, 234, 247, 1);
  align-self: center;
  span {
    font-size: 1rem;
    font-weight: 800;
    color: #8071fc;
    margin-bottom: 0.8rem;
  }
  p {
    color: black;
  }
`;

const SettingAccountContent = styled.div`
  height: 20%;
  width: 88%;
  border-radius: 8px;
  margin: 0 0 1rem 0;
  display: flex;
  flex-direction: column;
  box-shadow: -1px 0px 15px 7px rgb(236, 234, 247, 1);
  -webkit-box-shadow: -1px 0px 15px 7px rgb(236, 234, 247, 1);
  -moz-box-shadow: -1px 0px 15px 7px rgb(236, 234, 247, 1);
  align-self: center;

  a {
    height: 25%;
    width: 100%;
    display: flex;
    align-items: center;
    padding: 0 1.4rem;
    color: black;

    text-decoration: none;
    font-size: 1.1rem;
    background-color: white;
    border: none;
    border-radius: 8px;
    align-self: center;
  }
`;

const SettingLastContent = styled.div`
  height: 9%;
  width: 88%;
  border-radius: 8px;
  margin: 0;
  display: flex;
  flex-direction: column;

  box-shadow: -1px 0px 15px 7px rgb(236, 234, 247, 1);
  -webkit-box-shadow: -1px 0px 15px 7px rgb(236, 234, 247, 1);
  -moz-box-shadow: -1px 0px 15px 7px rgb(236, 234, 247, 1);
  align-self: center;
  button {
    height: 50%;
    width: 100%;
    display: flex;
    align-items: center;
    padding: 0 1.4rem;
    font-size: 0.95rem;
    background-color: white;
    border: none;
    border-radius: 8px;
    align-self: center;
    color: black;
  }
`;

export default SettingScreen;