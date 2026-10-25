package com.ish.cas.bindings;

import lombok.Data;

@Data
public class CoSummary {
    private Integer totalTriggers;
    private Integer successTriggers;
    private Integer pendingTriggers;
}
