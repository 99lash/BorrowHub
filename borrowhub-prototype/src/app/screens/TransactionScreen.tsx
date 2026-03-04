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
import { Check, Search, User, Package, Calendar, Clock, AlertCircle, Plus, Trash2 } from "lucide-react";
import { toast } from "sonner";

// Item inventory data by type
const itemTypesData = {
  Equipment: [
    "Projector - Epson EB-X41",
    "Camera - Canon EOS R6",
    "Conference Microphone",
    "Extension Cable 10m",
    "Wireless Presenter",
    "Tripod Stand",
    "HDMI Cable 5m"
  ],
  Laptop: [
    "Laptop - Dell XPS 15",
    "Laptop - MacBook Pro 16",
    "Laptop - Lenovo ThinkPad"
  ]
};

interface BorrowedItem {
  id: string;
  studentNumber: string;
  studentName: string;
  course: string;
  collateral: string;
  items: string[];
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
    collateral: "Student ID",
    items: ["Laptop - Dell XPS 15", "Extension Cable 10m"],
    borrowDate: "Feb 18, 2026",
    borrowTime: "09:15 AM",
    status: "Borrowed",
  },
  {
    id: "2",
    studentNumber: "2024-23456",
    studentName: "Emily Rodriguez",
    course: "BS Information Technology",
    collateral: "Driver's License",
    items: ["Projector - Epson EB-X41"],
    borrowDate: "Feb 18, 2026",
    borrowTime: "10:30 AM",
    status: "Borrowed",
  },
];

export function TransactionScreen() {
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
  const [formData, setFormData] = useState({
    studentNumber: "",
    studentName: "",
    course: "",
    collateral: "",
  });
  
  const [borrowedItemsList, setBorrowedItemsList] = useState<{type: string, name: string}[]>([
    { type: "", name: "" }
  ]);
  
  const [submitted, setSubmitted] = useState(false);

  const currentDateTime = new Date().toLocaleString("en-US", {
    month: "short",
    day: "numeric",
    year: "numeric",
    hour: "numeric",
    minute: "2-digit",
    hour12: true,
  });
  const staffName = "System Staff";

  const handleItemChange = (index: number, field: "type" | "name", value: string) => {
    const updatedList = [...borrowedItemsList];
    updatedList[index] = { ...updatedList[index], [field]: value };
    if (field === "type") {
      updatedList[index].name = ""; // Reset name when type changes
    }
    setBorrowedItemsList(updatedList);
  };

  const addItemRow = () => {
    setBorrowedItemsList([...borrowedItemsList, { type: "", name: "" }]);
  };

  const removeItemRow = (index: number) => {
    if (borrowedItemsList.length > 1) {
      setBorrowedItemsList(borrowedItemsList.filter((_, i) => i !== index));
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    // Validate that all item rows have a selected name
    if (borrowedItemsList.some(item => !item.name)) {
      toast.error("Incomplete Form", { description: "Please complete all item selections." });
      return;
    }
    
    toast.success("Borrow Request Submitted", {
      description: `${borrowedItemsList.length} item(s) have been successfully borrowed by ${formData.studentName}.`,
    });
    
    setSubmitted(true);
    setTimeout(() => {
      setSubmitted(false);
      setFormData({
        studentNumber: "",
        studentName: "",
        course: "",
        collateral: "",
      });
      setBorrowedItemsList([{ type: "", name: "" }]);
    }, 2500);
  };

  if (submitted) {
    return (
      <Card className="p-10 border-gray-200/60 text-center shadow-premium rounded-2xl bg-white/80 backdrop-blur-sm">
        <div className="w-20 h-20 bg-gradient-to-br from-green-50 to-green-100 rounded-2xl flex items-center justify-center mx-auto mb-5">
          <Check className="w-10 h-10 text-green-600" />
        </div>
        <h2 className="text-2xl font-bold text-gray-900 mb-2 tracking-tight">Borrow Request Submitted</h2>
        <p className="text-gray-500 mb-6">The items have been successfully assigned to the borrower.</p>
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
                  onChange={(e) => setFormData({...formData, studentNumber: e.target.value})}
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
                  onChange={(e) => setFormData({...formData, studentName: e.target.value})}
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
                  onChange={(e) => setFormData({...formData, course: e.target.value})}
                  className="h-12 px-4 border-gray-200 bg-gray-50 focus:bg-white rounded-xl transition-all"
                  required
                />
              </div>
              
              <div className="space-y-2">
                <Label htmlFor="collateral" className="text-gray-700 font-medium text-sm">
                  Collateral <span className="text-[#DC143C]">*</span>
                </Label>
                <Input
                  id="collateral"
                  placeholder="e.g., Student ID, Driver's License"
                  value={formData.collateral}
                  onChange={(e) => setFormData({...formData, collateral: e.target.value})}
                  className="h-12 px-4 border-gray-200 bg-gray-50 focus:bg-white rounded-xl transition-all"
                  required
                />
              </div>
            </div>
          </div>

          {/* Items Information */}
          <div>
            <div className="flex items-center justify-between mb-5">
              <h3 className="text-base font-semibold text-gray-900">Items to Borrow</h3>
              <Button type="button" variant="outline" size="sm" onClick={addItemRow} className="rounded-lg h-8 text-xs font-medium">
                <Plus className="w-3 h-3 mr-1.5" />
                Add Item
              </Button>
            </div>

            <div className="space-y-4">
              {borrowedItemsList.map((itemRow, index) => (
                <div key={index} className="flex flex-col sm:flex-row gap-3 items-start sm:items-end p-4 border border-gray-100 bg-gray-50/50 rounded-xl relative">
                  {borrowedItemsList.length > 1 && (
                    <Button
                      type="button"
                      variant="ghost"
                      size="icon"
                      onClick={() => removeItemRow(index)}
                      className="absolute -top-2 -right-2 w-7 h-7 bg-white border border-gray-200 rounded-full text-gray-400 hover:text-red-500 hover:border-red-200 shadow-sm z-10"
                    >
                      <Trash2 className="w-3 h-3" />
                    </Button>
                  )}
                  
                  <div className="space-y-2 flex-1 w-full">
                    <Label className="text-gray-700 font-medium text-xs">Type</Label>
                    <Select value={itemRow.type} onValueChange={(val) => handleItemChange(index, "type", val)} required>
                      <SelectTrigger className="h-10 border-gray-200 bg-white rounded-lg">
                        <SelectValue placeholder="Select type" />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="Equipment">Equipment</SelectItem>
                        <SelectItem value="Laptop">Laptop</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>
                  
                  <div className="space-y-2 flex-[2] w-full">
                    <Label className="text-gray-700 font-medium text-xs">Item Name</Label>
                    <Select 
                      value={itemRow.name} 
                      onValueChange={(val) => handleItemChange(index, "name", val)} 
                      disabled={!itemRow.type}
                      required
                    >
                      <SelectTrigger className="h-10 border-gray-200 bg-white rounded-lg disabled:opacity-50 disabled:bg-gray-50">
                        <SelectValue placeholder={itemRow.type ? "Choose specific item" : "Select type first"} />
                      </SelectTrigger>
                      <SelectContent>
                        {itemRow.type && itemTypesData[itemRow.type as keyof typeof itemTypesData]?.map((itemOp) => (
                          <SelectItem key={itemOp} value={itemOp}>
                            {itemOp}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                </div>
              ))}
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
        item.items.join(" ").toLowerCase().includes(searchQuery.toLowerCase()))
  );

  const handleOpenDetails = (item: BorrowedItem) => {
    setSelectedItem(item);
    setIsModalOpen(true);
  };

  const handleReturn = (id: string) => {
    const returnedItem = items.find(item => item.id === id);
    
    if (returnedItem) {
      toast.success("Return Processed Successfully", {
        description: `Items have been returned by ${returnedItem.studentName}.`,
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
                    <div className="text-sm text-gray-600">Items have been returned successfully</div>
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
                  <div className="flex flex-wrap gap-2 mb-3">
                    {item.items.map((itemName, idx) => (
                      <div key={idx} className="inline-flex items-center px-3 py-1.5 rounded-lg text-xs font-medium bg-amber-100 text-amber-700">
                        <Package className="w-3 h-3 mr-1.5" />
                        {itemName}
                      </div>
                    ))}
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
            <p className="text-gray-500 font-medium">No borrowed transactions found.</p>
          </Card>
        )}
      </div>

      {/* Premium Details Modal */}
      <Dialog open={isModalOpen} onOpenChange={setIsModalOpen}>
        <DialogContent className="sm:max-w-[420px] rounded-2xl max-h-[90vh] overflow-y-auto">
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
                  <div>
                    <div className="text-xs text-gray-600 mb-0.5 font-medium">Collateral</div>
                    <div className="text-sm text-gray-900 font-semibold bg-white px-2 py-1 rounded border border-gray-100 inline-block">{selectedItem.collateral}</div>
                  </div>
                </div>
              </div>

              {/* Items Information */}
              <div className="bg-gradient-to-br from-amber-50 to-amber-100/50 border border-amber-200/60 rounded-xl p-4">
                <div className="flex items-center gap-2 mb-3">
                  <div className="w-8 h-8 bg-white rounded-lg flex items-center justify-center">
                    <Package className="w-4 h-4 text-amber-600" />
                  </div>
                  <h3 className="text-sm font-semibold text-gray-900">Items Information ({selectedItem.items.length})</h3>
                </div>
                <ul className="space-y-2">
                  {selectedItem.items.map((itemName, idx) => (
                    <li key={idx} className="flex items-center gap-2">
                      <div className="w-1.5 h-1.5 rounded-full bg-amber-500"></div>
                      <span className="text-sm text-gray-900 font-semibold">{itemName}</span>
                    </li>
                  ))}
                </ul>
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
                      Verify ALL items and return the <b>{selectedItem.collateral}</b>.
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