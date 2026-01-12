// Manipulating the employee service

//the structure of the api response from the backend
interface Employee {
  employeeId: number;
  firstName: string;
  lastName: string;
  dateOfBirth: string; // "YYYY-MM-DD"
  gender: string;
  salary: number;
  hireDate: string; // "YYYY-MM-DD"
  jobStage: string;
  designation: string;
  managerEmployeeId: number | null;
}

interface ApiResponse {
  status: string;
  message: string;
  data: Employee[];
}

export async function fetchEmployees(
  page: Number,
  size: Number
): Promise<ApiResponse> {
  const token = localStorage.getItem("token");
  const response = await fetch(
    `/api/v1/employees/fetchEmployees?page=${page}&size=${size}`,
    {
      method: "GET",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    }
  );

  let data;
  try {
    data = await response.json();
  } catch {
    data = null;
  }

  if (!response.ok) {
    // Throw raw message — let global handler smoothen it
    const rawMessage = data?.message || "Network error: Unable to reach server";
    throw new Error(rawMessage);
  }

  return data; // success
}
