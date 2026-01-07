import { StrictMode } from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App.tsx";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import LoginPage from "./components/Login Page/loginPage.tsx";
import HomePage from "./components/HomePage/homePage.tsx";

const router = createBrowserRouter([
  {
    path: "/",
    element: <LoginPage />, // Default route: show Login first
  },
  {
    path: "/home",
    element: <HomePage />,
  },
  // You can add more routes here later!
]);

ReactDOM.createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>
);
