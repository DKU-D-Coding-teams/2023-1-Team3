import styled from "styled-components";
import React, { memo } from "react";
import { isObjectEmpty } from "./TypeFunction";
import { BoxIconElement } from "boxicons";

export const SubmitButton = ({ page }) => {
  const submitButtonStyle = {
    backgroundColor: "rgb(128, 113, 252)",
    width: page == "register" ? "100%" : "47.5%",

    height: page == "register" ? " 3.3rem" : "3rem",
  };
  return (
    <DefaultFormButtom type="submit" style={submitButtonStyle}>
      {page == "login" ? "로그인 " : "회원가입"}
    </DefaultFormButtom>
  );
};
export const GoToRegisterButton = ({ page, navigate }) => {
  return (
    <DefaultFormButtom
      type="button"
      style={{ backgroundColor: "#B1B1B1" }}
      onClick={() => {
        if (page) navigate(`/${page}`);
      }}
    >
      {page == "login" ? "로그인 " : "회원가입"}{" "}
    </DefaultFormButtom>
  );
};

export const VerifyButton = ({ sendEmailData, getValues }) => {
  return (
    <VerifyStyleButton
      type="submit"
      onClick={() => {
        sendEmailData(getValues("email"));
      }}
    >
      전송
    </VerifyStyleButton>
  );
};

export const VerifyCodeButton = ({
  sendCodeData,
  getValueEmail,

  getValueCode,
}) => {
  return (
    <VerifyStyleButton
      onClick={() => {
        sendCodeData(getValueEmail, getValueCode);
      }}
      type="button"
    >
      인증
    </VerifyStyleButton>
  );
};

export const BackButton = memo(function BackButton({ navigate }) {
  return (
    <BackArrowButton
      onClick={() => {
        navigate(-1);
      }}
    >
      <box-icon name="chevron-left" color="black" size="3rem"></box-icon>
    </BackArrowButton>
  );
});
export const BackFormButton = ({ handlePrevious }) => {
  return (
    <BackArrowButton onClick={handlePrevious}>
      <box-icon name="chevron-left" color="black" size="3rem"></box-icon>
    </BackArrowButton>
  );
};
export const SaveCheckButton = ({ sendImageToServer }) => {
  return (
    <button onClick={sendImageToServer}>
      <box-icon
        name="check"
        color="rgb(196, 196, 196)"
        size="2.3rem"
      ></box-icon>
    </button>
  );
};
export const ImageRegisterButton = ({
  handleNext,
  sendImageToServer,
  content,
}) => {
  return (
    <NextButtonWrap>
      <NextButton
        type="button"
        onClick={async () => {
          handleNext();
          await sendImageToServer();
        }}
      >
        {content}
      </NextButton>
    </NextButtonWrap>
  );
};
export const AfterRegisterButton = ({ content, navigate }) => {
  return (
    <NextButtonWrap>
      <NextButton
        type="button"
        onClick={() => {
          navigate(`/login`);
        }}
      >
        {content}
      </NextButton>
    </NextButtonWrap>
  );
};
export const NextStepButton = ({ handleNext, inputErrors, getValues }) => {
  return (
    <NextButtonWrap>
      <NextButton
        type="submit"
        disabled={!getValues || inputErrors == true ? true : false}
        onClick={() => {
          if (!getValues || inputErrors == true) {
          } else {
            handleNext();
          }
        }}
      >
        다음
      </NextButton>
    </NextButtonWrap>
  );
};

export const EmailNextStepButton = ({ handleNext, codeInfo, emailInfo }) => {
  return (
    <NextButtonWrap>
      <NextButton
        type="submit"
        onClick={() => {
          if (
            !emailInfo?.loading &&
            emailInfo?.emailStatus &&
            codeInfo?.codeBoolean == true &&
            !codeInfo?.loading
          )
            handleNext();
        }}
      >
        다음
      </NextButton>
    </NextButtonWrap>
  );
};
export const PreviousStepButton = memo(function PreviousStepButton({
  handlePrevious,
}) {
  return (
    <NextButton
      className="page-handler-button"
      onClick={() => {
        handlePrevious();
      }}
    >
      previous page
    </NextButton>
  );
});
export const UserDeleteButton = memo(function UserDeleteButton({
  goNextSlideHandler,
}) {
  return (
    <SmallUserButton
      type="button"
      onClick={() => {
        goNextSlideHandler();
      }}
    >
      <box-icon name="x" color="rgb(128, 113, 252)" size="2.5rem"></box-icon>
    </SmallUserButton>
  );
});

export const RecapUserListsButton = memo(function UserDeleteButton({
  getPeopleList,
}) {
  return (
    <SmallNextButton>
      <NextButton
        style={{
          fontSize: "1rem",
          fontWeight: "800",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
        }}
        type="button"
        onClick={() => {
          getPeopleList();
        }}
      >
        유저 다시보기
        <box-icon name="chevron-right" color="white" size="2rem"></box-icon>
      </NextButton>
    </SmallNextButton>
  );
});

export const UserMessageButton = () => {
  return (
    <SmallUserButton type="button">
      <box-icon
        color="rgb(128, 113, 252)"
        name="message-rounded"
        size="2rem"
        type="solid"
      ></box-icon>
    </SmallUserButton>
  );
};

export const UserLikeButton = memo(function UserLikeButton({
  saveValue,
  setSaveValue,
  sendLikeUser,
  memberId,
}) {
  return (
    <MediumUserButton
      type="button"
      style={{
        boxShadow:
          saveValue == true && " 0px 0px 33px 10px rgba(128,113,252,0.46)",
        WebkitBoxShadow:
          saveValue == true && " 0px 0px 33px 10px rgba(128,113,252,0.46)",
        MozBoxShadow:
          saveValue == true && " 0px 0px 33px 10px rgba(128,113,252,0.46)",
      }}
      onClick={async () => {
        setSaveValue((state) => !state);

        await sendLikeUser(memberId, !saveValue);
      }}
    >
      {saveValue == false && (
        <box-icon
          color="rgb(128, 113, 252)"
          name="heart"
          size="2.3rem"
          type="solid"
        ></box-icon>
      )}

      {saveValue == true && (
        <box-icon
          color="rgb(128, 113, 252)"
          name="heart"
          size="2.5rem"
          type="solid"
        ></box-icon>
      )}
    </MediumUserButton>
  );
});

export const FullStepButton = memo(function FullStepButton({
  handlePrevious,
  handleNext,
}) {
  return (
    <PageButtonWrap>
      <PreviousStepButton handlePrevious={handlePrevious} />

      <NextStepButton handleNext={handleNext} />
    </PageButtonWrap>
  );
});

export const GoFormButton = ({ page, navigate }) => {
  return (
    <>
      <NextButtonWrap style={{ height: "1rem" }}>
        <GoAnotherForm
          type="button"
          onClick={() => {
            if (page) navigate(`/${page}`);
          }}
        >
          {page == "login" ? "이미 가입을 하셨나요? " : "계정이 없나요?"}{" "}
        </GoAnotherForm>
      </NextButtonWrap>
    </>
  );
};

const SmallNextButton = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 3.3rem;
  width: 45%;
`;
const GoAnotherForm = styled.button`
  height: 2rem;
  color: rgb(128, 113, 252);
  background-color: white;
  border: none;
  font-weight: 600;
  font-size: 0.9rem;
  cursor: pointer;
`;
const SmallUserButton = styled.button`
  background-color: white;

  width: auto;
  height: auto;
  cursor: pointer;
  border: none;
  margin: 0 0.5rem;
`;
const PageButtonWrap = styled.div`
  display: flex;
  justify-content: space-between;
  height: 2rem;
  width: 100%;
  margin-top: 2rem;
`;
const NextButtonWrap = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 3.3rem;
  width: 100%;
  margin-top: 3rem;
`;

const NextButton = styled.button`
  width: 100%;
  height: 100%;
  background-color: rgb(128, 113, 252);
  color: white;
  border: none;
  font-weight: 600;
  font-size: 1.3rem;
  border-radius: 5px;
  cursor: pointer;
`;
const BackArrowButton = styled.button`
  position: fixed;
  top: 3%;
  left: 3%;

  background-color: transparent;

  border: none;
  cursor: pointer;
`;
const DefaultFormButtom = styled.button`
  width: 47.5%;
  height: 2.5rem;
  border: none;
  font-weight: 600;
  border-radius: 5px;
  font-size: 1.2rem;
  color: white;
  margin: 2.2rem 0;
  cursor: pointer;
`;

const VerifyStyleButton = styled.button`
  width: 6rem;
  height: 3rem;
  border: none;
  font-weight: 600;
  border-radius: 5px;
  font-size: 1.1rem;
  color: white;
  margin: 0.5rem;
  background-color: rgb(128, 113, 252);
  display: flex;
  text-align: center;
  justify-content: center;
  align-items: center;
  cursor: pointer;
`;

const MediumUserButton = styled.button`
  background-color: white;
  border-radius: 50%;
  width: 3rem;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 3rem;
  border: none;
  transition: all 0.2s ease-in-out;
`;
