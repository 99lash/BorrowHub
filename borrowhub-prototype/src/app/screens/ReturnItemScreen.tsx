import { useState } from "react";
import { Card } from "../components/ui/card";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { ArrowLeft, Search, Check } from "lucide-react";
import { useNavigate } from "react-router";

interface BorrowedItem {
  id: string;
  userId: string;
  userName: string;
  item: string;
  quantity: number;
  borrowDate: string;
  dueDate: string;
  status: "Borrowed" | "Returned";
}

const borrowedItems: BorrowedItem[] = [
  {
    id: "1",
    userId: "EMP2045",
    userName: "Sarah Chen",
    item: "Projector - Epson EB-X41",
    quantity: 1,
    borrowDate: "Feb 18, 2026",
    dueDate: "Feb 25, 2026",
    status: "Borrowed",
  },
  {
    id: "2",
    userId: "STU4521",
    userName: "Emily Rodriguez",
    item: "Camera - Canon EOS R6",
    quantity: 1,
    borrowDate: "Feb 17, 2026",
    dueDate: "Feb 24, 2026",
    status: "Borrowed",
  },
  {
    id: "3",
    userId: "EMP3107",
    userName: "David Kim",
    item: "Laptop - Dell XPS 15",
    quantity: 2,
    borrowDate: "Feb 16, 2026",
    dueDate: "Feb 23, 2026",
    status: "Borrowed",
  },
  {
    id: "4",
    userId: "STU3891",
    userName: "Lisa Thompson",
    item: "Conference Room Key - B305",
    quantity: 1,
    borrowDate: "Feb 17, 2026",
    dueDate: "Feb 24, 2026",
    status: "Borrowed",
  },
  {
    id: "5",
    userId: "EMP1523",
    userName: "Michael Brown",
    item: "Wireless Presenter",
    quantity: 1,
    borrowDate: "Feb 15, 2026",
    dueDate: "Feb 22, 2026",
    status: "Borrowed",
  },
];

export function ReturnItemScreen() {
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState("");
  const [items, setItems] = useState(borrowedItems);
  const [returnedId, setReturnedId] = useState<string | null>(null);

  const filteredItems = items.filter(
    (item) =>
      item.status === "Borrowed" &&
      (item.userName.toLowerCase().includes(searchQuery.toLowerCase()) ||
        item.userId.toLowerCase().includes(searchQuery.toLowerCase()) ||
        item.item.toLowerCase().includes(searchQuery.toLowerCase()))
  );

  const handleReturn = (id: string) => {
    setItems((prev) => prev.map((item) => (item.id === id ? { ...item, status: "Returned" as const } : item)));
    setReturnedId(id);
    setTimeout(() => {
      setReturnedId(null);
    }, 2000);
  };

  return (
    <div className="max-w-4xl mx-auto">
      {/* Header */}
      <div className="flex items-center gap-3 mb-6">
        <Button
          variant="ghost"
          size="sm"
          onClick={() => navigate("/app")}
          className="text-slate-600 -ml-2"
        >
          <ArrowLeft className="w-4 h-4" />
        </Button>
        <div>
          <h1 className="text-2xl text-slate-900 mb-1">Return Item</h1>
          <p className="text-slate-500 text-sm">Process item returns</p>
        </div>
      </div>

      {/* Search */}
      <Card className="p-4 border-slate-200 mb-6">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
          <Input
            placeholder="Search by user name, ID, or item..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="pl-9 h-10 border-slate-300"
          />
        </div>
      </Card>

      {/* Borrowed Items List */}
      <div className="space-y-3">
        {filteredItems.map((item) => {
          const isReturned = returnedId === item.id;
          return (
            <Card key={item.id} className="p-4 border-slate-200">
              {isReturned ? (
                <div className="flex items-center gap-3 text-green-600">
                  <div className="w-10 h-10 bg-green-50 rounded-full flex items-center justify-center">
                    <Check className="w-5 h-5" />
                  </div>
                  <div>
                    <div className="font-medium">Return Processed</div>
                    <div className="text-sm text-slate-600">Item has been returned successfully</div>
                  </div>
                </div>
              ) : (
                <div className="space-y-4">
                  {/* Header */}
                  <div className="flex items-start justify-between gap-4">
                    <div className="flex-1 min-w-0">
                      <div className="flex items-center gap-2 mb-1">
                        <span className="text-slate-900">{item.userName}</span>
                        <span className="text-sm text-slate-400">({item.userId})</span>
                      </div>
                      <div className="text-sm text-slate-600">{item.item}</div>
                    </div>
                    <span className="inline-flex items-center px-2.5 py-1 rounded text-xs bg-amber-50 text-amber-700 whitespace-nowrap">
                      {item.status}
                    </span>
                  </div>

                  {/* Details */}
                  <div className="grid grid-cols-3 gap-4 pt-3 border-t border-slate-100">
                    <div>
                      <div className="text-xs text-slate-500 mb-1">Quantity</div>
                      <div className="text-sm text-slate-900">{item.quantity}</div>
                    </div>
                    <div>
                      <div className="text-xs text-slate-500 mb-1">Borrowed</div>
                      <div className="text-sm text-slate-900">{item.borrowDate}</div>
                    </div>
                    <div>
                      <div className="text-xs text-slate-500 mb-1">Due Date</div>
                      <div className="text-sm text-slate-900">{item.dueDate}</div>
                    </div>
                  </div>

                  {/* Action */}
                  <Button
                    onClick={() => handleReturn(item.id)}
                    className="w-full h-10 bg-slate-800 hover:bg-slate-700"
                  >
                    Process Return
                  </Button>
                </div>
              )}
            </Card>
          );
        })}

        {filteredItems.length === 0 && (
          <Card className="p-12 border-slate-200 text-center">
            <p className="text-slate-500">No borrowed items found matching your search.</p>
          </Card>
        )}
      </div>
    </div>
  );
}
