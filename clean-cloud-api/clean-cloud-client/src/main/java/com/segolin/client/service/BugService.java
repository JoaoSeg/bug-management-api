package com.segolin.client.service;

import com.segolin.client.entity.Bug;
import com.segolin.client.model.BugModel;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public interface BugService {
    Bug registerBug(BugModel bugModel);

    String updateBug(BugModel bugModel, Long id);

    void deleteBug(Long id);

    String listReportedBugs();

    String listBug(Long id);

    String alterBugStatus(Long id, String status);

    String generateDash(String status, Timestamp period);
}
