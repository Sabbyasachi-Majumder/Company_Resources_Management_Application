import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";

import { useNavigate } from "react-router-dom";

export default function HomePage() {
  const navigate = useNavigate();
  return (
    <div className="container mx-auto py-8 px-4">
      <h1 className="text-3xl font-bold mb-8">
        Company Resource Management Application
      </h1>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <Card
          className="w-full max-w-sm"
          onClick={() => navigate("/authenticates")}
        >
          <CardHeader>
            <CardTitle>Authenticate Service</CardTitle>
            <CardDescription>CRUD operations about Users</CardDescription>
          </CardHeader>
          <CardContent>
            <p className="text-2xl font-bold text-primary">10</p>
            <p className="text-sm text-muted-foreground">Total Users</p>
          </CardContent>
        </Card>
        <Card
          className="w-full max-w-sm"
          onClick={() => navigate("/employees")}
        >
          <CardHeader>
            <CardTitle>Employee Service</CardTitle>
            <CardDescription>CRUD operations about Employees</CardDescription>
          </CardHeader>
          <CardContent>
            <p className="text-2xl font-bold text-primary">342</p>
            <p className="text-sm text-muted-foreground">Total Employees</p>
          </CardContent>
        </Card>
        <Card className="w-full max-w-sm" onClick={() => navigate("/projects")}>
          <CardHeader>
            <CardTitle>Project Service</CardTitle>
            <CardDescription>CRUD operations about Projects</CardDescription>
          </CardHeader>
          <CardContent>
            <p className="text-2xl font-bold text-primary">RICO-Verizon</p>
            <p className="text-sm text-muted-foreground">Largest Project</p>
          </CardContent>
        </Card>
        <Card
          className="w-full max-w-sm"
          onClick={() => navigate("/departments")}
        >
          <CardHeader>
            <CardTitle>Department Service</CardTitle>
            <CardDescription>CRUD operations about Departments</CardDescription>
          </CardHeader>
          <CardContent>
            <p className="text-2xl font-bold text-primary">BCSS</p>
            <p className="text-sm text-muted-foreground">
              Most Profitable Department
            </p>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
