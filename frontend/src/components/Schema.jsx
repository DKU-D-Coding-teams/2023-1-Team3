import * as yup from "yup";

export const schema = yup
  .object({
    name: yup
      .string()
      .required("이름을 입력해 주세요")
      .min(2, "이름을 최소 두 글자로 작성하세요")
      .matches(/^[a-zA-Zㄱ-ㅎ가-힣]*$/, "숫자,특수문자는 불가능합니다. "),

    email: yup
      .string()
      .required("이메일을 입력하세요")
      .matches(/\S+@dankook\.ac\.kr/, "@dankook.ac.kr 형식에 맞게 작성하세요"),

    password: yup
      .string()
      .required("비밀번호를 입력해 주세요")
      .min(8, "비밀번호는 최소 8글자입니다"),
    secondPassword: yup
      .string()
      .required()
      .oneOf([yup.ref("password")], "비밀번호가 일치하지 않습니다."),
    code: yup.string().required("이메일을 확인하세요"),
    birthday: yup
      .date()
      .required("필수 항목란 입니다.")
      .max(
        new Date(new Date().setFullYear(new Date().getFullYear() - 18)),
        "19살 이상만 이용가능합니다."
      ),
    gender: yup.string().required("성별은 필수 입력란 입니다."),
  })
  .required();
