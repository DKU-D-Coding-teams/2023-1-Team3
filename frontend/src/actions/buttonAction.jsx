import {
  BUTTON_SAVE_REQUEST,
  BUTTON_SAVE_SUCCESS,
  BUTTON_SAVE_FAIL,
} from "../constants/buttonConstants";
import axios from "axios";

//action creator
//Redux thunk

/** 이메일에 인증코드 전송 Action */
export const saveUserAction = (told, signal) => async (dispatch, getState) => {
  try {
    dispatch({ type: BUTTON_SAVE_REQUEST });

    const {
      loginInfo: { sessfbs_ffa0934 },
    } = getState();

    const config = {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${sessfbs_ffa0934.accessToken}`,
      },
    };
    const { data } = await axios.put(
      `${import.meta.env.VITE_API_URL}/choice?toId=${told}&signal=${signal}`,
      config
    );

    dispatch({ type: BUTTON_SAVE_SUCCESS, payload: data });
  } catch (error) {
    dispatch({
      type: BUTTON_SAVE_FAIL,
      payload:
        error.response && error.response.data.message
          ? error.response.data.message
          : error.messge,
    });
  }
};
