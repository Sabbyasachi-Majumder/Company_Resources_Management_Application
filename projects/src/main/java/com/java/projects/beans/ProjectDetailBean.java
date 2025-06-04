package com.java.projects.beans;

import java.util.ArrayList;

import com.java.projects.repository.Projects;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class ProjectDetailBean {

    @JsonProperty("prj")
    Projects prj;

    @JsonProperty("prjDetailsList")
    ArrayList<Projects> prjDetailsList;

    public ProjectDetailBean() {
    }

    public ProjectDetailBean(Projects prj, ArrayList<Projects> prjDetailsList) {
        this.prj = prj;
        this.prjDetailsList = prjDetailsList;
    }

    public Projects getPrj() {
        return prj;
    }

    public void setPrj(Projects prj) {
        this.prj = prj;
    }

    public ArrayList<Projects> getPrjDetailsList() {
        return prjDetailsList;
    }

    public void setPrjDetailsList(ArrayList<Projects> prjDetailsList) {
        this.prjDetailsList = prjDetailsList;
    }

}
