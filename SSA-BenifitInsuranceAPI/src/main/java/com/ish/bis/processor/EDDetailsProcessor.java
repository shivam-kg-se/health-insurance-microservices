package com.ish.bis.processor;

import com.ish.bis.bindings.ElgibilityDetails;
import com.ish.bis.entity.ElgibilityDetailsEntity;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class EDDetailsProcessor implements ItemProcessor<ElgibilityDetailsEntity, ElgibilityDetails> {

    private static final Logger logger = LoggerFactory.getLogger(EDDetailsProcessor.class);

    @Override
    public ElgibilityDetails process(ElgibilityDetailsEntity item) throws Exception {
        try {
            // STEP 1: Validate input data
            if (item == null || item.getCaseNo() == null) {
                logger.warn("Skipping null or invalid record");
                return null;
            }

            // STEP 2: Check eligibility status (BUSINESS LOGIC)
            if (!item.getPlanStatus().equalsIgnoreCase("approved")) {
                logger.debug("Skipping non-approved case: {} with status: {}",
                        item.getCaseNo(), item.getPlanStatus());
                return null;  // Skip non-approved records
            }

            // STEP 3: Validate SSN (BUSINESS LOGIC)
            if (item.getHolderSSN() == null || item.getHolderSSN() <= 0) {
                logger.warn("Invalid SSN for case {}: {}", item.getCaseNo(), item.getHolderSSN());
                return null;
            }

            // STEP 4: Copy and transform data
            ElgibilityDetails details = new ElgibilityDetails();
            BeanUtils.copyProperties(item, details);

            // STEP 5: Apply business logic - Benefit amount calculation
            double baseAmount = item.getBenifitAmt() != null ? item.getBenifitAmt() : 0.0;

            // Calculate final benefit (example: apply 5% bonus for approved status)
            double finalBenefit = baseAmount * 1.05;
            details.setBenifitAmt(finalBenefit);

            // STEP 6: Validate final benefit amount (BUSINESS LOGIC)
            if (finalBenefit < 100) {
                logger.warn("Benefit amount too low for case {}: ${}",
                        item.getCaseNo(), finalBenefit);
                return null;  // Skip low benefit amounts
            }

            logger.info("Successfully processed case {} with benefit: ${}",
                    item.getCaseNo(), finalBenefit);

            return details;

        } catch (Exception e) {
            logger.error("Error processing case {}: {}", item.getCaseNo(), e.getMessage());
            // Return null to skip problematic records, or throw exception to fail the step
            return null;
        }
    }
}
