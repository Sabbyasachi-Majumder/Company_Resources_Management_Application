import { useEffect, useState } from "react";
import { getDataCalls } from "@/api/getData";
import headerData from "../utility-functions/dataHeadersLists";

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

  //pagination related state management
  const [totalEntities, setTotalEntities] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [offset, setOffset] = useState(1);

  // These are the callback functions
  const handlePageChange = (newPage: number) => {
    setCurrentPage(newPage);
  };

  const handlePageSizeChange = (newSize: number) => {
    setPageSize(newSize);
    setCurrentPage(1);
  };

  useEffect(() => {
    console.log("Fetching Entities Data...");

    const loadData = async () => {
      try {
        setIsLoading(true);
        setError(null);

        const result = await getDataCalls(
          serviceName,
          "",
          currentPage,
          pageSize,
        );
        console.log(result);

        const data = result.data || [];
        setEntities(data.content);
        setHeaderMap(headerData({ serviceName }));
        setTotalEntities(data.totalElements);
        setCurrentPage(data.pageable.pageNumber + 1);
        setTotalPages(data.totalPages);
        setOffset(data.pageable.offset);
        console.log("Generated headers:", headerMap);
      } catch (err) {
        const message = err instanceof Error ? err.message : "Unknown error";
        setError(message);
        console.error("Fetch failed:", err);
      } finally {
        setIsLoading(false);
      }
    };

    loadData();
  }, [pageSize, currentPage]);

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

  return (
    <div className="p-6 max-w-7xl mx-auto">
      <h2 className="text-2xl font-bold mb-4">
        {serviceName.charAt(0).toUpperCase() +
          serviceName.substring(1) +
          " List"}
      </h2>
      <div className="pb-4 flex justify-end">
        <PaginationComponent
          currentPage={currentPage}
          totalPages={totalPages}
          pageSize={pageSize}
          totalEntities={totalEntities}
          offset={offset}
          onPageChange={handlePageChange}
          onPageSizeChange={handlePageSizeChange}
        />{" "}
      </div>
      <div>
        <PureTable columns={columns} data={entities} />
        {entities.length === 0 ? (
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
        ) : (
          ""
        )}
      </div>
    </div>
  );
}
