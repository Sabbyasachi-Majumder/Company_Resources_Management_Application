// dataTable.tsx
// Currently hardcoded for employees, but headers are dynamic

import { useEffect, useState } from "react";
import { fetchEmployees } from "@/api/employeesApi";
import { extractHeadersData } from "../utility-functions/extractHeadersData";

import type { ColumnDef } from "@tanstack/react-table";
import {
  flexRender,
  getCoreRowModel,
  getSortedRowModel,
  type SortingState,
  useReactTable,
} from "@tanstack/react-table";

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

// ────────────────────────────────────────────────
// Pure reusable table (only displays data + columns)
// ────────────────────────────────────────────────

interface PureTableProps<TData> {
  columns: ColumnDef<TData>[];
  data: TData[];
}

function PureTable<TData>({ columns, data }: PureTableProps<TData>) {
  const [sorting, setSorting] = useState<SortingState>([]);

  const table = useReactTable({
    data,
    columns,
    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
    state: { sorting },
    onSortingChange: setSorting,
  });

  return (
    <div className="rounded-md border">
      <Table>
        <TableHeader>
          {table.getHeaderGroups().map((headerGroup) => (
            <TableRow key={headerGroup.id}>
              {headerGroup.headers.map((header) => (
                <TableHead key={header.id} className="text-left">
                  {header.isPlaceholder
                    ? null
                    : flexRender(
                        header.column.columnDef.header,
                        header.getContext()
                      )}
                </TableHead>
              ))}
            </TableRow>
          ))}
        </TableHeader>

        <TableBody>
          {table.getRowModel().rows?.length ? (
            table.getRowModel().rows.map((row) => (
              <TableRow key={row.id}>
                {row.getVisibleCells().map((cell) => (
                  <TableCell key={cell.id}>
                    {flexRender(cell.column.columnDef.cell, cell.getContext())}
                  </TableCell>
                ))}
              </TableRow>
            ))
          ) : (
            <TableRow>
              <TableCell colSpan={columns.length} className="h-24 text-center">
                No results.
              </TableCell>
            </TableRow>
          )}
        </TableBody>
      </Table>
    </div>
  );
}

// ────────────────────────────────────────────────
// Main component – fetches employees & uses dynamic headers
// ────────────────────────────────────────────────

export default function EmployeeTable() {
  const [employees, setEmployees] = useState<any[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [headerMap, setHeaderMap] = useState<Record<string, string>>({});

  useEffect(() => {
    console.log("Fetching Employees Data...");

    const loadData = async () => {
      try {
        setIsLoading(true);
        setError(null);

        const result = await fetchEmployees(1, 10);

        if (result.status !== "success") {
          throw new Error(result.message || "API returned non-success status");
        }

        const data = result.data || [];
        setEmployees(data);

        if (data.length > 0) {
          const headers = extractHeadersData(data[0]);
          setHeaderMap(headers);
          console.log("Generated headers:", headers);
        }
      } catch (err) {
        const message = err instanceof Error ? err.message : "Unknown error";
        setError(message);
        console.error("Fetch failed:", err);
      } finally {
        setIsLoading(false);
      }
    };

    loadData();
  }, []);

  // Generate columns from dynamic headers
  const columns: ColumnDef<any>[] = Object.entries(headerMap).map(
    ([key, headerText]) => ({
      accessorKey: key,
      header: headerText,
    })
  );

  if (isLoading) {
    return (
      <div className="p-8 text-center text-gray-600">Loading employees...</div>
    );
  }

  if (error) {
    return (
      <div className="p-8 text-center text-red-600">
        Error: {error}
        <br />
        <button
          onClick={() => window.location.reload()}
          className="mt-4 px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
        >
          Retry
        </button>
      </div>
    );
  }

  return (
    <div className="p-6 max-w-7xl mx-auto">
      <h2 className="text-2xl font-bold mb-6">Employee List</h2>

      {employees.length === 0 ? (
        <p className="text-gray-500 text-center py-12">
          No employees found in the database.
        </p>
      ) : (
        <PureTable columns={columns} data={employees} />
      )}
    </div>
  );
}
