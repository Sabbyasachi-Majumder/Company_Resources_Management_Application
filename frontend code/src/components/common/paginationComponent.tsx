import { Field, FieldLabel } from "@/components/ui/field";
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

export function PaginationComponent() {
  return (
    <div className="flex items-center justify-between gap-5">
      <Pagination className="mx-0 w-auto">
        {/* First & Previous */}
        <PaginationContent>
          <PaginationItem>
            <PaginationFirst href="#" />
            <PaginationPrevious href="#" />
          </PaginationItem>
          {/* Rows per page selector */}
          <PaginationItem>
            <div className="flex items-center gap-2">
              <span className="text-sm text-muted-foreground whitespace-nowrap">
                Rows per page
              </span>
              <Select defaultValue="10">
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

          {/* Current page / Page number selector */}
          <PaginationItem>
            <div className="flex items-center gap-2">
              <span className="text-sm text-muted-foreground whitespace-nowrap">
                Page Number
              </span>
              <Select defaultValue="1">
                <SelectTrigger className="w-20" id="select-rows-per-page">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent align="start">
                  <SelectGroup>
                    <SelectItem value="1">1</SelectItem>
                    <SelectItem value="2">2</SelectItem>
                    <SelectItem value="3">3</SelectItem>
                  </SelectGroup>
                </SelectContent>
              </Select>
            </div>
          </PaginationItem>

          {/* Next & Last */}
          <PaginationItem>
            <PaginationNext href="#" />
            <PaginationLast href="#" />
          </PaginationItem>
        </PaginationContent>
      </Pagination>
    </div>
  );
}
