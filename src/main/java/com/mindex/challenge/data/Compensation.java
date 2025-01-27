package com.mindex.challenge.data;

import java.util.Date;
import java.util.concurrent.TimeUnit;

//the new Compensation type
public class Compensation {
    //the primary key for each object
    private String compensationID;
    //the foreign key to each employee object
    private String employeeId;
    //the salary of the individual
    private Double salary;
    //the start date of said salary
    private Date effectiveDate;
    //the end date of said salary
    private Date endDate;
    //if start date, end date, and salary, then the total compensation for work can be calculated
    private double totalComp;
    //if the job in question yields stock options
    private boolean stockOptions;

    public Compensation(){  
    }

    public void setcompensationID(String compensationID){
        this.compensationID = compensationID;
    }

    public String getcompensationID(){
        return compensationID;
    }

    public void setEmployeeId(String employeeId){
        this.employeeId = employeeId;
    }

    public String getEmployeeId(){
        return employeeId;
    }

    public void setSalary(Double salary){
        this.salary = salary;
    }

    public Double getSalary(){
        return salary;
    }

    public void seteffectiveDate(Date effectiveDate){
        this.effectiveDate = effectiveDate;
    }

    public Date geteffectiveDate(){
        return effectiveDate;
    }

    public void setendDate(Date endDate){
        this.endDate = endDate;
    }

    public Date getendDate(){
        return endDate;
    }
    /*input:effectiveDate(implicit), endDate(implicit), salary(implicit)
     *output: updating the total compensation for an employee(implicit)
     *description: if all the implicit inputs are provided then the total compensation will be calculated and assigned to the totalComp field
     */
    public void settotalComp(){
        if(effectiveDate != null && endDate != null && salary != null){
            Long milliDifference = endDate.getTime() - effectiveDate.getTime();
            Long daysDifference = TimeUnit.DAYS.convert(milliDifference, TimeUnit.MILLISECONDS);
            double timeDifference = ((double)(daysDifference)/365);
            double comp = timeDifference * salary;
            this.totalComp = comp;
        }
    }

    public double gettotalComp(){
        return totalComp;
    }

    public void setstockOption(boolean stockOptions){
        this.stockOptions = stockOptions;
    }

    public boolean getStockOptions(){
        return stockOptions;
    }
}
