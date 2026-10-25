package com.ish.dcs.service;

import com.ish.dcs.bindings.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public interface IDcMgmtService {
        public Integer generateCaseNo(Integer appId);
        public List<String> showAllPlanNames();
        public Integer savePlanSelection(PlanSectionInputs plan);
        public Integer saveIncomeDetails(IncomeInputs income);
        public Integer saveEducationDetails(EducationInputs education);
        public Integer saveChildernDetails(List<ChildInputs> children);
        public DcSummaryReport showSCSummary(Integer caseNo);
}
