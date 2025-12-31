// Authenticating the user
export async function authenticateUser(userName: string, password: string) {
  const response = await fetch(`/api/v1/authenticates/authenticate`, {
    method: "POST",
    body: JSON.stringify({
      // Convert JS object to JSON string
      userName: userName,
      password: password,
    }),
    headers: {
      "Content-type": "application/json; charset=UTF-8", // Sending JSON
    },
  });

  if (!response.ok) {
    throw new Error("unable to authenticate for " + userName);
  }

  return await response.json(); // Returns the data directly
}
