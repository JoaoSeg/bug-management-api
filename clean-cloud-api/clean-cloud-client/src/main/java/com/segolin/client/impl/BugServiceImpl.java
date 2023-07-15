package com.segolin.client.impl;

import com.nimbusds.jose.shaded.gson.Gson;
import com.segolin.client.entity.Bug;
import com.segolin.client.entity.Employee;
import com.segolin.client.model.BugModel;
import com.segolin.client.repository.BugRepository;
import com.segolin.client.repository.EmployeeRepository;
import com.segolin.client.service.BugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
public class BugServiceImpl implements BugService {

    @Autowired
    public BugRepository bugRepository;

    @Autowired
    public EmployeeRepository employeeRepository;

    @Override
    public String registerBug(BugModel bugModel) {
        Gson gson = new Gson();
        Bug bug = new Bug();
        bug.setTitle(bugModel.getTitle());
        bug.setDescription(bugModel.getDescription());
        bug.setCreation(Timestamp.from(Instant.now()));
        bug.setStatus("reported");

        bugRepository.save(bug);

        return gson.toJson(bug);
    }

    @Override
    public String updateBug(BugModel bugModel, Long id) throws UsernameNotFoundException {
        if (bugRepository.findById(id).isEmpty()) {
            throw new UsernameNotFoundException("Bug not found");
        }

        Bug tempBug = bugRepository.findById(id).get();

        if (tempBug.getStatus().equalsIgnoreCase("analyze")) {
            return "Bug is still in analyze";
        }

        Bug bug = new Bug();
        bug.setId(id);
        bug.setTitle(bugModel.getTitle());
        bug.setDescription(bugModel.getDescription());
        bug.setLastUpdate(Timestamp.from(Instant.now()));
        bug.setStatus(tempBug.getStatus());
        bug.setCreation(tempBug.getCreation());

        bugRepository.save(bug);

        return "Bug edited successfully";
    }

    @Override
    public void deleteBug(Long id) {
        if (bugRepository.findById(id).isEmpty()) {
            throw new UsernameNotFoundException("Bug not found");
        }

        bugRepository.deleteById(id);
    }

    @Override
    public String listReportedBugs() {
        List<Bug> bugs = bugRepository.findAllByStatus("reported");
        Gson gson = new Gson();
        return gson.toJson(bugs);
    }

    @Override
    public String listBug(Long id, Authentication authentication) {
        if (bugRepository.findById(id).isEmpty()) {
            throw new UsernameNotFoundException("Bug not found");
        }
        Employee employee = employeeRepository.findByEmail(authentication.getName());
        Bug bug = bugRepository.findById(id).get();
        bug.setEmployee(employee);
        bug.setLastUpdate(Timestamp.from(Instant.now()));
        bug.setStatus("analyze");

        bugRepository.save(bug);
        Gson gson = new Gson();
        return gson.toJson(bug);
    }

    @Override
    public String alterBugStatus(Long id, String status, Authentication authentication) {
        if (bugRepository.findById(id).isEmpty()) {
            throw new UsernameNotFoundException("Bug not found");
        }

        Bug bug = bugRepository.findById(id).get();

        if(bug.getEmployee() != null && !bug.getEmployee().getEmail().equalsIgnoreCase(authentication.getName())) {
            return "Bug is already being fixed by " + bug.getEmployee().getFirstName();
        }

        if (bug.getStatus().equalsIgnoreCase("reported") && !status.equalsIgnoreCase("analyzes")) {
            return "Bug status must follow flow";
        }

        bug.setLastUpdate(Timestamp.from(Instant.now()));
        bug.setStatus(status);
        bugRepository.save(bug);

        return "Bug status resolved";
    }

    @Override
    public String generateDash(String status, Timestamp beginPeriod, Timestamp endPeriod) {

        List<Bug> bugList = bugRepository.findAllByStatusAndCreationBetween(status, beginPeriod, endPeriod);

        Gson gson = new Gson();

        return gson.toJson(bugList);
    }

}
