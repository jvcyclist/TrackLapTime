package com.karas.tracklaptime;

import java.util.ArrayList;
import java.util.List;

public class ResultFileMapper {

    private List<Integer> amountOfLaps = new ArrayList<>();
    private List<Double> expectedResults = new ArrayList<>();
    private List<Double> givenResults = new ArrayList<>();
    private String fullTextFileContent;
    private String fullTime;

    public void setFullTime(String fullTime){
        this.fullTime = fullTime;
    }

    private void createFirstRow(){

    }

    private void createExpectedResultsRow(){

    }

    private void createGivenResultsRow(){

    }

    public void setExpectedResults(List<Double> expectedResults){
        this.expectedResults.addAll(expectedResults);
    }

    public void addGivenResult(Double result) {
        this.givenResults.add(result);
    }

    public void test(){
        System.out.println("Siema");
    }

}
