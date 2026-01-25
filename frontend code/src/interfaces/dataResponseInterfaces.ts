// Fetch reponse structure for Operation Summary of Create, Update, Delete operations.
export interface OperationSummary {
  totalRequested: number;
  successCount: number;
  errorCount: number;
  operationDetails: Record<number, string>; //The summary of operation of each data entry if they failed.
}

// Fetch response structure for System errors like authentication error, Database errors, etc.
export interface ErrorDetails {
  errorCode: string;
  errorMessage: string;
}

// Generic paginated response for any table
export interface PaginatedTableData {
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

export interface ApiResponse {
  data: OperationSummary | ErrorDetails | PaginatedTableData;
}
