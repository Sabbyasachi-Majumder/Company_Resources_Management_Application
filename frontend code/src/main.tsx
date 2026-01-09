import { StrictMode } from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import LoginPage from "./components/Login Page/loginPage.tsx";
import HomePage from "./components/HomePage/homePage.tsx";
import DummyPage from "./components/utility-functions/dummy placeholder react page.tsx";

const router = createBrowserRouter([
  {
    path: "/",
    element: <LoginPage />, // Default route: show Login first
  },
  {
    path: "/home",
    element: <HomePage />,
  },
  {
    path: "/employees",
    element: <DummyPage />, // Default route: show Login first
  },
  {
    path: "/departments",
    element: <DummyPage />,
  },
  {
    path: "/projects",
    element: <DummyPage />, // Default route: show Login first
  },
  // Add more routes here
]);

ReactDOM.createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>
);
