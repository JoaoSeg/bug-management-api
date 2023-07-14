package com.segolin.client.controller;

import com.segolin.client.model.BugModel;
import com.segolin.client.service.BugService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@Slf4j
public class BugController {

    @Autowired
    private BugService bugService;

    @PostMapping("/api/bug/tester")
    public String issue(@RequestBody BugModel bugModel) {
        return bugService.registerBug(bugModel).toString();
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
    public String bug(@RequestParam("id") Long id) {
        return bugService.listBug(id);
    }

    @PutMapping("/api/bug/developer")
    public String alterBugStatus(@RequestParam("id")  Long id, @RequestParam("status") String status) {
        return bugService.alterBugStatus(id, status);
    }

    @GetMapping("/api/bug/manager")
    public String showBugDash(@RequestParam("status") String status, @RequestParam("period") Timestamp period) {
        return bugService.generateDash(status, period);
    }

}
