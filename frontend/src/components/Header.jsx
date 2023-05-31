import React from "react";
import { useLocation } from "react-router-dom";
import { styled } from "styled-components";
import { Link } from "react-router-dom";

import { LogoImageMedium } from "./Logo";
import { BackButton } from "./Button";
const Header = () => {
  const location = useLocation().pathname;
  return (
    <>
      {location == "/message" ? (
        <div className="header">
          <h1>Message</h1>
        </div>
      ) : location == "/save" ? (
        <div className="header">
          <h1>Save</h1>
        </div>
      ) : location == "/setting" ? (
        <div className="header">
          <h1>Setting</h1>
        </div>
      ) : (
        <></>
      )}
    </>
  );
};

export const HomeHeader = ({ navigate }) => {
  return (
    <HeaderLogoWrap>
      <HeaderButtonWrap>
        <BackButton navigate={navigate} />
      </HeaderButtonWrap>

      <Link to="/">
        <LogoImageMedium />
      </Link>
    </HeaderLogoWrap>
  );
};

export const SaveHeader = ({ navigate }) => {
  return (
    <SaveHeaderWrap>
      <HeaderButtonWrap style={{ left: "0%", top: "3%" }}>
        <BackButton navigate={navigate} />
      </HeaderButtonWrap>
      <span>Like</span>
    </SaveHeaderWrap>
  );
};
export const MessageHeader = () => {
  return (
    <MessageHeaderWrap>
      <span>Message</span>
    </MessageHeaderWrap>
  );
};
const MessageHeaderWrap = styled.div`
  height: 15vh;
  width: 100%;
  display: flex;
  background-color: rgb(128, 113, 252);
  align-items: center;
  justify-content: center;
  span {
    font-size: 2.6rem;
    font-weight: 600;
    color: white;
  }
`;
const HeaderLogoWrap = styled.div`
  height: 11vh;
  width: 100%;

  display: flex;
  margin: 0 2rem;
  position: relative;

  align-items: center;
  justify-content: center;
  a {
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }
`;
const HeaderButtonWrap = styled.div`
  position: absolute;
  top: 0%;
  left: 3%;
  background-color: transparent;
  border: none;
  cursor: pointer;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  height: 100%;
`;
const SaveHeaderWrap = styled.div`
  height: 9vh;
  width: auto;
  display: flex;
  position: relative;
  margin: 0 1.2rem;
  border-bottom: 1.9px solid rgb(128, 113, 252);
  align-items: center;
  justify-content: center;

  span {
    font-size: 1.7rem;
    font-weight: 600;
  }
`;

export default Header;
