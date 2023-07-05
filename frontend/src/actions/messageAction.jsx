//action creator
//Redux thunk

import {
  MESSAGE_GET_REQUEST,
  MESSAGE_FETCH_GET_SUCCESS,
  MESSAGE_SEND_GET_SUCCESS,
  MESSAGE_GET_FAIL,
  MESSAGE_INITIATE,
  MESSAGE_LOG_SUCCESS,
} from "../constants/messageConstants";

export const messageInitiateAction = (user) => async (dispatch) => {
  dispatch({ type: MESSAGE_INITIATE, payload: user });
};

/** 리스트 가졍 Action */
export const getMessagesAction =
  (messageResponse) => async (dispatch, getState) => {
    try {
      dispatch({ type: MESSAGE_GET_REQUEST });

      if (messageResponse?.body) {
        const chatMessageResponse = JSON.parse(messageResponse.body);

        if (chatMessageResponse.status === "SEND") {
          const data = chatMessageResponse?.chatMessages;

          console.log(data);
          dispatch({ type: MESSAGE_SEND_GET_SUCCESS, payload: data });
        } else if (chatMessageResponse.status === "FETCH") {
          if (chatMessageResponse.count !== 0) {
            const data = chatMessageResponse?.chatMessages;
            console.log(data);

            dispatch({ type: MESSAGE_FETCH_GET_SUCCESS, payload: data });
          }
        } else if (chatMessageResponse.status === "LOG") {
          const data = chatMessageResponse?.chatUsers;

          dispatch({ type: MESSAGE_LOG_SUCCESS, payload: data });
        }
      } else {
        const data = JSON.parse(messageResponse);

        dispatch({ type: MESSAGE_SEND_GET_SUCCESS, payload: data });
      }
    } catch (error) {
      dispatch({
        type: MESSAGE_GET_FAIL,
        payload:
          error.response && error.response.data.message
            ? error.response.data.message
            : error.messge,
      });
    }
  };
