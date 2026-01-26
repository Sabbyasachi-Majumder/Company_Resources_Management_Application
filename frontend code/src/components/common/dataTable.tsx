import { useEffect, useState } from "react";
import { getDataCalls } from "@/api/getData";
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

import { PaginationComponent } from "./paginationComponent";

// ────────────────────────────────────────────────
// Pure reusable table (only displays data + columns name)
// ────────────────────────────────────────────────

interface PureTableProps<TData> {
  columns: ColumnDef<TData>[];
  data: TData[];
}

function PureTable<TData>({ columns, data }: PureTableProps<TData>) {
  const [sorting, setSorting] = useState<SortingState>([]); //not using right now properly

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
                        header.getContext(),
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
// Main component – fetches entities & uses dynamic headers
// ────────────────────────────────────────────────

interface DataTableProps {
  serviceName: string;
  // Add more props later, e.g. data?: YourDataType[], columns, etc.
}

export default function DataTable({ serviceName }: DataTableProps) {
  const [entities, setEntities] = useState<any[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [headerMap, setHeaderMap] = useState<Record<string, string>>({});

  useEffect(() => {
    console.log("Fetching Entities Data...");

    const loadData = async () => {
      try {
        setIsLoading(true);
        setError(null);

        const result = await getDataCalls(serviceName, "", 1, 15);
        console.log(result);

        const data = result.data || [];
        setEntities(data.content);

        if (data.content.length > 0) {
          const headers = extractHeadersData(data.content[0]);
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
    }),
  );

  if (isLoading) {
    return (
      <div className="p-8 text-center text-gray-600">Loading entities...</div>
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
      <h2 className="text-2xl font-bold mb-6">{serviceName} List</h2>
      <div className="pagination-component">
        <PaginationComponent />
      </div>
      {entities.length === 0 ? (
        <p className="text-gray-500 text-center py-12">
          No entities found in the database.
        </p>
      ) : (
        <PureTable columns={columns} data={entities} />
      )}
    </div>
  );
}
