package com.java.projects.service;

import com.java.projects.repository.Projects;

import java.util.ArrayList;
import java.util.Optional;

public interface ProjectService {

    public void addData(ArrayList<Projects> prjDetailList);

    public Optional<Projects> searchData(int projectID);
}
