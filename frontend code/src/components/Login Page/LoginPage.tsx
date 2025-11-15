// Login Page for the application
import "./LoginPage.css";

import { TextboxInput } from "../common/textbox-input";
import { CheckboxInput } from "../common/checkbox-input";
import { parseToJson } from "../utility-functions/parseToJson";

export const LoginPage = () => {
  const sampleData = {
    "Learn React": "Core library",
    "Use Vite": "Fast dev server",
    "Master Hooks": "useState, useEffect",
    "Build Projects": "Portfolio pieces",
  };
  return (
    <div className="login-page-container">
      <div className="header">Login Page</div>
      <div className="textbox-input">
        <TextboxInput
          labelName={"User-Id"}
          inputType={"textbox"}
          placeHolderName="Enter the user id"
        />
      </div>
      <div className="textbox-input">
        <TextboxInput
          labelName={"Password"}
          inputType={"password"}
          placeHolderName="Enter the Password"
        />
      </div>
      <div className="info-check-password-reset-container">
        <div className="remember-user-box">
          <CheckboxInput checkboxLabel="" labelData={parseToJson(sampleData)} />
        </div>
      </div>
      <div className="login-button">login-button</div>
      <div className="register-line">register-line</div>
    </div>
  );
};
