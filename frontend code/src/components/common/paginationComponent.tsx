import {
  Pagination,
  PaginationContent,
  PaginationFirst,
  PaginationItem,
  PaginationNext,
  PaginationLast,
  PaginationPrevious,
} from "@/components/ui/pagination";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

interface PaginationProps {
  currentPage: number;
  totalPages: number;
  pageSize: number;
  isFirstPage: Boolean;
  isLastPage: Boolean;
  onPageChange: (page: number) => void; //callback functions from parent class
  onPageSizeChange: (size: number) => void;
}

export function PaginationComponent({
  currentPage,
  totalPages,
  pageSize,
  isFirstPage,
  isLastPage,
  onPageChange,
  onPageSizeChange,
}: PaginationProps) {
  return (
    <div className="flex items-center justify-between gap-5">
      <Pagination className="mx-0 w-auto">
        <PaginationContent>
          {/* No of Elements out of Total display board */}
          <PaginationItem>
            <div className="flex items-center gap-2 pr-4">
              <span className="text-sm text-muted-foreground whitespace-nowrap">
                Showing {1} to {50} out of {totalPages}
              </span>
            </div>
          </PaginationItem>

          {/* Rows per page selector */}
          <PaginationItem>
            <div className="flex items-center gap-2">
              <span className="text-sm text-muted-foreground whitespace-nowrap">
                Rows per page
              </span>
              <Select
                value={String(pageSize)}
                onValueChange={(value) => {
                  const newSize = Number(value);
                  onPageSizeChange(newSize); // ← calls parent's function
                }}
                defaultValue="10"
              >
                <SelectTrigger className="w-20" id="select-rows-per-page">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent align="start">
                  <SelectGroup>
                    <SelectItem value="10">10</SelectItem>
                    <SelectItem value="25">25</SelectItem>
                    <SelectItem value="100">100</SelectItem>
                  </SelectGroup>
                </SelectContent>
              </Select>
            </div>
          </PaginationItem>

          {/* First & Previous */}
          <PaginationItem>
            <PaginationFirst
              href="#"
              aria-disabled={currentPage === 1}
              className={
                currentPage === 1 ? "pointer-events-none opacity-50" : ""
              }
              onClick={() => currentPage > 1 && onPageChange(1)}
            />

            <PaginationPrevious
              href="#"
              aria-disabled={currentPage === 1}
              className={
                currentPage === 1 ? "pointer-events-none opacity-50" : ""
              }
              onClick={() => currentPage > 1 && onPageChange(currentPage - 1)}
            />
          </PaginationItem>

          {/* Current page / Page number selector */}
          <PaginationItem>
            <div className="flex items-center gap-2">
              <span className="text-sm text-muted-foreground whitespace-nowrap">
                Page Number
              </span>
              <Select
                value={String(currentPage)} // ← this makes it controlled
                onValueChange={(value) => {
                  const newPage = Number(value);
                  if (newPage >= 1 && newPage <= totalPages) {
                    // optional safety
                    onPageChange(newPage);
                  }
                }}
              >
                <SelectTrigger className="w-20" id="select-page-number">
                  <SelectValue /> {/* no need for placeholder here */}
                </SelectTrigger>

                <SelectContent align="start">
                  {Array.from(
                    { length: totalPages },
                    (_, index) => index + 1,
                  ).map((page) => (
                    <SelectItem key={page} value={String(page)}>
                      {page}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </PaginationItem>

          {/* Next & Last */}
          <PaginationItem>
            {/* Next button */}
            <PaginationNext
              aria-disabled={currentPage >= totalPages}
              tabIndex={currentPage >= totalPages ? -1 : undefined}
              className={
                currentPage >= totalPages
                  ? "pointer-events-none opacity-50"
                  : ""
              }
              onClick={(e) => {
                if (currentPage < totalPages) {
                  onPageChange(currentPage + 1);
                } else {
                  e.preventDefault(); // extra safety
                }
              }}
            />

            {/* Last button */}
            <PaginationLast
              aria-disabled={currentPage >= totalPages}
              tabIndex={currentPage >= totalPages ? -1 : undefined}
              className={
                currentPage >= totalPages
                  ? "pointer-events-none opacity-50"
                  : ""
              }
              onClick={(e) => {
                if (currentPage < totalPages) {
                  onPageChange(totalPages);
                } else {
                  e.preventDefault();
                }
              }}
            />
          </PaginationItem>
        </PaginationContent>
      </Pagination>
    </div>
  );
}
