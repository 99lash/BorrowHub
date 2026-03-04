import { useState, useEffect } from "react";
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
import { Plus, Edit, Trash2, Shield, Key } from "lucide-react";
import { toast } from "sonner";

export function UserManagementScreen() {
  const [users, setUsers] = useState<any[]>([]);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingUser, setEditingUser] = useState<any>(null);
  
  const [formData, setFormData] = useState({
    username: "",
    name: "",
    role: "MIS/CSD Staff",
    password: "123"
  });

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = () => {
    const usersStr = localStorage.getItem("borrowHubUsers");
    if (usersStr) {
      setUsers(JSON.parse(usersStr));
    }
  };

  const handleOpenAdd = () => {
    setEditingUser(null);
    setFormData({ username: "", name: "", role: "MIS/CSD Staff", password: "123" });
    setIsDialogOpen(true);
  };

  const handleOpenEdit = (user: any) => {
    setEditingUser(user);
    setFormData({ ...user });
    setIsDialogOpen(true);
  };

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();
    let updatedUsers = [...users];

    if (editingUser) {
      const index = updatedUsers.findIndex(u => u.username === editingUser.username);
      if (index !== -1) {
        updatedUsers[index] = formData;
      }
      toast.success("User Updated", { description: `${formData.name} has been updated.` });
    } else {
      if (updatedUsers.find(u => u.username === formData.username)) {
        toast.error("Error", { description: "Username already exists." });
        return;
      }
      updatedUsers.push(formData);
      toast.success("User Added", { description: `${formData.name} has been created.` });
    }

    setUsers(updatedUsers);
    localStorage.setItem("borrowHubUsers", JSON.stringify(updatedUsers));
    setIsDialogOpen(false);
  };

  const handleDelete = (username: string) => {
    if (username === "admin") {
      toast.error("Action Denied", { description: "Default admin cannot be deleted." });
      return;
    }
    const updatedUsers = users.filter(u => u.username !== username);
    setUsers(updatedUsers);
    localStorage.setItem("borrowHubUsers", JSON.stringify(updatedUsers));
    toast.success("User Deleted", { description: `User ${username} has been removed.` });
  };

  const handleResetPassword = (username: string) => {
    let updatedUsers = [...users];
    const index = updatedUsers.findIndex(u => u.username === username);
    if (index !== -1) {
      updatedUsers[index].password = "123";
      setUsers(updatedUsers);
      localStorage.setItem("borrowHubUsers", JSON.stringify(updatedUsers));
      toast.success("Password Reset", { description: `Password for ${username} reset to default (123).` });
    }
  };

  return (
    <div className="max-w-5xl mx-auto space-y-6 animate-fade-in">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold text-gray-900 mb-2 tracking-tight">User Management</h1>
          <p className="text-gray-500 text-base">Manage system access and roles</p>
        </div>
        <Button 
          className="bg-gradient-to-br from-black to-gray-800 hover:from-gray-800 hover:to-gray-900 sm:w-auto rounded-xl shadow-lg h-11 px-5 font-medium" 
          onClick={handleOpenAdd}
        >
          <Plus className="w-4 h-4 mr-2" />
          Add User
        </Button>
      </div>

      <Card className="border-gray-200/60 overflow-hidden shadow-sm rounded-2xl bg-white/80 backdrop-blur-sm">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gradient-to-r from-gray-50 to-gray-100 border-b border-gray-200">
              <tr>
                <th className="text-left px-5 py-4 text-sm font-semibold text-gray-700">Name</th>
                <th className="text-left px-5 py-4 text-sm font-semibold text-gray-700">Username</th>
                <th className="text-left px-5 py-4 text-sm font-semibold text-gray-700">Role</th>
                <th className="text-right px-5 py-4 text-sm font-semibold text-gray-700">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {users.map((user) => (
                <tr key={user.username} className="hover:bg-gray-50/80 transition-all duration-200">
                  <td className="px-5 py-4 text-sm font-medium text-gray-900 flex items-center gap-3">
                    <div className="w-8 h-8 rounded-full bg-blue-100 text-blue-600 flex items-center justify-center text-xs font-bold">
                      {user.name.charAt(0)}
                    </div>
                    {user.name}
                  </td>
                  <td className="px-5 py-4 text-sm text-gray-600">{user.username}</td>
                  <td className="px-5 py-4">
                    <span className={`inline-flex items-center px-2.5 py-1 rounded-lg text-xs font-medium ${
                      user.role.includes("Admin") ? "bg-purple-100 text-purple-700" : "bg-green-100 text-green-700"
                    }`}>
                      <Shield className="w-3 h-3 mr-1.5" />
                      {user.role}
                    </span>
                  </td>
                  <td className="px-5 py-4 text-right">
                    <div className="flex items-center justify-end gap-2">
                      <Button variant="ghost" size="sm" onClick={() => handleResetPassword(user.username)} className="h-8 px-2 text-amber-600 hover:text-amber-700 hover:bg-amber-50" title="Reset Password to 123">
                        <Key className="w-4 h-4" />
                      </Button>
                      <Button variant="ghost" size="sm" onClick={() => handleOpenEdit(user)} className="h-8 w-8 p-0 text-blue-600 hover:text-blue-700 hover:bg-blue-50">
                        <Edit className="w-4 h-4" />
                      </Button>
                      <Button variant="ghost" size="sm" onClick={() => handleDelete(user.username)} className="h-8 w-8 p-0 text-red-600 hover:text-red-700 hover:bg-red-50">
                        <Trash2 className="w-4 h-4" />
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </Card>

      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent className="sm:max-w-[420px] rounded-2xl">
          <DialogHeader>
            <DialogTitle className="text-xl font-bold">{editingUser ? "Edit User" : "Add New User"}</DialogTitle>
            <DialogDescription className="text-sm text-gray-500">
              Fill in the details below.
            </DialogDescription>
          </DialogHeader>
          <form onSubmit={handleSave} className="space-y-4 mt-4">
            <div className="space-y-2">
              <Label htmlFor="name">Full Name</Label>
              <Input
                id="name"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                className="h-11 rounded-xl"
                required
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="username">Username</Label>
              <Input
                id="username"
                value={formData.username}
                onChange={(e) => setFormData({ ...formData, username: e.target.value })}
                className="h-11 rounded-xl"
                disabled={!!editingUser}
                required
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="role">Role</Label>
              <Select value={formData.role} onValueChange={(val) => setFormData({ ...formData, role: val })}>
                <SelectTrigger className="h-11 rounded-xl">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="MIS/CSD Staff">MIS/CSD Staff</SelectItem>
                  <SelectItem value="MIS/CSD Admin">MIS/CSD Admin</SelectItem>
                </SelectContent>
              </Select>
            </div>
            {!editingUser && (
              <div className="space-y-2">
                <Label htmlFor="password">Default Password</Label>
                <Input
                  id="password"
                  value={formData.password}
                  disabled
                  className="h-11 rounded-xl bg-gray-50 text-gray-500"
                />
              </div>
            )}
            <div className="pt-2 flex gap-3">
              <Button type="button" variant="outline" onClick={() => setIsDialogOpen(false)} className="flex-1 rounded-xl">
                Cancel
              </Button>
              <Button type="submit" className="flex-1 rounded-xl bg-gray-900 text-white hover:bg-gray-800">
                {editingUser ? "Save Changes" : "Create User"}
              </Button>
            </div>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
}