package com.segolin.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BugModel {

    private String title;
    private String description;
    private String status;
    private Timestamp creation;
    private Timestamp lastUpdate;

}
