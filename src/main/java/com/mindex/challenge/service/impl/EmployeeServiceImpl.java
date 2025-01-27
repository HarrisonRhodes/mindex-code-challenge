package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    /*input: the employeeID that allows the employee object to be pulled
     *output: report with the employee name and the number of employees under the employee in question
     *description: counts the number of reporters to the employee in question
     */
    @Override
    public ReportingStructure generateReport(String id) {
        Employee employee = read(id);
        ReportingStructure response = new ReportingStructure();
        LOG.debug("generating number of reporters to employee");
        response.reportNumber(employee);
        return response;
    }

     /*input: the compensation of a pre-existing employee
     *output: a new compensation record in the Compensation repository database
     *description: if the compensation record is filled out properly, a new compensation record for an existing employee will be created in the Compensation database
     */
    @Override
    public Compensation createCompensation(Compensation compensation){
        //checking if the individual exists in the current context
        if(employeeRepository.findByEmployeeId(compensation.getEmployeeId()) == null){
            throw new RuntimeException("Invalid employeeId: " + compensation.getEmployeeId());
        }
        //check if the salary is specified
        if(compensation.getSalary() == null){
            throw new RuntimeException("Salary must be listed for compensation to be legitimate compensation");
        }
        //check if the start date is specified
        if(compensation.geteffectiveDate() == null){
            throw new RuntimeException("Effective date must be listed for compensation to allow for proper compensation of the individual");
        }
        //checking if the end date is specified or not and if so, is it lower than the start date
        if(compensation.getendDate() == null || compensation.getendDate().getTime() > compensation.geteffectiveDate().getTime()){
            compensation.setcompensationID(UUID.randomUUID().toString());
            //if both dates are specified, calculate the total compensation for the job
            if(compensation.getendDate() != null && compensation.geteffectiveDate() != null){
                compensation.settotalComp();
            }
            return compensationRepository.insert(compensation);
        }else{
            throw new RuntimeException("The end date cannot be later than the effective date");
        }
    }

    /*input: a boolean that allows you to search by either compensationID(false) or employeeID(true), a string representing either the compensationID or employeeID in question
     *output: a list of all the compensation info dictated by the boolean and the id
     *description: depending on the boolean value you give, the compensation info(could be singular or array based) based on either the employeeID or the compensationID will be returned
     */
    @Override 
    public Compensation[] compensationInfo(boolean searchSwitch, String id){
        if(searchSwitch == false){
            Compensation[] existingComp = {compensationRepository.findBycompensationID(id)};
            return existingComp;
        }else{
            Compensation[] existingComps = compensationRepository.findByemployeeId(id);
            return existingComps;
        }
    }
}
