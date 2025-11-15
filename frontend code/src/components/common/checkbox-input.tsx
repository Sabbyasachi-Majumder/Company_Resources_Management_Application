//default checkbox inputs for the application

import { useState } from "react";

interface props {
  checkboxLabel: string;
  labelData: { string: string };
}

export const CheckboxInput = (props: props) => {
  const [items, setItems] = useState(props.labelData);

  const handleToggle = (key) => {
    setItems((prev) =>
      prev.map((item) =>
        item.key === key ? { ...item, checked: !item.checked } : item
      )
    );
  };

  return (
    <>
      <div className="checkbox-input-container">
        {props.checkboxLabel !== "" ? (
          <span>{props.checkboxLabel}</span>
        ) : (
          <></>
        )}
        <ul style={{ listStyle: "none", padding: 0 }}>
          {props.labelData.map((item) => (
            <li key={item.key} style={{ margin: "12px 0" }}>
              <label
                style={{
                  display: "flex",
                  alignItems: "center",
                  cursor: "pointer",
                }}
              >
                <input
                  type="checkbox"
                  checked={item.checked}
                  onChange={() => handleToggle(item.key)}
                />
                <span style={{ marginLeft: "10px" }}>
                  <strong>{item.key}</strong>: {item.value}
                  {item.checked && " ✔️"}
                </span>
              </label>
            </li>
          ))}
        </ul>
      </div>
    </>
  );
};
