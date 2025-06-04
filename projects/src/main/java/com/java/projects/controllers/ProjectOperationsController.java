package com.java.projects.controllers;


import com.java.projects.beans.ProjectDetailBean;
import com.java.projects.beans.ProjectResponseBean;
import com.java.projects.repository.Projects;
import com.java.projects.repository.ProjectsRepository;
import com.java.projects.service.ProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class ProjectOperationsController {

    @Autowired
    public ProjectsRepository projectsRepository;

    @Autowired
    public ProjectServiceImpl projectService;

    @Autowired
    private DataSource dataSource;

    // testing connection
    @GetMapping(value = "/testProjectConnection")
    public ResponseEntity<ProjectResponseBean> testPostmanToApplicationConnection() {
        ProjectResponseBean prBean = new ProjectResponseBean();
        prBean.setResponseMessage("Connection successfully established.");
        prBean.setResponseStatusCode(HttpStatus.OK);
        return returnResponse(prBean);
    }

    // testing Database connection
    @GetMapping(value = "/testProjectDataBaseConnection")
    public ResponseEntity<ProjectResponseBean> testDataBaseConnection() throws SQLException {
        ProjectResponseBean prBean = new ProjectResponseBean();
        Connection connection = DataSourceUtils.getConnection(dataSource);
        if (connection.isValid(1)) {
            prBean.setResponseMessage("Database Connection successfully established.");
            prBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            prBean.setResponseMessage("Database Connection failed.");
            prBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(prBean);
    }

    // returning the response bean with adequate status code , message , custom header and response body information
    public ResponseEntity<ProjectResponseBean> returnResponse(ProjectResponseBean prBean) {
        return ResponseEntity
                .status(prBean.getResponseStatusCode()) // Set status code
                .header("URL", "/printProjectListJson")
                .body(prBean);  // Set response body
    }

    //    Fetching the database table data on request
    @GetMapping(value = "/fetchProjectDetailData", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ProjectResponseBean> fetchProjects() {
        ArrayList<Projects> empList = (ArrayList<Projects>) projectsRepository.findAll();
        ProjectResponseBean prBean = new ProjectResponseBean();
        if (!empList.isEmpty()) {
            prBean.setPrjDetailsList(empList);
            prBean.setResponseMessage("Fetching " + empList.size() + " project data records");
            prBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            prBean.setResponseMessage("Given list was empty thus no data can be fetched");
            prBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(prBean);
    }

    //method to add the project details to the database
    @PostMapping(value = "/addProjectDetailData", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ProjectResponseBean> addProjects(@RequestBody ProjectDetailBean prjBean) {
        ProjectResponseBean prBean = new ProjectResponseBean();
        if (prjBean.getPrjDetailsList() != null && !prjBean.getPrjDetailsList().isEmpty()) {
            projectService.addData(prjBean.getPrjDetailsList());
            prBean.setResponseMessage("Successfully added " + prjBean.getPrjDetailsList().size() + " project data records");
            prBean.setResponseStatusCode(HttpStatus.CREATED);
        } else {
            prBean.setResponseMessage("Given list was empty thus there are no additions to the database");
            prBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(prBean);
    }

    // method to search for a project details based on project id
    @GetMapping(value = "/searchProjectData", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ProjectResponseBean> searchProjects(@RequestBody ProjectDetailBean prjBean) {
        ProjectResponseBean prBean = new ProjectResponseBean();
        Optional<Projects> index = projectService.searchData(prjBean.getPrj().getProjectID());
        if (index.isPresent()) {
            prBean.setPrj(index.get());
            prBean.setResponseMessage("Successfully found Project Id " + prjBean.getPrj().getProjectID() + " data records");
            prBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            prBean.setResponseMessage("Project Id " + prjBean.getPrj().getProjectID() + " was not found.");
            prBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(prBean);
    }

    // method to update the project details based on project id
    @PutMapping(value = "/updateProjectData", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ProjectResponseBean> updateProjects(@RequestBody ProjectDetailBean prjBean) {
        ProjectResponseBean prBean = new ProjectResponseBean();
        if ((projectService.searchData(prjBean.getPrjDetailsList().get(0).getProjectID())).isPresent()) {
            projectService.addData(prjBean.getPrjDetailsList());
            prBean.setResponseMessage("Successfully updated Project Id " + prjBean.getPrjDetailsList().get(0).getProjectID() + " data records");
            prBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            prBean.setResponseMessage("Project Id " + prjBean.getPrjDetailsList().get(0).getProjectID() + " was not found.");
            prBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(prBean);
    }

    // method to remove the project details from the database or local class variable
    @DeleteMapping(value = "/deleteProjectData", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ProjectResponseBean> deleteProjects(@RequestBody ProjectDetailBean prjBean) {
        ProjectResponseBean prBean = new ProjectResponseBean();
        if ((projectService.searchData(prjBean.getPrj().getProjectID()).isPresent())) {
            projectsRepository.deleteById(prjBean.getPrj().getProjectID());
            prBean.setResponseMessage("Successfully deleted Project Id " + prjBean.getPrj().getProjectID() + " data records");
            prBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            prBean.setResponseMessage("Project Id " + prjBean.getPrj().getProjectID() + " was not found.");
            prBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(prBean);
    }
}
