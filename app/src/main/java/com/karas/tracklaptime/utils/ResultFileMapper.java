package com.karas.tracklaptime.utils;

import android.text.TextUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ResultFileMapper {

    private static final String DELIMITER = ",";
    private List<Double> expectedResults = new ArrayList<>();
    private List<Double> givenResults = new ArrayList<>();
    private List<String> firstRowList = new ArrayList<>();
    private List<String> secondRowList = new ArrayList<>();
    private List<String> thirdRowList = new ArrayList<>();


    private String fullTime;
    private String timestamp;

    public ResultFileMapper() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.timestamp = timestamp.toString();
    }

    public String getResultsAsCsvContent() {
        createFirstRow();
        createSecondRow();
        createThirdRow();
        return TextUtils.join(DELIMITER, this.firstRowList)
                + "\r\n"
                + TextUtils.join(DELIMITER, this.secondRowList)
                + "\r\n"
                + TextUtils.join(DELIMITER, this.thirdRowList)
                + "\r\n";
    }

    public String getFileName() {
        return this.timestamp
                .replace("-", "")
                .replace(" ", "_")
                .replace(":", "")
                .replace(".", "")
                + "_TRACK_CYCLING"
                + ".csv";
    }

    public void setFullTime(String fullTime) {
        this.fullTime = fullTime;
    }

    private void createFirstRow() {
        this.firstRowList.add(timestamp);
        this.firstRowList.add("OKRAZENIE");
        this.firstRowList.add(" ");
        for (int i = 1; i <= givenResults.size(); i++) {
            this.firstRowList.add(String.valueOf(i));
        }
    }

    private void createSecondRow() {
        this.secondRowList.add(timestamp);
        this.secondRowList.add("ZALOZENIA");
        this.secondRowList.add(" ");
        if (!expectedResults.isEmpty()) {
            for (Double expectedResult : expectedResults
            ) {
                secondRowList.add(String.valueOf(expectedResult));
            }
        } else {
            for (int i = 1; i <= givenResults.size(); i++) {
                expectedResults.add(0.0);
            }
        }
    }

    private void createThirdRow() {
        this.thirdRowList.add(timestamp);
        this.thirdRowList.add("UZYSKANY CZAS");
        this.thirdRowList.add(fullTime);
        for (Double givenResult : givenResults
        ) {
            thirdRowList.add(String.valueOf(givenResult));
        }
    }

    public void setExpectedResults(List<Double> expectedResults) {
        this.expectedResults.addAll(expectedResults);
    }

    public void addGivenResult(Double result) {
        this.givenResults.add(result);
    }


}
