import React from "react";
import { peopleListAction } from "../actions/peopleAction";
import { useDispatch } from "react-redux";
import { styled } from "styled-components";
import { HomeWrap } from "../screens/home/HomeContent";
const NoValueUser = ({ endPageSignal }) => {
  const dispatch = useDispatch();

  return (
    <HomeWrap>
      <NoValueWrap>
        <NoValueImageWrap>
          <NoValueImage src="/no-value.png" alt="no-value-image" />
        </NoValueImageWrap>

        <NoValueTextWrap>
          <NoValueTextBig>더 볼 유저가 없습니다.. </NoValueTextBig>
          <NoValueTextSmall>처음부터 유저를 다시 볼까요?</NoValueTextSmall>
        </NoValueTextWrap>
        <NoValueTextWrap>
          <button onClick={() => dispatch(peopleListAction())}>
            마지막 페이지입니다, 다시 보겠습니까? 클릭!
          </button>
          <NoValueTextSmall
            style={{ fontSize: "0.8rem", color: "rgb(128, 113, 252)" }}
          >
            유저 다시보기가 안된다면 저장화면에 가보세요!
          </NoValueTextSmall>
        </NoValueTextWrap>
      </NoValueWrap>
    </HomeWrap>
  );
};

const NoValueTextBig = styled.span`
  font-size: 1.5rem;
  font-weight: 700;
  padding: 0.7rem 0;
`;

const NoValueTextWrap = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 25%;
`;
const NoValueTextSmall = styled.span`
  font-size: 1rem;
  font-weight: 600;
`;

const NoValueImage = styled.img`
  width: 11rem;
  height: 14rem;
`;
const NoValueImageWrap = styled.div`
  width: 100%;
  height: 50%;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  padding: 0.4rem 0;
`;

const NoValueWrap = styled.div`
  height: 99%;
  width: 85%;
  border-radius: 12px;

  display: flex;
  justify-content: space-around;
  flex-direction: column;
  padding: 0.7rem 0;
  align-items: center;
  box-shadow: 0px 0px 17px 6px rgb(236, 234, 247, 1);
  -webkit-box-shadow: 0px 0px 17px 6px rgb(236, 234, 247, 1);
  -moz-box-shadow: 0px 0px 17px 6px rgb(236, 234, 247, 1);
`;
export default NoValueUser;
