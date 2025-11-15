//
export const parseToJson = (jsonInput: any) => {
  try {
    return JSON.parse(jsonInput);
  } catch {
    return "error"; // fallback
  }
};
