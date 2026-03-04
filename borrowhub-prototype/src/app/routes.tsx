import { createBrowserRouter, Navigate } from "react-router";
import { LoginScreen } from "./screens/LoginScreen";
import { DashboardScreen } from "./screens/DashboardScreen";
import { InventoryScreen } from "./screens/InventoryScreen";
import { TransactionScreen } from "./screens/TransactionScreen";
import { TransactionLogsScreen } from "./screens/TransactionLogsScreen";
import { AccountSettingsScreen } from "./screens/AccountSettingsScreen";
import { UserManagementScreen } from "./screens/UserManagementScreen";
import { MainLayout } from "./components/MainLayout";

export const router = createBrowserRouter([
  {
    path: "/",
    Component: LoginScreen,
  },
  {
    path: "/app",
    Component: MainLayout,
    children: [
      { index: true, Component: DashboardScreen },
      { path: "inventory", Component: InventoryScreen },
      { path: "transaction", Component: TransactionScreen },
      { path: "logs", Component: TransactionLogsScreen },
      { path: "settings", Component: AccountSettingsScreen },
      { path: "users", Component: UserManagementScreen },
      { path: "*", element: <Navigate to="/app" replace /> },
    ],
  },
  {
    path: "*",
    element: <Navigate to="/" replace />,
  },
]);