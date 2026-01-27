// This fucntion will return different data table headers depending on the service called

export default function headerData({
  serviceName,
}: {
  serviceName: string;
}): Record<string, string> {
  switch (serviceName) {
    case "employees":
      return {
        employeeId: "Employee Id",
        firstName: "First Name",
        lastName: "Last Name",
        dateOfBirth: "Date Of Birth",
        designation: "Designation",
        gender: "Gender",
        hireDate: "Hire Date",
        jobStage: "Job Stage",
        salary: "Salary",
        managerEmployeeId: "Manager Employee Id",
      };
    default:
      return {};
  }
}
