package com.virtualatm.atmsimulator.dto.card;

import lombok.Data;

@Data
public class IssueCardRequest {
    private String pin; // optional
}
