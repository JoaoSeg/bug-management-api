package com.segolin.client.service;

import com.segolin.client.entity.Bug;
import com.segolin.client.model.BugModel;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public interface BugService {
    Bug registerBug(BugModel bugModel);

    String updateBug(BugModel bugModel, Long id);

    void deleteBug(Long id);

    String listReportedBugs();

    String listBug(Long id, Authentication authentication);

    String alterBugStatus(Long id, String status, Authentication authentication);

    String generateDash(String status, Timestamp beginPeriod, Timestamp endPeriod);
}
