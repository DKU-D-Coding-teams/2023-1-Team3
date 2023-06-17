import React, { useCallback, useEffect, useState } from "react";
import { batch, useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { styled } from "styled-components";
import { PersonalInfoFormHook } from "../../../hooks/FormHoooks";
import { personalInfoSchema } from "../../../components/Form/Schema";
import { DefaultBackHeader } from "../../../components/Header";
import {
  getPersonalInfoAction,
  personalInfoEditAction,
} from "../../../actions/securityEditAction";
import { ChangeProfileButton } from "../../../components/Button";

import PersonalImageEdit from "./imageEdit/PersonalImageEdit";
import { BackFormButton } from "../../../components/Button";
import {
  securityUserInfoEditResetAction,
  securityUserInfoResetAction,
  securityUserProfileResetAction,
} from "../../../actions/resetAction";

const PersonalInfoScreen = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const personalInfo = useSelector((state) => state.personalInfo);
  const personalEditInfo = useSelector((state) => state.personalEditInfo);

  const profileEditInfo = useSelector((state) => state.profileEditInfo);
  const { personalInfoStatus: user } = personalInfo;
  useEffect(() => {
    batch(async () => {
      await dispatch(securityUserInfoEditResetAction());
      dispatch(securityUserInfoResetAction());
      dispatch(securityUserProfileResetAction());

      dispatch(getPersonalInfoAction());
    });
  }, [batch]);

  const onSubmit = async (data) => {
    await dispatch(
      personalInfoEditAction({
        department: data.major,
        introduction: data.description,
        name: data.name,
        gender: data.gender,
        birth: data.birthday?.toISOString().split("T")[0],
      })
    );
  };

  const [changeProfile, setChangeProfile] = useState(false);

  const handleChangeProfile = useCallback(() => {
    setChangeProfile((prev) => !prev);
  });
  return (
    <PersonalInfo>
      {!changeProfile ? (
        <DefaultBackHeader navigate={navigate} />
      ) : (
        <GoBackToProfile>
          {" "}
          <BackFormButton handlePrevious={handleChangeProfile} />
        </GoBackToProfile>
      )}
      {!changeProfile && (
        <PersonalInfoImage
          decoding="async"
          fetchpriority="high"
          loading="lazy"
          src={
            user?.image == "DEFAULT" && user?.gender == "MALE"
              ? `
../default/default-men.png`
              : user?.image == "DEFAULT" && user?.gender == "FEMALE"
              ? `
../default/default-women.png`
              : user?.image
          }
          alt="profileEdit-profile"
        ></PersonalInfoImage>
      )}

      {changeProfile && (
        <PersonalImageEdit
          profileEditInfo={profileEditInfo}
          dispatch={dispatch}
          getPersonalInfoAction={getPersonalInfoAction}
        />
      )}

      <PersonalInfoContent style={{ display: changeProfile && "none" }}>
        <PersonalInfoFormHook
          schema={personalInfoSchema}
          info={personalEditInfo}
          onSubmit={onSubmit}
          user={user}
        />
      </PersonalInfoContent>

      <PersonalInfoButtonWrap>
        {!changeProfile && (
          <ChangeProfileButton handleChangeProfile={handleChangeProfile} />
        )}

        {/* <SaveProfileButton /> */}
      </PersonalInfoButtonWrap>
    </PersonalInfo>
  );
};
const PersonalInfoButtonWrap = styled.div`
  width: auto;
  // width: 12rem;

  height: auto;
  position: absolute;
  top: 43%;
  right: 3%;
  display: flex;
  justify-content: space-between;
`;

const PersonalInfo = styled.div`
  height: 100%;
  position: absolute;
  width: 100%;
  display: flex;
  flex-direction: column;
  font-size: 1rem;

  align-self: start;
`;

const GoBackToProfile = styled.div`
  position: absolute;
  top: 4%;
  left: 3%;
  z-index: 1000;
`;

const PersonalInfoImage = styled.img`
  height: 48%;
  width: 100%;
  display: flex;
  flex-direction: column;
  object-fit: cover;
`;

const PersonalInfoContent = styled.div`
  height: 52%;
  width: 100%;
  display: flex;
  flex-direction: column;
  padding: 1rem;
  overflow: scroll;

  &::-webkit-scrollbar {
    display: none;
  }
`;

export default PersonalInfoScreen;
