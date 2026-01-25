import type { ApiResponse } from "./../interfaces/dataResponseInterfaces";

export async function fetchResponse(
  page: Number,
  size: Number,
): Promise<ApiResponse> {
  const token = localStorage.getItem("token");
  if (!token)
    throw new Error("Authentication token not found. Please log in again.");

  const response = await fetch(`/api/v1/employees?page=${page}&size=${size}`, {
    method: "GET",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });
  if (!response.ok) {
    throw new Error(`HTTP error ${response.status} — ${response.statusText}`);
  }

  const json = await response.json();

  // Very important safety check — helps during development
  if (!json || typeof json !== "object" || !("data" in json)) {
    throw new Error("Invalid response format — missing 'data' key");
  }

  return json as ApiResponse;
}
