package com.mindex.challenge.data;

import java.util.List;

//the new reporting structure type
public class ReportingStructure {

    //the name of the employee
    String employee;
    //the numer of people who report to the employee in question
    int numberOfReports;

    public void setEmployee(String employee){
        this.employee = employee;
    }

    public String getEmployee(){
        return employee;
    }

    public void setnumberOfReports(int report){
        this.numberOfReports = report;
    }

    public int getnumberOfReport(){
        return this.numberOfReports;
    }
    /*
     * input: the higher ranked employee in question
     * output:integer(the number of people reporting to the employee in question)
     * description:this method uses recursion to count the number of people in direct reports
     */
    public void reportNumber(Employee boss){
        List<Employee> empReportee = boss.getDirectReports();
        if(empReportee != null){
            /*I decided to create a helper method to do the heavy lifting given that the initial method
            will be be called in the API and it should only be called with the employee ID
            */
            this.numberOfReports = recursiveChecker(empReportee);
        }else{
            this.numberOfReports = 0;
        }
        //the name of the employee in question
        this.employee = boss.getFirstName() + " " + boss.getLastName();
    }

    /*input: the employees under the employee in question
     *output: the number of people who work for that employee
     *description: a recursive method that will dig through all direct reporters in order to get the number of people who report to the original employee
     */
    public int recursiveChecker(List<Employee> empReportee){
        int numberOfEmployed = 0;
        for(int i = 0; i < empReportee.size(); i++){
            numberOfEmployed += 1;
            Employee employed = empReportee.get(i);
            if(employed.getDirectReports() != null && employed.getDirectReports().size() != 0){
                numberOfEmployed += recursiveChecker(employed.getDirectReports());
            }
        }
        return numberOfEmployed;
    }
    
}
