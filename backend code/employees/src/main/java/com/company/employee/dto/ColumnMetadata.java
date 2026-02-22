package com.company.employee.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Metadata for each column in the employee data table (used for rendering and validation)")
public record ColumnMetadata(
        @Schema(description = "Backend field name", example = "firstName")
        String name,

        @Schema(description = "Display label in table header", example = "First Name")
        String label,

        @Schema(description = "Data type for UI rendering", allowableValues = {"string", "number", "date"})
        String uiType,

        @Schema(description = "Recommended input component", allowableValues = {"text", "number", "date", "select"})
        String inputType,

        @Schema(description = "Whether field is required (create/update)")
        boolean required,

        @Schema(description = "Whether field can become editable when row is in edit mode")
        boolean editable,

        @Schema(description = "Whether column supports sorting")
        boolean sortable,

        @Schema(description = "Whether column supports filtering")
        boolean filterable,

        @Schema(description = "Show in data table")
        boolean visibleInTable,

        @Schema(description = "Show in create/update forms")
        boolean visibleInForm,

        @Schema(description = "Display order (lower = appears earlier)")
        Integer order,

        @Schema(description = "Client-side validation rules")
        ValidationMetadata validation,

        @Schema(description = "Options if input type is dropdown or checkbox or multiple choice")
        List<String> options
) {
}

@Schema(description = "Simple client-side validation rules")
record ValidationMetadata(
        @Schema(description = "Field is required")
        boolean required,

        @Schema(description = "Minimum value (numbers) or null")
        Integer minValue,

        @Schema(description = "Maximum value/length or null")
        Integer maxValue,

        @Schema(description = "Error message / tooltip text")
        String message
) {
}