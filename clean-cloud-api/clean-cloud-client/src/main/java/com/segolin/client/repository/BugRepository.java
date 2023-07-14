package com.segolin.client.repository;

import com.segolin.client.entity.Bug;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface BugRepository extends JpaRepository<Bug, Long> {
    List<Bug> findAllByStatus(String reported);

    List<Bug> findAllByStatusAndCreationBetween(String status, Timestamp beginPeriod, Timestamp endPeriod);
}
