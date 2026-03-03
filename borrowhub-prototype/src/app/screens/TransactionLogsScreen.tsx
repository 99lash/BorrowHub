import { useState } from "react";
import { Card } from "../components/ui/card";
import { Input } from "../components/ui/input";
import { Button } from "../components/ui/button";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../components/ui/select";
import { Search, Download, ArrowLeft } from "lucide-react";
import { useNavigate } from "react-router";
import { FileText } from "lucide-react";

interface Transaction {
  id: string;
  userId: string;
  userName: string;
  action: "Borrowed" | "Returned" | "Added" | "Updated";
  item: string;
  dateTime: string;
  performedBy: string;
}

const transactionData: Transaction[] = [
  {
    id: "1",
    userId: "EMP2045",
    userName: "Sarah Chen",
    action: "Borrowed",
    item: "Projector - Epson EB-X41",
    dateTime: "Feb 18, 2026 09:15 AM",
    performedBy: "Admin (John Smith)",
  },
  {
    id: "2",
    userId: "EMP1892",
    userName: "James Miller",
    action: "Returned",
    item: "Laptop - Dell XPS 15",
    dateTime: "Feb 18, 2026 08:45 AM",
    performedBy: "Admin (John Smith)",
  },
  {
    id: "3",
    userId: "STU4521",
    userName: "Emily Rodriguez",
    action: "Borrowed",
    item: "Camera - Canon EOS R6",
    dateTime: "Feb 17, 2026 04:30 PM",
    performedBy: "Staff (Maria Garcia)",
  },
  {
    id: "4",
    userId: "EMP3107",
    userName: "David Kim",
    action: "Returned",
    item: "Whiteboard Marker Set",
    dateTime: "Feb 17, 2026 02:20 PM",
    performedBy: "Staff (Maria Garcia)",
  },
  {
    id: "5",
    userId: "STU3891",
    userName: "Lisa Thompson",
    action: "Borrowed",
    item: "Conference Room Key - B305",
    dateTime: "Feb 17, 2026 11:00 AM",
    performedBy: "Admin (John Smith)",
  },
  {
    id: "6",
    userId: "SYSTEM",
    userName: "System",
    action: "Added",
    item: "HDMI Cable 5m (Qty: 25)",
    dateTime: "Feb 16, 2026 03:15 PM",
    performedBy: "Admin (John Smith)",
  },
  {
    id: "7",
    userId: "EMP1523",
    userName: "Michael Brown",
    action: "Borrowed",
    item: "Wireless Presenter",
    dateTime: "Feb 15, 2026 10:30 AM",
    performedBy: "Staff (Maria Garcia)",
  },
  {
    id: "8",
    userId: "STU2341",
    userName: "Jessica Lee",
    action: "Returned",
    item: "Extension Cable 10m",
    dateTime: "Feb 15, 2026 09:00 AM",
    performedBy: "Staff (Maria Garcia)",
  },
  {
    id: "9",
    userId: "SYSTEM",
    userName: "System",
    action: "Updated",
    item: "Portable Speaker (Status: Maintenance)",
    dateTime: "Feb 14, 2026 04:45 PM",
    performedBy: "Admin (John Smith)",
  },
  {
    id: "10",
    userId: "EMP4567",
    userName: "Robert Wilson",
    action: "Borrowed",
    item: "Tripod Stand",
    dateTime: "Feb 14, 2026 01:20 PM",
    performedBy: "Admin (John Smith)",
  },
];

export function TransactionLogsScreen() {
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState("");
  const [actionFilter, setActionFilter] = useState("all");

  const filteredData = transactionData.filter((transaction) => {
    const matchesSearch =
      transaction.userName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      transaction.userId.toLowerCase().includes(searchQuery.toLowerCase()) ||
      transaction.item.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesAction = actionFilter === "all" || transaction.action === actionFilter;
    return matchesSearch && matchesAction;
  });

  const getActionColor = (action: string) => {
    switch (action) {
      case "Borrowed":
        return "bg-amber-50 text-amber-700";
      case "Returned":
        return "bg-green-50 text-green-700";
      case "Added":
        return "bg-blue-50 text-blue-700";
      case "Updated":
        return "bg-slate-100 text-slate-700";
      default:
        return "bg-slate-50 text-slate-700";
    }
  };

  return (
    <div className="max-w-6xl mx-auto space-y-6 animate-fade-in">
      {/* Premium Header */}
      <div className="flex items-start justify-between gap-4 flex-wrap">
        <div className="flex items-center gap-4">
          <Button
            variant="ghost"
            size="sm"
            onClick={() => navigate("/app")}
            className="text-gray-600 hover:text-gray-900 hover:bg-gray-100 -ml-2 rounded-lg"
          >
            <ArrowLeft className="w-4 h-4" />
          </Button>
          <div>
            <h1 className="text-3xl font-bold text-gray-900 mb-2 tracking-tight">Transaction Logs</h1>
            <p className="text-gray-500 text-base">Complete audit trail of all activities</p>
          </div>
        </div>
        <Button
          variant="outline"
          size="sm"
          className="border-gray-300 hover:bg-gray-100 rounded-lg h-10 px-4 font-medium"
        >
          <Download className="w-4 h-4 mr-2" />
          Export
        </Button>
      </div>

      {/* Premium Filters Card */}
      <Card className="p-5 border-gray-200/60 shadow-sm hover:shadow-premium transition-all duration-300 rounded-2xl bg-white/80 backdrop-blur-sm">
        <div className="flex flex-col sm:flex-row gap-4">
          <div className="flex-1 relative">
            <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
            <Input
              placeholder="Search by user, ID, or item..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="pl-11 h-11 border-gray-200 bg-gray-50 focus:bg-white rounded-xl transition-all"
            />
          </div>
          <Select value={actionFilter} onValueChange={setActionFilter}>
            <SelectTrigger className="sm:w-52 h-11 border-gray-200 bg-gray-50 rounded-xl">
              <SelectValue placeholder="Filter by action" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">All Actions</SelectItem>
              <SelectItem value="Borrowed">Borrowed</SelectItem>
              <SelectItem value="Returned">Returned</SelectItem>
              <SelectItem value="Added">Added</SelectItem>
              <SelectItem value="Updated">Updated</SelectItem>
            </SelectContent>
          </Select>
        </div>
      </Card>

      {/* Premium Table Card */}
      <Card className="border-gray-200/60 overflow-hidden shadow-sm hover:shadow-premium transition-all duration-300 rounded-2xl bg-white/80 backdrop-blur-sm">
        {/* Desktop Table */}
        <div className="hidden lg:block overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gradient-to-r from-gray-50 to-gray-100 border-b border-gray-200">
              <tr>
                <th className="text-left px-5 py-4 text-sm font-semibold text-gray-700">User</th>
                <th className="text-left px-5 py-4 text-sm font-semibold text-gray-700">Action</th>
                <th className="text-left px-5 py-4 text-sm font-semibold text-gray-700">Item</th>
                <th className="text-left px-5 py-4 text-sm font-semibold text-gray-700">Date & Time</th>
                <th className="text-left px-5 py-4 text-sm font-semibold text-gray-700">Performed By</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {filteredData.map((transaction) => (
                <tr key={transaction.id} className="hover:bg-gray-50/80 transition-all duration-200 group">
                  <td className="px-5 py-4">
                    <div className="text-sm font-medium text-gray-900">{transaction.userName}</div>
                    <div className="text-xs text-gray-500 font-medium">{transaction.userId}</div>
                  </td>
                  <td className="px-5 py-4">
                    <span
                      className={`inline-flex items-center px-3 py-1.5 rounded-lg text-xs font-medium ${getActionColor(
                        transaction.action
                      )}`}
                    >
                      {transaction.action}
                    </span>
                  </td>
                  <td className="px-5 py-4 text-sm font-medium text-gray-900">{transaction.item}</td>
                  <td className="px-5 py-4 text-sm text-gray-600 font-medium">{transaction.dateTime}</td>
                  <td className="px-5 py-4 text-sm text-gray-600 font-medium">{transaction.performedBy}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Mobile/Tablet Cards */}
        <div className="lg:hidden divide-y divide-gray-100">
          {filteredData.map((transaction) => (
            <div key={transaction.id} className="p-5 hover:bg-gray-50/80 transition-all duration-200">
              <div className="flex items-start justify-between gap-3 mb-3">
                <div className="flex-1 min-w-0">
                  <div className="text-sm font-semibold text-gray-900 mb-1">{transaction.userName}</div>
                  <div className="text-xs text-gray-500 font-medium">{transaction.userId}</div>
                </div>
                <span
                  className={`inline-flex items-center px-3 py-1.5 rounded-lg text-xs font-medium ${getActionColor(
                    transaction.action
                  )}`}
                >
                  {transaction.action}
                </span>
              </div>
              <div className="text-sm font-medium text-gray-900 mb-3">{transaction.item}</div>
              <div className="flex items-center gap-2 text-xs text-gray-500 font-medium">
                <span>{transaction.dateTime}</span>
                <span>•</span>
                <span>{transaction.performedBy}</span>
              </div>
            </div>
          ))}
        </div>

        {filteredData.length === 0 && (
          <div className="p-16 text-center">
            <div className="w-16 h-16 bg-gray-100 rounded-2xl flex items-center justify-center mx-auto mb-4">
              <FileText className="w-8 h-8 text-gray-400" />
            </div>
            <p className="text-gray-500 font-medium">No transactions found matching your criteria.</p>
          </div>
        )}
      </Card>

      {/* Results Count */}
      <div className="flex items-center justify-between text-sm text-gray-500 px-1">
        <span className="font-medium">Showing {filteredData.length} of {transactionData.length} transactions</span>
      </div>
    </div>
  );
}