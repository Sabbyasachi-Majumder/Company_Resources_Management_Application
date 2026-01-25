import type { ApiResponse } from "./../interfaces/dataResponseInterfaces";

export async function getDataCalls(
  serviceName: string,
  extraPath: string = "",
  pageNo: number = 1,
  size: number = 10,
): Promise<ApiResponse> {
  const token = localStorage.getItem("token");
  if (!token) throw new Error("Authentication token not found");

  //building the proper path
  let finalPath = `/api/v1/${serviceName}`;
  if (extraPath === "")
    finalPath += `?page=${pageNo}&size=${size}`; //paginated page fetch
  else finalPath += extraPath; //else its fetching specific data

  const response = await fetch(finalPath, {
    method: "GET",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });
  if (!response.ok) {
    throw new Error(
      `HTTP error for ${serviceName} fetch: ${response.status} — ${response.statusText}`,
    );
  }

  const json = await response.json();

  // Very important safety check — helps during development
  if (!json?.data) {
    throw new Error("Invalid response format — missing 'data' key");
  }

  return json as ApiResponse;
}
