/**
 * Takes a sample data object (first row) and generates a map of
 * original keys → human-readable header strings
 *
 * Example:
 * Input:  { employeeId: 1, firstName: "Eureka", managerEmployeeId: 5 }
 * Output: { employeeId: "Employee Id", firstName: "First Name", managerEmployeeId: "Manager Employee Id" }
 */
export function extractHeadersData(
  sample: Record<string, any>
): Record<string, string> {
  const headerMap: Record<string, string> = {};

  Object.keys(sample).forEach((key) => {
    const header = key
      // Insert space before uppercase letters (except first char)
      .replace(/([a-z0-9])([A-Z])/g, "$1 $2")
      // Split → title case each word → join
      .split(/\s+/)
      .map((word) =>
        word.toLowerCase() === "id"
          ? "Id"
          : word.charAt(0).toUpperCase() + word.slice(1).toLowerCase()
      )
      .join(" ");

    headerMap[key] = header;
  });

  return headerMap;
}
