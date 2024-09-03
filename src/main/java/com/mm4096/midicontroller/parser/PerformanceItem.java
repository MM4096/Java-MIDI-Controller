package com.mm4096.midicontroller.parser;

public class PerformanceItem {
    private String patch;
    private String patchName;
    private String comments;

    public PerformanceItem(String patch, String comments) {
        this.patch = patch;
        this.patchName = patch;
        this.comments = comments;
    }

    public PerformanceItem(String patch, String patchName, String comments) {
        this.patch = patch;
        this.patchName = patchName;
        this.comments = comments;
    }

    public String getPatch() {
        return patch;
    }

    public void setPatch(String patch) {
        this.patch = patch;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPatchName() {
        return patchName;
    }

    public void setPatchName(String patchName) {
        this.patchName = patchName;
    }
}
