// Fetch reponse structure for Operation Summary of Create, Update, Delete operations.
interface OperationSummary {
  totalRequested: number;
  successCount: number;
  errorCount: number;
  operationDetails: Record<number, string>; //The summary of operation of each data entry if they failed.
}

// Fetch response structure for System errors like authentication error, Database errors, etc.
interface ErrorDetails {
  errorCode: string;
  errorMessage: string;
}

// Generic paginated response for any table
interface PaginatedTableData {
  content: any[]; // array of data (Employee, Department, etc.)
  pageable: {
    pageNumber: number;
    pageSize: number;
    sort: {
      empty: boolean;
      unsorted: boolean;
      sorted: boolean;
    };
    offset: number;
    unpaged: boolean;
    paged: boolean;
  };
  totalElements: number;
  totalPages: number;
  last: boolean;
  first: boolean;
  size: number;
  number: number;
  sort: {
    empty: boolean;
    unsorted: boolean;
    sorted: boolean;
  };
  numberOfElements: number;
  empty: boolean;
}

interface ApiResponse {
  data: OperationSummary | ErrorDetails | PaginatedTableData;
}

export async function fetchResponse(
  page: Number,
  size: Number,
): Promise<ApiResponse> {
  const token = localStorage.getItem("token");
  if (!token)
    throw new Error("Authentication token not found. Please log in again.");

  const response = await fetch(`/api/v1/employees?page=${page}&size=${size}`, {
    method: "GET",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });
  if (!response.ok) {
    throw new Error(`HTTP error ${response.status} — ${response.statusText}`);
  }

  const json = await response.json();

  // Very important safety check — helps during development
  if (!json || typeof json !== "object" || !("data" in json)) {
    throw new Error("Invalid response format — missing 'data' key");
  }

  return json as ApiResponse;
}
