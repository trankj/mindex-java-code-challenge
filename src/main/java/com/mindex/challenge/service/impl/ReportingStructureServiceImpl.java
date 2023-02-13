package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }


        ReportingStructure reportingStructure = new ReportingStructure(employee, getNumberOfReports(0, employee));
        int t = reportingStructure.getNumberOfReports();
        return reportingStructure;
    }

    private Integer getNumberOfReports(Integer numberOfReports, Employee employee) {
        if (employee.getDirectReports() == null) {
            return numberOfReports;
        }
        for (Employee e : employee.getDirectReports()) {
            numberOfReports++;
            Employee info = employeeRepository.findByEmployeeId(e.getEmployeeId());
            if (info.getDirectReports() != null && info.getDirectReports().size() > 0) {
                numberOfReports = getNumberOfReports(numberOfReports, info);
            }
        }
        return numberOfReports;
    }
}
