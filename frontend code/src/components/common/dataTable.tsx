import { cn } from "@/lib/utils";

import type { ColumnDef } from "@tanstack/react-table";
import {
  flexRender,
  getCoreRowModel,
  useReactTable,
} from "@tanstack/react-table";
import { Triangle } from "lucide-react"; //for sorting icons

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

// ───────────────────────────────────────────────────────────────────────────────────────────────────────────────
// displays data + columns name and accepts the sorting changes to transfer to the main component for refetching the data
// ───────────────────────────────────────────────────────────────────────────────────────────────────────────────

interface PureTableProps<TData> {
  columns: ColumnDef<TData>[];
  data: TData[];
  sortedByColumnName: string;
  sortOrder: string;
}

export default function PureTable<TData>({
  columns,
  data,
  sortedByColumnName,
  sortOrder,
}: PureTableProps<TData>) {
  const table = useReactTable({
    data,
    columns,
    getCoreRowModel: getCoreRowModel(),
  });

  return (
    <div className="rounded-md border">
      <Table>
        <TableHeader>
          {table.getHeaderGroups().map((headerGroup) => (
            <TableRow key={headerGroup.id}>
              {headerGroup.headers.map((header) => {
                return (
                  <TableHead
                    key={header.id}
                    className="text-left whitespace-nowrap"
                  >
                    <div className="flex items-center gap-1">
                      <span className="truncate">
                        {header.isPlaceholder
                          ? null
                          : flexRender(
                              header.column.columnDef.header,
                              header.getContext(),
                            )}
                      </span>
                      {/* Sort Icons */}
                      {header.column.id === sortedByColumnName ? (
                        <Triangle
                          className={cn(
                            "h-3 w-3 shrink-0 transition-transform",
                            // Is this column the currently sorted one?
                            header.column.id === sortedByColumnName
                              ? sortOrder === "asc"
                                ? "text-foreground" // solid black/up
                                : "text-foreground rotate-180" // solid black/down
                              : "text-muted-foreground opacity-100", // faint grey for others
                          )}
                        />
                      ) : (
                        ""
                      )}
                    </div>
                  </TableHead>
                );
              })}
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
