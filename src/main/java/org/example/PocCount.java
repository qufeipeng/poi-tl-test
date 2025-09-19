package org.example;

import lombok.Data;

@Data
public class PocCount {
    private Long count;
    private String detail;

    public PocCount(Long count, String detail) {
        this.count = count;
        this.detail = detail;
    }
}
