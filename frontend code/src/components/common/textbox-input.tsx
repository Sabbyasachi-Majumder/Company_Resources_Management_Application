//default textbox input for the application

interface props {
  labelName: string;
  placeHolderName: string;
  inputType: string;
}

export const TextboxInput = (props: props) => {
  return (
    <>
      <div className="textbox-input-container">
        <span>{props.labelName}</span>
        <input
          className="textbox-input"
          placeholder={props.placeHolderName}
          type={props.inputType}
        />
      </div>
    </>
  );
};
