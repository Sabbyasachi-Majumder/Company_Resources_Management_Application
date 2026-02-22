package com.company.employee.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class EmployeeHealthServiceImpl implements EmployeeHealthService {

    private final DataSource dataSource;

    // For detailed logging in the application
    private static final Logger logger = LoggerFactory.getLogger(EmployeeHealthServiceImpl.class);

    public EmployeeHealthServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //Test Database Connection business logic
    public String testDatabaseConnection() {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            if (connection.isValid(1)) {
                logger.debug("Testing successful . Database connection is present.");
                return "Connection from Employee Application to Employee Database successfully established.";
            } else {
                logger.error("Testing failed . Database connection is not present.");
                return "Connection to Employee Database failed to be established.";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
