// Login Page for the application :
// User has to enter the username,password(8-16 alphanumeric characters) and check the option to have a longer JWT for inifinte login

import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";

import { useState } from "react";

import { authenticateUser } from "../../api/loginApi.ts";
import { handleApiError } from "../utility-functions/handleErrorAndExceptions.ts";
import { useNavigate } from "react-router-dom";

export default function LoginPage() {
  // console.log("/t/t***********Login Page Logs***********");
  const [username, setUsername] = useState<string>(""); //for storing the userName data
  const [password, setPassword] = useState<string>(""); //for storing the password data
  const [keepLoggedIn, setKeepLoggedIn] = useState(true); //for requesting the backend to make a longer or infinite token or refresh token

  const [error, setError] = useState<string | null>(null); //for displaying the error message from the backend

  const [isLoading, setIsLoading] = useState(false); //

  const navigate = useNavigate();

  const handleSubmitButton = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError("");
    setIsLoading(true); // Start loading
    try {
      const result = await authenticateUser(username, password);
      const { token, refreshToken } = result.data;
      localStorage.setItem("token", token);
      localStorage.setItem("refreshToken", refreshToken);
      alert(result.message); //temporary success message, will be removed in favour of redirecting and success toast
      console.log(result);
      navigate("/home");
    } catch (err: any) {
      setError(handleApiError(err));
    } finally {
      setIsLoading(false); // Always stop loading
    }
  };

  return (
    <form
      className="fixed inset-0 bg-gray-950 flex items-center justify-center overflow-hidden"
      onSubmit={handleSubmitButton}
    >
      {/* Fixed full screen, no overflow */}
      <Card className="w-full max-w-md flex flex-col border-width-10 border-yellow-500">
        <CardHeader className="text-center space-y-2 pt-8">
          <h1 className="text-3xl font-bold text-white">Login</h1>
          <p className="text-sm text-gray-300">
            All sessions are secure and encrypted
          </p>
        </CardHeader>
        <CardContent className="space-y-6 px-8 pb-10">
          <div className="space-y-2">
            <Label htmlFor="username" className="text-white">
              User Name
            </Label>
            <Input
              id="username"
              placeholder="user123"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="bg-white/20 border-white/30 text-white placeholder-gray-400 focus:border-white/50 "
              required
            />
          </div>
          <div className="space-y-2">
            <Label htmlFor="password" className="text-white">
              Password
            </Label>
            <Input
              id="password"
              type="password"
              placeholder="••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="bg-white/20 border-white/30 text-white placeholder-gray-400 focus:border-white/50"
              required
            />
            {/* Inline Error Message - Add this here */}
            {error && (
              <div className="bg-red-900/40 border border-red-600 px-4 py-3 text-sm text-red-300 animate-in fade-in slide-in-from-top-2 duration-300">
                <p className="text-center font-medium">{error}</p>
              </div>
            )}
          </div>
          <div className="flex items-center space-x-2">
            <Checkbox
              id="keep"
              checked={keepLoggedIn}
              onCheckedChange={(checked) => setKeepLoggedIn(checked as boolean)}
              className="h-5 w-5"
            />
            <Label
              htmlFor="keep"
              className="text-sm text-gray-300 cursor-pointer font-normal"
            >
              Remember Me
            </Label>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 max-w-md mx-auto mt-6">
            <Button
              variant="outline"
              size="lg"
              type="submit"
              disabled={isLoading}
            >
              {isLoading ? "Logging in..." : "Login"}
            </Button>
            <Button
              variant="outline"
              size="lg"
              type="reset"
              onClick={() => {
                setUsername("");
                setPassword("");
                setKeepLoggedIn(true);
                setError(null);
              }}
            >
              Cancel
            </Button>
          </div>
        </CardContent>
      </Card>
    </form>
  );
}
