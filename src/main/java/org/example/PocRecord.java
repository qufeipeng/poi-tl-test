package org.example;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PocRecord {
    private String status;
    private String customer;
    private String project;
    private String risk;
    private String plan;
    private String evaluate;
    private String sales;
    private String sa;
    private String poc;
    private Integer progress;
    private String workContent;

    public PocRecord(String status, String customer, String project, String risk, String plan,
                     String evaluate, String sales, String sa, String poc,Integer progress, String workContent) {
        this.status = status;
        this.customer = customer;
        this.project = project;
        this.risk = risk;
        this.plan = plan;
        this.evaluate = evaluate;
        this.sales = sales;
        this.sa = sa;
        this.poc = poc;
        this.progress = progress;
        this.workContent = workContent;
    }



}
