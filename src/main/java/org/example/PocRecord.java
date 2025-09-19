package org.example;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class PocRecord {
    private String owner;
    private String status;
    private String customer;
    private String project;
    private String risk;
    private String todoRisk;
    private String plan;
    private String evaluate;
    private String sales;
    private String sa;
    private String poc;
    private Integer progress;
    private String workContent;
    private Date pocStartDt;
    private Date pocEndDt;
    private Date onlineDt;

    public PocRecord(String owner, String status, String customer, String project, String risk, String todoRisk, String plan,
                     String evaluate, String sales, String sa, String poc,Integer progress, String workContent,
                     Date pocStartDt, Date pocEndDt, Date onlineDt) {
        this.owner = owner;
        this.status = status;
        this.customer = customer;
        this.project = project;
        this.risk = risk;
        this.todoRisk = todoRisk;
        this.plan = plan;
        this.evaluate = evaluate;
        this.sales = sales;
        this.sa = sa;
        this.poc = poc;
        this.progress = progress;
        this.workContent = workContent;
        this.pocStartDt = pocStartDt;
        this.pocEndDt = pocEndDt;
        this.onlineDt = onlineDt;
    }



}
