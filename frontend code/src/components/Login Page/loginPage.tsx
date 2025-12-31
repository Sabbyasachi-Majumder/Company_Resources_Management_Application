import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";

import { useEffect, useState } from "react";

import { authenticateUser } from "../../api/loginApi.ts";

export default function LoginPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [keepLoggedIn, setKeepLoggedIn] = useState(true);

  const handleSubmitButton = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log(username + " : " + password);
    try {
      const result = await authenticateUser(username, password);
      localStorage.setItem("token", result.token);
      localStorage.setItem("refreshToken", result.refreshToken);
    } catch (err: any) {
      console.error("Login error : " + err);
    }
  };

  useEffect(() => {
    console.log("Token : " + localStorage.getItem("token"));
    console.log("Refresh Token : " + localStorage.getItem("refreshToken"));
  }, [localStorage.getItem("token"), localStorage.getItem("refreshToken")]);

  return (
    <form
      className="fixed inset-0 bg-blue flex items-center justify-center overflow-hidden"
      onSubmit={handleSubmitButton}
    >
      {/* Fixed full screen, gradient background, no overflow */}
      <Card className="w-full max-w-md bg-white/10 backdrop-blur-md border-white/20 shadow-2xl">
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
              className="bg-white/20 border-white/30 text-white placeholder-gray-400 focus:border-white/50"
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
          </div>
          <div className="flex items-center space-x-2">
            <Checkbox id="keep" defaultChecked />
            <Label
              htmlFor="keep"
              className="text-sm text-gray-300 cursor-pointer font-normal"
            >
              Remember Me
            </Label>
          </div>
          <div className="flex gap-4 pt-4">
            <Button
              type="submit"
              className="flex-1 bg-offWhite text-white border border-gray-600"
            >
              Submit
            </Button>
            <Button
              type="reset"
              onClick={() => {
                setUsername("");
                setPassword("");
                setKeepLoggedIn(true);
              }}
              className="flex-1 bg-offWhite text-white border border-gray-600"
            >
              Cancel
            </Button>
          </div>
        </CardContent>
      </Card>
    </form>
  );
}
