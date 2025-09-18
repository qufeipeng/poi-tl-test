package org.example;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PocRecord {
    private String status;
    private String customer;
    private String project;
    private String risk;
    private String done;
    private String sales;
    private String sa;
    private String poc;
    private Integer progress;

    public PocRecord(String status, String customer, String project, String risk,
                     String done, String sales, String sa, String poc,Integer progress) {
        this.status = status;
        this.customer = customer;
        this.project = project;
        this.risk = risk;
        this.done = done;
        this.sales = sales;
        this.sa = sa;
        this.poc = poc;
        this.progress = progress;
    }



}
