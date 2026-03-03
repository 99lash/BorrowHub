import { Outlet, useNavigate, useLocation } from "react-router";
import { 
  Home, 
  Package, 
  ArrowLeftRight,
  FileText,
  LogOut
} from "lucide-react";
import { Button } from "./ui/button";
import logo from "../../assets/logo.png";
import { toast } from "sonner";

const navigation = [
  { name: "Home", path: "/app", icon: Home },
  { name: "Inventory", path: "/app/inventory", icon: Package },
  { name: "Transaction", path: "/app/transaction", icon: ArrowLeftRight },
  { name: "Logs", path: "/app/logs", icon: FileText },
];

export function MainLayout() {
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    navigate("/");
  };

  const isActive = (path: string) => {
    if (path === "/app") {
      return location.pathname === "/app";
    }
    return location.pathname.startsWith(path);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 via-white to-gray-50 pb-20">
      {/* Premium Header with Glass Effect */}
      <header className="bg-white/80 backdrop-blur-xl border-b border-gray-200/80 sticky top-0 z-50 shadow-sm">
        <div className="px-4 sm:px-6 h-16 flex items-center justify-between max-w-7xl mx-auto">
          {/* Logo Section */}
          <div className="flex items-center gap-3">
            <div className="relative">
              <div className="absolute inset-0 bg-[#DC143C]/20 blur-lg rounded-full"></div>
              <img src={logo} alt="BorrowHub" className="w-9 h-9 relative z-10" />
            </div>
            <span className="text-lg font-semibold text-gray-900 tracking-tight">BorrowHub</span>
          </div>
          
          {/* Logout Button */}
          <Button
            variant="ghost"
            size="sm"
            onClick={handleLogout}
            className="text-gray-600 hover:text-gray-900 hover:bg-gray-100 h-9 px-3 rounded-lg transition-all"
          >
            <LogOut className="w-4 h-4 mr-2" />
            <span className="hidden sm:inline text-sm">Logout</span>
          </Button>
        </div>
      </header>

      {/* Main Content */}
      <main className="px-4 sm:px-6 py-6 max-w-7xl mx-auto">
        <Outlet />
      </main>

      {/* Premium Bottom Navigation */}
      <nav className="fixed bottom-0 left-0 right-0 bg-white/95 backdrop-blur-xl border-t border-gray-200/80 z-50 shadow-lg">
        <div className="flex items-center justify-around px-2 py-2 max-w-md mx-auto">
          {navigation.map((item) => {
            const Icon = item.icon;
            const active = isActive(item.path);
            return (
              <button
                key={item.path}
                onClick={() => navigate(item.path)}
                className={`relative flex flex-col items-center gap-1.5 px-4 py-2.5 rounded-xl transition-all duration-200 min-w-[72px] group ${
                  active
                    ? "text-[#DC143C]"
                    : "text-gray-500 hover:text-gray-700"
                }`}
              >
                {/* Active Indicator */}
                {active && (
                  <div className="absolute inset-0 bg-gradient-to-br from-[#DC143C]/10 to-[#B91130]/10 rounded-xl"></div>
                )}
                
                {/* Icon */}
                <div className="relative">
                  <Icon className={`w-5 h-5 transition-transform duration-200 ${
                    active ? "scale-110" : "group-hover:scale-105"
                  }`} />
                  {active && (
                    <div className="absolute inset-0 bg-[#DC143C]/20 blur-md rounded-full"></div>
                  )}
                </div>
                
                {/* Label */}
                <span className={`text-xs font-medium relative ${
                  active ? "font-semibold" : ""
                }`}>
                  {item.name}
                </span>
              </button>
            );
          })}
        </div>
      </nav>
    </div>
  );
}