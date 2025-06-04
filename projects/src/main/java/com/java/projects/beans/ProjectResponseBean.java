package com.java.projects.beans;

import java.util.ArrayList;

import com.java.projects.repository.Projects;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ProjectResponseBean {

    @JsonProperty("responseMessage")
    public String responseMessage;

    @JsonProperty("responseStatusCode")
    public HttpStatus responseStatusCode;

    @JsonProperty("emp")
    Projects prj;

    @JsonProperty("empDetailsList")
    ArrayList<Projects> prjDetailsList;

    public ProjectResponseBean(String responseMessage, HttpStatus responseStatusCode, Projects prj, ArrayList<Projects> prjDetailsList) {
        this.responseMessage = responseMessage;
        this.responseStatusCode = responseStatusCode;
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

    public ProjectResponseBean() {
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
    public HttpStatus getResponseStatusCode() {
        return responseStatusCode;
    }

    public void setResponseStatusCode(HttpStatus responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }
}
