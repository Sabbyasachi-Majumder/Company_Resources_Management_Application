// Authenticating the user

//the structure of the api response from the backend
interface AuthResponse {
  status: string;
  message: string;
  data: {
    token: string;
    refreshToken: string;
  };
}

export async function authenticateUser(
  userName: string,
  password: string
): Promise<AuthResponse> {
  const response = await fetch(`/api/v1/authenticates/authenticate`, {
    method: "POST",
    body: JSON.stringify({ userName, password }),
    headers: {
      "Content-type": "application/json; charset=UTF-8", // Sending JSON
    },
  });

  let data;
  try {
    data = await response.json();
  } catch {
    data = null;
  }

  if (!response.ok) {
    // Throw raw message — let global handler smoothen it
    const rawMessage = data?.message || "Network error: Unable to reach server";
    throw new Error(rawMessage);
  }

  return data; // success
}
