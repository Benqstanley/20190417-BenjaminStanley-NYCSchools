package com.zianderthalapps.a20190415_benjaminstanley_nycschools;

public class SATScores {
    private String mathAvg;
    private String readingAvg;
    private String writingAvg;

    public SATScores(String mathAvg, String readingAvg, String writingAvg) {
        this.mathAvg = mathAvg;
        this.readingAvg = readingAvg;
        this.writingAvg = writingAvg;
    }

    public String getMathAvg() {
        return mathAvg;
    }

    public String getReadingAvg() {
        return readingAvg;
    }

    public String getWritingAvg() {
        return writingAvg;
    }
}
