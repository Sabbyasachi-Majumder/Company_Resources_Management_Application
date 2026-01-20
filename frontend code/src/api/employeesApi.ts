// Manipulating the employee service

//the structure of the api response from the backend
interface OperationSummary {
  totalRequested: Number;
  successCount: Number;
  errorCount: Number;
  operationDetails: Record<number, string>;
}

interface ErrorDetails {
  errorCode: string;
  errorMessage: string;
}

interface ApiResponse {
  data: JSON;
}

export async function fetchEmployees(
  page: Number,
  size: Number,
): Promise<ApiResponse> {
  const token = localStorage.getItem("token");
  const response = await fetch(`/api/v1/employees?page=${page}&size=${size}`, {
    method: "GET",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  let data;
  try {
    data = await response.json();
  } catch {
    data = null;
  }

  if (!response.ok) {
    // Throw raw message — let global handler smoothen it
    const rawMessage = "Network error: Unable to reach server";
    throw new Error(rawMessage);
  }

  return data; // success
}
