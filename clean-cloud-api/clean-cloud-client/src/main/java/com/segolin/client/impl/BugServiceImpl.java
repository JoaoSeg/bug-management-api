package com.segolin.client.impl;

import com.segolin.client.entity.Bug;
import com.segolin.client.entity.Employee;
import com.segolin.client.entity.VerificationToken;
import com.segolin.client.model.BugModel;
import com.segolin.client.repository.BugRepository;
import com.segolin.client.repository.EmployeeRepository;
import com.segolin.client.service.BugService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Bug registerBug(BugModel bugModel) {
        Bug bug = new Bug();
        bug.setTitle(bugModel.getTitle());
        bug.setDescription(bugModel.getDescription());
        bug.setCreation(bugModel.getCreation());
        bug.setStatus(bugModel.getStatus());

        bugRepository.save(bug);
        return bug;
    }

    @Override
    public void updateBug(BugModel bugModel, Long id) throws UsernameNotFoundException {
        if (bugRepository.findById(id).isEmpty()) {
            throw new UsernameNotFoundException("Bug not found");
        }

        Bug bug = new Bug();
        bug.setTitle(bugModel.getTitle());
        bug.setDescription(bugModel.getDescription());
        bug.setCreation(bugModel.getCreation());
        bug.setStatus(bugModel.getStatus());
        bug.setLastUpdate(Timestamp.from(Instant.now()));

        bugRepository.save(bug);
    }

    @Override
    public void deleteBug(Long id) {
        if (bugRepository.findById(id).isPresent()) {
            bugRepository.deleteById(id);
        } else {
            throw new UsernameNotFoundException("Bug not found");
        }
    }

    @Override
    public String listReportedBugs() {
        List<Bug> bugs = bugRepository.findAllByStatus("reported");
        return Arrays.deepToString(bugs.toArray());
    }

    @Override
    public String listBug(Long id) {
        if (bugRepository.findById(id).isEmpty()) {
            throw new UsernameNotFoundException("Bug not found");
        }
        Bug bug = bugRepository.findById(id).get();
        Employee employee = bug.getEmployee();

        employeeRepository.save(employee);
        return bug.toString();
    }

    @Override
    public String alterBugStatus(Long id, String status) {
        if (bugRepository.findById(id).isEmpty()) {
            throw new UsernameNotFoundException("Bug not found");
        }

        Bug bug = bugRepository.findById(id).get();

        if(bug.getEmployee() != null) {
            return "Bug is already being fixed by " + bug.getEmployee().getFirstName();
        }

        if (bug.getStatus().equalsIgnoreCase("reported") && !status.equalsIgnoreCase("analyzes")) {
            return "Bug status must follow flow";
        }

        bug.setStatus(status);
        bugRepository.save(bug);

        return "Bug status resolved";
    }

    @Override
    public String generateDash(String status, Timestamp period) {
        return ;
    }

}
