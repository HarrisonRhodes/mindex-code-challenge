package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String employeeReporters;
    private String employeeCompensation;
    private String employeeCompensationFinder;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        employeeReporters = "http://localhost:" + port + "/employee/numberOfReports/{id}";
        employeeCompensation = "http://localhost:" + port + "/employee/compensation";
        employeeCompensationFinder = "http://localhost:" + port + "/employee/findCompensation/{searchSwitch}/{id}";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    @Test
    public void testGenerateReportingStructure(){
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        //checking for subordinates
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
        ReportingStructure results = restTemplate.getForEntity(employeeReporters,ReportingStructure.class , createdEmployee.getEmployeeId()).getBody();
        assertEquals(results.getnumberOfReport(), 0);
        assertEquals(results.getEmployee(), "John Doe" );
    }

    @Test
    public void testCompensationFeatures(){
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("James");
        testEmployee.setLastName("Richards");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Helpdesk");
        testEmployee.setDirectReports(null);

        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        Compensation originalSalary = new Compensation();
        //checking the compensation create feature
        originalSalary.setEmployeeId(createdEmployee.getEmployeeId());
        originalSalary.setSalary(67500.0);
        originalSalary.seteffectiveDate(new Date(2025, 1, 17));
        originalSalary.setstockOption(true);
        Compensation compensationRecord = restTemplate.postForEntity(employeeCompensation, originalSalary, Compensation.class).getBody();
        assertEquals(compensationRecord.getEmployeeId(), createdEmployee.getEmployeeId());
        assertEquals(originalSalary.getSalary().toString(), "67500.0");
        
        //checking if the create can differentiate between good and bad creates
        originalSalary.seteffectiveDate(null);
        assertTrue(restTemplate.postForEntity(employeeCompensation, originalSalary, Compensation.class).getStatusCode().isError());
        
        //checking the search by compensation ID
        Compensation[] compensationByCompID = restTemplate.getForEntity(employeeCompensationFinder,Compensation[].class , false, compensationRecord.getcompensationID()).getBody();
        assertEquals(compensationByCompID[0].getcompensationID(), compensationRecord.getcompensationID());

        //checking the search by employee ID
        Compensation[] compensationByEmpID = restTemplate.getForEntity(employeeCompensationFinder,Compensation[].class , true, compensationRecord.getEmployeeId()).getBody();
        assertEquals(compensationByEmpID[0].getEmployeeId(), originalSalary.getEmployeeId());
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }


}
