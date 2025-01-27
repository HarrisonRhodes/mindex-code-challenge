package com.mindex.challenge.controller;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee create request for id [{}]", id);

        return employeeService.read(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }

    @GetMapping("/employee/numberOfReports/{id}")
    public ReportingStructure generateReport(@PathVariable String id){
        LOG.debug("Received employee create report request for id [{}]", id);
        return employeeService.generateReport(id);
    }

    @PostMapping("/employee/compensation")
    public Compensation compensationCreate(@RequestBody Compensation compensation){
        LOG.debug("Received compensation create request for id [{}]", compensation.getEmployeeId());
        return employeeService.createCompensation(compensation);
    }
    
    @GetMapping("/employee/findCompensation/{searchSwitch}/{id}")
    public Compensation[] findCompensation(@PathVariable boolean searchSwitch, @PathVariable String id){
        if(searchSwitch == true){
            LOG.debug("Searching compensation records for employee with ID [{}]", id);
        }else{
            LOG.debug("Searching compensation records for compensation record with ID [{}]", id);
        }
        return employeeService.compensationInfo(searchSwitch, id);
    }
}
