import { yupResolver } from "@hookform/resolvers/yup";
import { useForm, FormProvider } from "react-hook-form";
import LoginForm from "../screens/login/form/LoginForm";
import RegisterForm from "../screens/register/form/RegisterForm";
import SecurityPasswordForm from "../screens/setting/form/SecurityPasswordForm";
import PersonalInfoForm from "../screens/setting/form/PersonalInfoForm";

export const LoginFormHook = ({ schema, onSubmit, loginInfo, navigate }) => {
  const methods = useForm({
    mode: "onChange",
    resolver: yupResolver(schema),
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = methods;

  return (
    <FormProvider {...methods}>
      <LoginForm
        onSubmit={onSubmit}
        loginInfo={loginInfo}
        navigate={navigate}
      />
    </FormProvider>
  );
};

export const RegisterFormHook = ({
  schema,
  onSubmit,
  emailInfo,
  registerInfo,
  sendEmailData,
  sendCodeData,
  handleNext,
  handlePrevious,
  seconds,
  currentStep,
  codeInfo,
  navigate,
  dispatch,
}) => {
  const defaultValues = {
    major: { value: "국어국문학과" },
  };
  const methods = useForm({
    mode: "onChange",
    defaultValues,
    resolver: yupResolver(schema),
  });

  const {
    register,
    handleSubmit,
    getValues,
    setValue,
    formState: { errors },
  } = methods;

  return (
    <FormProvider {...methods}>
      <RegisterForm
        onSubmit={onSubmit}
        emailInfo={emailInfo}
        registerInfo={registerInfo}
        sendEmailData={sendEmailData}
        sendCodeData={sendCodeData}
        handleNext={handleNext}
        handlePrevious={handlePrevious}
        seconds={seconds}
        currentStep={currentStep}
        codeInfo={codeInfo}
        dispatch={dispatch}
        navigate={navigate}
      />
    </FormProvider>
  );
};

export const SecurityPaswordFormHook = ({
  schema,
  onSubmit,
  error,
  loading,
}) => {
  const methods = useForm({
    mode: "onChange",
    resolver: yupResolver(schema),
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = methods;

  return (
    <FormProvider {...methods}>
      <SecurityPasswordForm
        onSubmit={onSubmit}
        error={error}
        loading={loading}
      />
    </FormProvider>
  );
};

export const PersonalInfoFormHook = ({ schema, onSubmit, error, loading }) => {
  const methods = useForm({
    mode: "onChange",
    resolver: yupResolver(schema),
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = methods;

  return (
    <FormProvider {...methods}>
      <PersonalInfoForm onSubmit={onSubmit} error={error} loading={loading} />
    </FormProvider>
  );
};
