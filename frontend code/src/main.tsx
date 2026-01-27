import { StrictMode } from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import LoginPage from "./components/Login Page/loginPage.tsx";
import HomePage from "./components/HomePage/homePage.tsx";
import DummyPage from "./components/utility-functions/dummy placeholder react page.tsx";
import DataTable from "./components/common/dataTable.tsx";

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
    path: "/authenticates",
    element: <DummyPage />,
  },
  {
    path: "/employees",
    element: <DataTable serviceName="employees" />,
  },
  {
    path: "/departments",
    element: <DummyPage />,
  },
  {
    path: "/projects",
    element: <DummyPage />,
  },
  // Add more routes here
]);

ReactDOM.createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>,
);
