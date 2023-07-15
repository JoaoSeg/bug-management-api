package com.segolin.client.controller;

import com.segolin.client.model.BugModel;
import com.segolin.client.service.BugService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;

@RestController
@Slf4j
public class BugController {

    @Autowired
    private BugService bugService;

    @PostMapping("/api/bug/tester")
    public String issue(@RequestBody BugModel bugModel) {
        return bugService.registerBug(bugModel);
    }

    @PutMapping("/api/bug/tester")
    public String update(@RequestBody BugModel bugModel, @RequestParam("id") Long id) {
        return bugService.updateBug(bugModel, id);
    }

    @DeleteMapping("/api/bug/tester")
    public String delete(@RequestParam("id") Long id) {
        bugService.deleteBug(id);
        return "Bug deleted";
    }

    @GetMapping("/api/bug/developer/listbugs")
    public String list() {
        return bugService.listReportedBugs();
    }

    @GetMapping("/api/bug/developer")
    public String bug(@RequestParam("id") Long id, Authentication authentication) {
        return bugService.listBug(id, authentication);
    }

    @PutMapping("/api/bug/developer")
    public String alterBugStatus(@RequestParam("id")  Long id, @RequestParam("status") String status, Authentication authentication) {
        return bugService.alterBugStatus(id, status, authentication);
    }

    @GetMapping("/api/bug/manager")
    public String showBugDash(@RequestParam("status") String status, @RequestParam("begin") Timestamp beginPeriod, @RequestParam("end") Timestamp endPeriod) {
        return bugService.generateDash(status, beginPeriod, endPeriod);
    }

}
