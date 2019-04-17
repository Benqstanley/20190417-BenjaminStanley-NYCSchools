package com.zianderthalapps.a20190415_benjaminstanley_nycschools;
//Class created to store the relevant information about each of the schools
public class School {
    String schoolName;
    String overviewParagraph;
    String dbn;

    public String getDbn() {
        return dbn;
    }

    public School(String name, String overviewParagraph, String dbn){
        this.schoolName = name;
        this.overviewParagraph = overviewParagraph;
        this.dbn = dbn;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getOverviewParagraph() {
        return overviewParagraph;
    }

    public void setOverviewParagraph(String overviewParagraph) {
        this.overviewParagraph = overviewParagraph;
    }
}
