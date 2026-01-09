//Global Error and exception handling and appropriate message generating class for the application

export function handleApiError(error: any): string {
  // Log the full error for debugging (you'll see it in console)
  console.error("API Error:", error.message);

  // Default fallback message
  let userMessage = "An unexpected error occurred. Please try again.";

  const apiMessage = error.message.toLowerCase();
  //checking for invalid credentials
  if (
    apiMessage.includes("invalid username ") ||
    apiMessage.includes("invalid password") ||
    apiMessage.includes("unauthorized")
  ) {
    userMessage = "Invalid username or password. Please try again.";
  } else if (
    //checking for network issues
    apiMessage.includes("network error") ||
    apiMessage.includes("failed to fetch")
  ) {
    userMessage = "Network error. Check your internet connection.";
  } else if (error?.status === 500) {
    userMessage = "Server error. Please try later.";
  }
  return userMessage;
}
