import { useState } from "react";
import { useNavigate } from "react-router";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import logo from "../../assets/logo.png";
import { toast } from "sonner";

export function LoginScreen() {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleSignIn = (e: React.FormEvent) => {
    e.preventDefault();
    
    // Validate credentials
    if (username === "user" && password === "123") {
      toast.success("Login Successful", {
        description: "Welcome to BorrowHub!",
      });
      setTimeout(() => {
        navigate("/app");
      }, 500);
    } else {
      toast.error("Invalid Credentials", {
        description: "Username or password is incorrect.",
      });
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 via-white to-gray-100 flex items-center justify-center p-4">
      <div className="w-full max-w-md animate-fade-in">
        {/* Premium Card with Shadow */}
        <div className="bg-white rounded-2xl shadow-premium border border-gray-100 p-8 sm:p-10">
          {/* Logo Section */}
          <div className="flex flex-col items-center mb-8">
            <div className="relative mb-4">
              <div className="absolute inset-0 bg-gradient-to-br from-[#DC143C]/20 to-[#B91130]/20 blur-2xl rounded-full"></div>
              <img src={logo} alt="BorrowHub Logo" className="w-20 h-20 relative z-10 drop-shadow-lg" />
            </div>
            <h1 className="text-2xl sm:text-3xl text-gray-900 mb-1.5">BorrowHub</h1>
            <p className="text-gray-500 text-sm">Management Information System</p>
          </div>

          {/* Form */}
          <form onSubmit={handleSignIn} className="space-y-5">
            <div className="space-y-2">
              <Label htmlFor="username" className="text-gray-700 text-sm font-medium">
                Username
              </Label>
              <Input
                id="username"
                type="text"
                placeholder="Enter your username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                className="h-12 px-4 bg-gray-50 border-gray-200 rounded-xl focus:bg-white focus:border-[#DC143C] focus:ring-2 focus:ring-[#DC143C]/20 transition-all"
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="password" className="text-gray-700 text-sm font-medium">
                Password
              </Label>
              <Input
                id="password"
                type="password"
                placeholder="Enter your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="h-12 px-4 bg-gray-50 border-gray-200 rounded-xl focus:bg-white focus:border-[#DC143C] focus:ring-2 focus:ring-[#DC143C]/20 transition-all"
                required
              />
            </div>

            <Button 
              type="submit" 
              className="w-full h-12 bg-gradient-to-r from-[#DC143C] to-[#B91130] hover:from-[#B91130] hover:to-[#9A0E28] text-white rounded-xl shadow-lg shadow-[#DC143C]/25 hover:shadow-xl hover:shadow-[#DC143C]/30 transition-all duration-200 font-medium mt-6"
            >
              Sign In
            </Button>
          </form>

          {/* Footer */}
          <div className="mt-8 text-center">
            <p className="text-xs text-gray-400">
              Authorized personnel only
            </p>
          </div>
        </div>

        {/* Bottom Text */}
        <div className="mt-6 text-center">
          <p className="text-xs text-gray-500">
            © 2026 Computer Science Studies. All rights reserved.
          </p>
        </div>
      </div>
    </div>
  );
}