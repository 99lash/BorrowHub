import { useState } from "react";
import { useNavigate } from "react-router";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "../components/ui/tabs";
import { Card } from "../components/ui/card";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from "../components/ui/dialog";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../components/ui/select";
import { Check, Search, User, Package, Calendar, Clock, AlertCircle } from "lucide-react";
import { toast } from "sonner";

// Item inventory data
const itemCategories = {
  Laptop: Array.from({ length: 30 }, (_, i) => `Laptop ${i + 1}`),
  Projector: Array.from({ length: 5 }, (_, i) => `Projector ${i + 1}`),
  HDMI: Array.from({ length: 5 }, (_, i) => `HDMI Cable ${i + 1}`),
  Camera: ["Camera 1", "Camera 2", "Camera 3"],
  Other: [],
};

interface BorrowedItem {
  id: string;
  studentNumber: string;
  studentName: string;
  course: string;
  item: string;
  borrowDate: string;
  borrowTime: string;
  status: "Borrowed" | "Returned";
}

const borrowedItems: BorrowedItem[] = [
  {
    id: "1",
    studentNumber: "2024-12345",
    studentName: "Sarah Chen",
    course: "BS Computer Science",
    item: "Laptop 5",
    borrowDate: "Feb 18, 2026",
    borrowTime: "09:15 AM",
    status: "Borrowed",
  },
  {
    id: "2",
    studentNumber: "2024-23456",
    studentName: "Emily Rodriguez",
    course: "BS Information Technology",
    item: "Projector 2",
    borrowDate: "Feb 18, 2026",
    borrowTime: "10:30 AM",
    status: "Borrowed",
  },
  {
    id: "3",
    studentNumber: "2024-34567",
    studentName: "David Kim",
    course: "BS Accountancy",
    item: "HDMI Cable 3",
    borrowDate: "Feb 18, 2026",
    borrowTime: "11:00 AM",
    status: "Borrowed",
  },
];

export function TransactionScreen() {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState("borrow");

  return (
    <div className="max-w-2xl mx-auto space-y-6 animate-fade-in">
      {/* Premium Header */}
      <div>
        <h1 className="text-3xl font-bold text-gray-900 mb-2 tracking-tight">Transactions</h1>
        <p className="text-gray-500 text-base">Borrow and return items</p>
      </div>

      {/* Premium Tabs */}
      <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full">
        <TabsList className="grid w-full grid-cols-2 h-12 bg-gray-100 p-1 rounded-xl">
          <TabsTrigger value="borrow" className="rounded-lg font-medium data-[state=active]:bg-white data-[state=active]:shadow-sm">
            Borrow Item
          </TabsTrigger>
          <TabsTrigger value="return" className="rounded-lg font-medium data-[state=active]:bg-white data-[state=active]:shadow-sm">
            Return Item
          </TabsTrigger>
        </TabsList>

        <TabsContent value="borrow" className="mt-6">
          <BorrowTab />
        </TabsContent>

        <TabsContent value="return" className="mt-6">
          <ReturnTab />
        </TabsContent>
      </Tabs>
    </div>
  );
}

function BorrowTab() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    studentNumber: "",
    studentName: "",
    course: "",
    itemType: "",
    item: "",
    otherItem: "",
  });
  const [submitted, setSubmitted] = useState(false);

  const currentDateTime = new Date().toLocaleString("en-US", {
    month: "short",
    day: "numeric",
    year: "numeric",
    hour: "numeric",
    minute: "2-digit",
    hour12: true,
  });
  const staffName = "Maria Garcia";

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    const itemName = formData.itemType === "Other" ? formData.otherItem : formData.item;
    
    toast.success("Borrow Request Submitted", {
      description: `${itemName} has been successfully borrowed by ${formData.studentName}.`,
    });
    
    setSubmitted(true);
    setTimeout(() => {
      setSubmitted(false);
      setFormData({
        studentNumber: "",
        studentName: "",
        course: "",
        itemType: "",
        item: "",
        otherItem: "",
      });
    }, 2000);
  };

  const handleChange = (field: string, value: string) => {
    setFormData((prev) => {
      const newData = { ...prev, [field]: value };
      if (field === "itemType") {
        newData.item = "";
        newData.otherItem = "";
      }
      return newData;
    });
  };

  const availableItems = formData.itemType
    ? itemCategories[formData.itemType as keyof typeof itemCategories] || []
    : [];

  if (submitted) {
    return (
      <Card className="p-10 border-gray-200/60 text-center shadow-premium rounded-2xl bg-white/80 backdrop-blur-sm">
        <div className="w-20 h-20 bg-gradient-to-br from-green-50 to-green-100 rounded-2xl flex items-center justify-center mx-auto mb-5">
          <Check className="w-10 h-10 text-green-600" />
        </div>
        <h2 className="text-2xl font-bold text-gray-900 mb-2 tracking-tight">Borrow Request Submitted</h2>
        <p className="text-gray-500 mb-6">The item has been successfully assigned to the borrower.</p>
        <div className="bg-gradient-to-br from-gray-50 to-gray-100 rounded-xl p-5 text-sm text-left">
          <div className="text-gray-600 mb-1 font-medium">Return by end of day:</div>
          <div className="text-gray-900 font-semibold text-base">
            {new Date().toLocaleDateString("en-US", { month: "short", day: "numeric", year: "numeric" })}
          </div>
        </div>
      </Card>
    );
  }

  return (
    <div className="space-y-5">
      {/* Auto-generated Info Card */}
      <Card className="p-5 border-blue-200/60 bg-gradient-to-br from-blue-50 to-blue-100/50 shadow-sm rounded-2xl">
        <div className="space-y-3 text-sm">
          <div className="flex justify-between items-center">
            <span className="text-gray-600 font-medium">Date & Time:</span>
            <span className="text-gray-900 font-semibold">{currentDateTime}</span>
          </div>
          <div className="flex justify-between items-center">
            <span className="text-gray-600 font-medium">Processed by:</span>
            <span className="text-gray-900 font-semibold">{staffName}</span>
          </div>
          <div className="flex justify-between items-center">
            <span className="text-gray-600 font-medium">Return by:</span>
            <span className="text-gray-900 font-semibold">End of day (Same day)</span>
          </div>
        </div>
      </Card>

      {/* Premium Form Card */}
      <Card className="p-6 sm:p-8 border-gray-200/60 shadow-sm hover:shadow-premium transition-all duration-300 rounded-2xl bg-white/80 backdrop-blur-sm">
        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Borrower Information */}
          <div className="pb-5 border-b border-gray-200">
            <h3 className="text-base font-semibold text-gray-900 mb-5">Borrower Information</h3>

            <div className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="studentNumber" className="text-gray-700 font-medium text-sm">
                  Student Number <span className="text-[#DC143C]">*</span>
                </Label>
                <Input
                  id="studentNumber"
                  placeholder="e.g., 2024-12345"
                  value={formData.studentNumber}
                  onChange={(e) => handleChange("studentNumber", e.target.value)}
                  className="h-12 px-4 border-gray-200 bg-gray-50 focus:bg-white rounded-xl transition-all"
                  required
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="studentName" className="text-gray-700 font-medium text-sm">
                  Full Name <span className="text-[#DC143C]">*</span>
                </Label>
                <Input
                  id="studentName"
                  placeholder="e.g., Juan Dela Cruz"
                  value={formData.studentName}
                  onChange={(e) => handleChange("studentName", e.target.value)}
                  className="h-12 px-4 border-gray-200 bg-gray-50 focus:bg-white rounded-xl transition-all"
                  required
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="course" className="text-gray-700 font-medium text-sm">
                  Course <span className="text-[#DC143C]">*</span>
                </Label>
                <Input
                  id="course"
                  placeholder="e.g., BS Computer Science"
                  value={formData.course}
                  onChange={(e) => handleChange("course", e.target.value)}
                  className="h-12 px-4 border-gray-200 bg-gray-50 focus:bg-white rounded-xl transition-all"
                  required
                />
              </div>
            </div>
          </div>

          {/* Item Information */}
          <div>
            <h3 className="text-base font-semibold text-gray-900 mb-5">Item Information</h3>

            <div className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="itemType" className="text-gray-700 font-medium text-sm">
                  Item Type <span className="text-[#DC143C]">*</span>
                </Label>
                <Select value={formData.itemType} onValueChange={(value) => handleChange("itemType", value)} required>
                  <SelectTrigger className="h-12 border-gray-200 bg-gray-50 rounded-xl">
                    <SelectValue placeholder="Select item type" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="Laptop">Laptop</SelectItem>
                    <SelectItem value="Projector">Projector</SelectItem>
                    <SelectItem value="HDMI">HDMI Cable</SelectItem>
                    <SelectItem value="Camera">Camera</SelectItem>
                    <SelectItem value="Other">Other</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              {formData.itemType && formData.itemType !== "Other" && (
                <div className="space-y-2">
                  <Label htmlFor="item" className="text-gray-700 font-medium text-sm">
                    Select Item <span className="text-[#DC143C]">*</span>
                  </Label>
                  <Select value={formData.item} onValueChange={(value) => handleChange("item", value)} required>
                    <SelectTrigger className="h-12 border-gray-200 bg-gray-50 rounded-xl">
                      <SelectValue placeholder="Choose specific item" />
                    </SelectTrigger>
                    <SelectContent>
                      {availableItems.map((item) => (
                        <SelectItem key={item} value={item}>
                          {item}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
              )}

              {formData.itemType === "Other" && (
                <div className="space-y-2">
                  <Label htmlFor="otherItem" className="text-gray-700 font-medium text-sm">
                    Specify Item <span className="text-[#DC143C]">*</span>
                  </Label>
                  <Input
                    id="otherItem"
                    placeholder="Enter item name"
                    value={formData.otherItem}
                    onChange={(e) => handleChange("otherItem", e.target.value)}
                    className="h-12 px-4 border-gray-200 bg-gray-50 focus:bg-white rounded-xl transition-all"
                    required
                  />
                </div>
              )}
            </div>
          </div>

          {/* Important Note */}
          <div className="bg-gradient-to-br from-amber-50 to-amber-100/50 border border-amber-200/60 rounded-xl p-4">
            <p className="text-sm text-gray-700 font-medium">
              <span className="font-semibold text-amber-900">Important:</span> All borrowed items must be returned by the end of the day.
            </p>
          </div>

          {/* Submit Button */}
          <Button 
            type="submit" 
            className="w-full h-12 bg-gradient-to-br from-black to-gray-800 hover:from-gray-800 hover:to-gray-900 rounded-xl shadow-lg shadow-black/20 hover:shadow-xl hover:shadow-black/30 transition-all duration-300 font-medium"
          >
            Submit Borrow Request
          </Button>
        </form>
      </Card>
    </div>
  );
}

function ReturnTab() {
  const [searchQuery, setSearchQuery] = useState("");
  const [items, setItems] = useState(borrowedItems);
  const [returnedId, setReturnedId] = useState<string | null>(null);
  const [selectedItem, setSelectedItem] = useState<BorrowedItem | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const filteredItems = items.filter(
    (item) =>
      item.status === "Borrowed" &&
      (item.studentName.toLowerCase().includes(searchQuery.toLowerCase()) ||
        item.studentNumber.toLowerCase().includes(searchQuery.toLowerCase()) ||
        item.item.toLowerCase().includes(searchQuery.toLowerCase()))
  );

  const handleOpenDetails = (item: BorrowedItem) => {
    setSelectedItem(item);
    setIsModalOpen(true);
  };

  const handleReturn = (id: string) => {
    const returnedItem = items.find(item => item.id === id);
    
    if (returnedItem) {
      toast.success("Return Processed Successfully", {
        description: `${returnedItem.item} has been returned by ${returnedItem.studentName}.`,
      });
    }
    
    setItems((prev) => prev.map((item) => (item.id === id ? { ...item, status: "Returned" as const } : item)));
    setReturnedId(id);
    setIsModalOpen(false);
    setTimeout(() => {
      setReturnedId(null);
    }, 2000);
  };

  return (
    <div className="space-y-5">
      {/* Premium Search Card */}
      <Card className="p-5 border-gray-200/60 shadow-sm hover:shadow-premium transition-all duration-300 rounded-2xl bg-white/80 backdrop-blur-sm">
        <div className="relative">
          <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
          <Input
            placeholder="Search by student name, number, or item..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="pl-11 h-12 border-gray-200 bg-gray-50 focus:bg-white rounded-xl transition-all"
          />
        </div>
      </Card>

      {/* Borrowed Items List */}
      <div className="space-y-4">
        {filteredItems.map((item) => {
          const isReturned = returnedId === item.id;
          return (
            <Card 
              key={item.id} 
              className={`p-5 border-gray-200/60 shadow-sm hover:shadow-premium transition-all duration-300 rounded-2xl bg-white/80 backdrop-blur-sm ${
                !isReturned ? 'cursor-pointer active:scale-[0.98]' : ''
              }`}
              onClick={() => !isReturned && handleOpenDetails(item)}
            >
              {isReturned ? (
                <div className="flex items-center gap-4 text-green-600">
                  <div className="w-14 h-14 bg-gradient-to-br from-green-50 to-green-100 rounded-2xl flex items-center justify-center flex-shrink-0">
                    <Check className="w-7 h-7" />
                  </div>
                  <div>
                    <div className="font-semibold text-gray-900 text-base mb-1">Return Processed</div>
                    <div className="text-sm text-gray-600">Item has been returned successfully</div>
                  </div>
                </div>
              ) : (
                <div>
                  <div className="flex items-start justify-between mb-3">
                    <div className="flex-1">
                      <div className="text-base font-semibold text-gray-900 mb-2">{item.studentName}</div>
                      <div className="text-sm text-gray-500 mb-3 font-medium">
                        {item.studentNumber} • {item.course}
                      </div>
                    </div>
                    <div className="text-xs text-[#DC143C] font-medium bg-red-50 px-3 py-1.5 rounded-lg">
                      Click to view
                    </div>
                  </div>
                  <div className="inline-flex items-center px-3 py-1.5 rounded-lg text-xs font-medium bg-amber-100 text-amber-700 mb-3">
                    <Package className="w-3 h-3 mr-1.5" />
                    {item.item}
                  </div>
                  <div className="flex items-center text-xs text-gray-500 font-medium">
                    <Clock className="w-3 h-3 mr-1.5" />
                    Borrowed: {item.borrowDate} at {item.borrowTime}
                  </div>
                </div>
              )}
            </Card>
          );
        })}

        {filteredItems.length === 0 && (
          <Card className="p-16 border-gray-200/60 text-center shadow-sm rounded-2xl bg-white/80 backdrop-blur-sm">
            <div className="w-16 h-16 bg-gray-100 rounded-2xl flex items-center justify-center mx-auto mb-4">
              <Search className="w-8 h-8 text-gray-400" />
            </div>
            <p className="text-gray-500 font-medium">No borrowed items found.</p>
          </Card>
        )}
      </div>

      {/* Premium Details Modal */}
      <Dialog open={isModalOpen} onOpenChange={setIsModalOpen}>
        <DialogContent className="sm:max-w-[420px] rounded-2xl">
          <DialogHeader>
            <DialogTitle className="text-xl font-bold tracking-tight">Borrowing Details</DialogTitle>
            <DialogDescription className="text-sm text-gray-500">
              Review information before processing return
            </DialogDescription>
          </DialogHeader>

          {selectedItem && (
            <div className="space-y-3 mt-4">
              {/* Borrower Information */}
              <div className="bg-gradient-to-br from-blue-50 to-blue-100/50 border border-blue-200/60 rounded-xl p-4">
                <div className="flex items-center gap-2 mb-3">
                  <div className="w-8 h-8 bg-white rounded-lg flex items-center justify-center">
                    <User className="w-4 h-4 text-blue-600" />
                  </div>
                  <h3 className="text-sm font-semibold text-gray-900">Borrower Information</h3>
                </div>
                <div className="space-y-2">
                  <div>
                    <div className="text-xs text-gray-600 mb-0.5 font-medium">Full Name</div>
                    <div className="text-sm text-gray-900 font-semibold">{selectedItem.studentName}</div>
                  </div>
                  <div>
                    <div className="text-xs text-gray-600 mb-0.5 font-medium">Student Number</div>
                    <div className="text-sm text-gray-900 font-semibold">{selectedItem.studentNumber}</div>
                  </div>
                  <div>
                    <div className="text-xs text-gray-600 mb-0.5 font-medium">Course</div>
                    <div className="text-sm text-gray-900 font-semibold">{selectedItem.course}</div>
                  </div>
                </div>
              </div>

              {/* Item Information */}
              <div className="bg-gradient-to-br from-amber-50 to-amber-100/50 border border-amber-200/60 rounded-xl p-4">
                <div className="flex items-center gap-2 mb-3">
                  <div className="w-8 h-8 bg-white rounded-lg flex items-center justify-center">
                    <Package className="w-4 h-4 text-amber-600" />
                  </div>
                  <h3 className="text-sm font-semibold text-gray-900">Item Information</h3>
                </div>
                <div>
                  <div className="text-xs text-gray-600 mb-0.5 font-medium">Borrowed Item</div>
                  <div className="text-sm text-gray-900 font-bold">{selectedItem.item}</div>
                </div>
              </div>

              {/* Time Information */}
              <div className="bg-gradient-to-br from-gray-50 to-gray-100/50 border border-gray-200/60 rounded-xl p-4">
                <div className="flex items-center gap-2 mb-3">
                  <div className="w-8 h-8 bg-white rounded-lg flex items-center justify-center">
                    <Clock className="w-4 h-4 text-gray-600" />
                  </div>
                  <h3 className="text-sm font-semibold text-gray-900">Time Information</h3>
                </div>
                <div className="space-y-2">
                  <div>
                    <div className="text-xs text-gray-600 mb-0.5 font-medium">Borrow Date</div>
                    <div className="text-sm text-gray-900 font-semibold flex items-center gap-1.5">
                      <Calendar className="w-3 h-3 text-gray-400" />
                      {selectedItem.borrowDate}
                    </div>
                  </div>
                  <div>
                    <div className="text-xs text-gray-600 mb-0.5 font-medium">Borrow Time</div>
                    <div className="text-sm text-gray-900 font-semibold flex items-center gap-1.5">
                      <Clock className="w-3 h-3 text-gray-400" />
                      {selectedItem.borrowTime}
                    </div>
                  </div>
                </div>
              </div>

              {/* Warning Message */}
              <div className="bg-gradient-to-br from-red-50 to-red-100/50 border border-red-200/60 rounded-xl p-3">
                <div className="flex items-start gap-2">
                  <AlertCircle className="w-4 h-4 text-red-600 flex-shrink-0 mt-0.5" />
                  <div>
                    <div className="text-xs font-semibold text-red-900 mb-0.5">Important Reminder</div>
                    <div className="text-xs text-red-700">
                      Please verify the item condition before processing.
                    </div>
                  </div>
                </div>
              </div>

              {/* Action Buttons */}
              <div className="flex gap-3 pt-1">
                <Button
                  variant="outline"
                  onClick={() => setIsModalOpen(false)}
                  className="flex-1 h-11 rounded-xl border-gray-300 hover:bg-gray-100 font-medium"
                >
                  Cancel
                </Button>
                <Button
                  onClick={() => handleReturn(selectedItem.id)}
                  className="flex-1 h-11 bg-gradient-to-br from-[#DC143C] to-[#B91130] hover:from-[#B91130] hover:to-[#9A0E28] rounded-xl shadow-lg shadow-[#DC143C]/25 hover:shadow-xl hover:shadow-[#DC143C]/30 transition-all duration-300 font-medium"
                >
                  Process Return
                </Button>
              </div>
            </div>
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
}