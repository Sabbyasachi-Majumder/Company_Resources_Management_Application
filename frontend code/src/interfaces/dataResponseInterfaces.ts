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

export interface ColumnMetadata {
  name: string; // e.g. "employeeId", "firstName", "gender"
  label: string; // e.g. "Employee ID", "First Name", "Gender"
  uiType: "string" | "number" | "date"; // main data type
  inputType: "text" | "number" | "date" | "select"; // recommended input component
  required: boolean;
  editable: boolean; // whether field becomes editable in edit mode
  sortable: boolean;
  filterable: boolean;
  visibleInTable: boolean;
  visibleInForm: boolean;
  order: number; // sort order for columns
  validation: {
    required: boolean;
    minValue: number | null;
    maxValue: number | null;
    message: string;
  } | null; // null if no validation rules
  // If you already added options for gender:
  options?: string[]; // optional - only present on fields like gender
}

export interface ApiResponse {
  data: OperationSummary | ErrorDetails | PaginatedTableData | ColumnMetadata[];
}
