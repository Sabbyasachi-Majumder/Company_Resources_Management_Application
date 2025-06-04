package com.java.projects.service;

import com.java.projects.repository.Projects;
import com.java.projects.repository.ProjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Optional;

public class ProjectServiceImpl implements ProjectService {

    @Autowired
    public ProjectsRepository projectsRepository;

    //Adds Employee details to the projects table
    public void addData(ArrayList<Projects> prjDetailsList) {
        for (Projects e : prjDetailsList) {
            projectsRepository.save(e);
        }
    }

    //Searching for projects details using projectID
    public Optional<Projects> searchData(int projectID) {
        return projectsRepository.findById(projectID);
    }
}
