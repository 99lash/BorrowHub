import { useNavigate } from "react-router";
import { Card } from "../components/ui/card";
import { Button } from "../components/ui/button";
import { 
  Package, 
  ArrowDownToLine, 
  CheckCircle, 
  Clock,
  ArrowDownToLine as BorrowIcon,
  ArrowUpFromLine as ReturnIcon,
  PackagePlus
} from "lucide-react";

  const stats = [
  {
    title: "Total Items",
    value: "248",
    icon: Package,
    color: "bg-red-50 text-[#DC143C]",
  },
  {
    title: "Currently Borrowed",
    value: "42",
    icon: ArrowDownToLine,
    color: "bg-amber-50 text-amber-600",
  },
  {
    title: "Available Now",
    value: "206",
    icon: CheckCircle,
    color: "bg-green-50 text-green-600",
  },
  {
    title: "Due Today",
    value: "18",
    icon: Clock,
    color: "bg-gray-100 text-gray-700",
  },
];

const recentTransactions = [
  {
    id: "1",
    user: "Sarah Chen",
    userId: "EMP2045",
    action: "Borrowed",
    item: "Projector - Epson EB-X41",
    date: "Feb 18, 2026",
    time: "09:15 AM",
  },
  {
    id: "2",
    user: "James Miller",
    userId: "EMP1892",
    action: "Returned",
    item: "Laptop - Dell XPS 15",
    date: "Feb 18, 2026",
    time: "08:45 AM",
  },
  {
    id: "3",
    user: "Emily Rodriguez",
    userId: "STU4521",
    action: "Borrowed",
    item: "Camera - Canon EOS R6",
    date: "Feb 17, 2026",
    time: "04:30 PM",
  },
  {
    id: "4",
    user: "David Kim",
    userId: "EMP3107",
    action: "Returned",
    item: "Whiteboard Marker Set",
    date: "Feb 17, 2026",
    time: "02:20 PM",
  },
  {
    id: "5",
    user: "Lisa Thompson",
    userId: "STU3891",
    action: "Borrowed",
    item: "Conference Room Key - B305",
    date: "Feb 17, 2026",
    time: "11:00 AM",
  },
];

export function DashboardScreen() {
  const navigate = useNavigate();

  return (
    <div className="max-w-6xl mx-auto space-y-8 animate-fade-in">
      {/* Header Section */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-2 tracking-tight">Dashboard</h1>
        <p className="text-gray-500 text-base">Overview of resource management</p>
      </div>

      {/* Premium Stats Grid */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 lg:gap-5">
        {stats.map((stat, index) => {
          const Icon = stat.icon;
          return (
            <Card 
              key={stat.title} 
              className="group p-5 border-gray-200/60 hover:border-gray-300 hover:shadow-premium transition-all duration-300 bg-white/80 backdrop-blur-sm rounded-2xl animate-scale-in"
              style={{ animationDelay: `${index * 50}ms` }}
            >
              <div className={`w-12 h-12 rounded-xl ${stat.color} flex items-center justify-center mb-4 group-hover:scale-110 transition-transform duration-300`}>
                <Icon className="w-6 h-6" />
              </div>
              <div className="text-3xl font-bold text-gray-900 mb-1.5 tracking-tight">{stat.value}</div>
              <div className="text-sm text-gray-600 font-medium">{stat.title}</div>
            </Card>
          );
        })}
      </div>

      {/* Quick Actions Section */}
      <div>
        <h2 className="text-xl font-semibold text-gray-900 mb-4 tracking-tight">Quick Actions</h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <Button
            onClick={() => navigate("/app/transaction")}
            className="h-auto py-5 px-6 bg-gradient-to-br from-[#DC143C] to-[#B91130] hover:from-[#B91130] hover:to-[#9A0E28] justify-start rounded-xl shadow-lg shadow-[#DC143C]/20 hover:shadow-xl hover:shadow-[#DC143C]/30 transition-all duration-300 group"
          >
            <BorrowIcon className="w-6 h-6 mr-4 group-hover:scale-110 transition-transform" />
            <div className="text-left">
              <div className="font-semibold text-base">Borrow / Return</div>
              <div className="text-sm opacity-90 font-normal">Process transactions</div>
            </div>
          </Button>
          <Button
            onClick={() => navigate("/app/inventory")}
            className="h-auto py-5 px-6 bg-gradient-to-br from-black to-gray-800 hover:from-gray-800 hover:to-gray-900 justify-start rounded-xl shadow-lg shadow-black/20 hover:shadow-xl hover:shadow-black/30 transition-all duration-300 group"
          >
            <PackagePlus className="w-6 h-6 mr-4 group-hover:scale-110 transition-transform" />
            <div className="text-left">
              <div className="font-semibold text-base">Manage Inventory</div>
              <div className="text-sm opacity-90 font-normal">View and edit items</div>
            </div>
          </Button>
        </div>
      </div>

      {/* Recent Transactions Section */}
      <div>
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-xl font-semibold text-gray-900 tracking-tight">Recent Transactions</h2>
          <Button 
            variant="ghost" 
            size="sm"
            onClick={() => navigate("/app/logs")}
            className="text-gray-600 hover:text-gray-900 hover:bg-gray-100 rounded-lg transition-all"
          >
            View All →
          </Button>
        </div>
        <Card className="border-gray-200/60 overflow-hidden shadow-sm hover:shadow-premium transition-all duration-300 rounded-2xl bg-white/80 backdrop-blur-sm">
          <div className="divide-y divide-gray-100">
            {recentTransactions.map((transaction, index) => (
              <div 
                key={transaction.id} 
                className="p-5 hover:bg-gray-50/80 transition-all duration-200 group"
              >
                <div className="flex items-start justify-between gap-4">
                  <div className="flex-1 min-w-0">
                    {/* User Info */}
                    <div className="flex items-center gap-2 mb-2">
                      <span className="text-sm font-semibold text-gray-900">{transaction.user}</span>
                      <span className="text-xs text-gray-400 font-medium">({transaction.userId})</span>
                    </div>
                    
                    {/* Action & Item */}
                    <div className="flex items-center gap-2.5 mb-2">
                      <span
                        className={`inline-flex items-center px-2.5 py-1 rounded-lg text-xs font-medium ${
                          transaction.action === "Borrowed"
                            ? "bg-amber-100 text-amber-700"
                            : "bg-green-100 text-green-700"
                        }`}
                      >
                        {transaction.action}
                      </span>
                      <span className="text-sm text-gray-700 truncate font-medium">{transaction.item}</span>
                    </div>
                    
                    {/* Date & Time */}
                    <div className="text-xs text-gray-500 font-medium">
                      {transaction.date} • {transaction.time}
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </Card>
      </div>
    </div>
  );
}