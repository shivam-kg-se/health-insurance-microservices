package com.ish.eds.services;

import com.ish.eds.bindings.ElgibilityDetailsOutput;

public interface IElgibilityDeterminationMgmtService {
    public ElgibilityDetailsOutput determineElgibility(Integer caseNo);
}
