package com.ish.cas.service;

import com.ish.cas.bindings.CoSummary;
import com.ish.cas.entity.CitizenApplicationEntity;
import com.ish.cas.entity.CoTriggersEntity;
import com.ish.cas.entity.DcCaseEntity;
import com.ish.cas.entity.ElgibilityDetailsEntity;
import com.ish.cas.repository.IApplicationRegistrationRepository;
import com.ish.cas.repository.ICOTriggerRepository;
import com.ish.cas.repository.IDcCaseRepository;
import com.ish.cas.repository.IElgibilityDeterminationRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CorrespondenceMgmtServiceImpl implements ICorrespondenceMgmtService{

    @Autowired
    private ICOTriggerRepository triggerRepository;
    @Autowired
    private IElgibilityDeterminationRepository elgiRepository;
    @Autowired
    private IDcCaseRepository caseRepo;

    @Autowired
    private IApplicationRegistrationRepository citizenRepo;

    int pendingTriggers=0;
    int successTrigger=0;
    @Override
    public CoSummary processPendingTriggers() {

//        get all pending triggers
        List<CoTriggersEntity> triggerList = triggerRepository.findByTriggerStatus("pending");
        CoSummary summary = new CoSummary();
        summary.setTotalTriggers(triggerList.size());

//        process the trigger in multithreading env... uisng executor framework
        ExecutorService executorService  = Executors.newFixedThreadPool(10);
        ExecutorCompletionService<Object> poll = new ExecutorCompletionService<>(executorService);


//        process all pending triggers
        for(CoTriggersEntity triggerEntity:triggerList){
            poll.submit(()-> {
                    try{
                        processTrigger(summary,triggerEntity);
                        successTrigger++;
                    } catch (Exception e) {
                        e.printStackTrace();
                        pendingTriggers++;
                    }

                    return triggerList;
            });

        }
        summary.setPendingTriggers(pendingTriggers);
        summary.setSuccessTriggers(successTrigger);
        return summary;
    }
    private CitizenApplicationEntity processTrigger(CoSummary summary, CoTriggersEntity triggerEntity)throws Exception{
        CitizenApplicationEntity citizenApplicationEntity = null;
        //            get elgibility details based on caseNo
       ElgibilityDetailsEntity  elgibiEntity= elgiRepository.findByCaseNo(triggerEntity.getCaseNo());
//            get appId based on caseNo
        Optional<DcCaseEntity>  optCaseEntity = caseRepo.findById(triggerEntity.getCaseNo());
        if(optCaseEntity.isPresent()){
            DcCaseEntity caseEntity = optCaseEntity.get();
            Integer appId = caseEntity.getAppId();
//                get the citizen details based on appId
            Optional<CitizenApplicationEntity> optCitizenEntity= citizenRepo.findById(appId);
            if(optCitizenEntity.isPresent()){
                citizenApplicationEntity = optCitizenEntity.get();
            }
        }

//            generate pdf docs having elgibility details and send to email
            generatePdfAndSendMail(elgibiEntity,citizenApplicationEntity);
        return citizenApplicationEntity;
    }
    private void  generatePdfAndSendMail(ElgibilityDetailsEntity elgibiEntity, CitizenApplicationEntity citizenEntity) throws Exception {
        Document document = new Document(PageSize.A4);
        File file = new File(elgibiEntity.getCaseNo()+".pdf");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        PdfWriter.getInstance(document,fos);
        document.open();

//        define font of paragraph
        Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
        font.setSize(30);
        font.setColor(Color.CYAN);


//        create the paragraph having content and above font style
        Paragraph paragraph = new Paragraph("plan approval /denial communication");
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
//        add para to data
        document.add(paragraph);
//        display search result as the pdf table
        PdfPTable table = new PdfPTable(10);
        table.setWidthPercentage(70);
        table.setWidths(new float[]{3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,});
        table.setSpacingBefore(2.0f);

//        prepare heading row cells in the pdf table
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.gray);
        cell.setPadding(5);

        Font cellFont= FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        cellFont.setColor(Color.BLACK);

        cell.setPhrase(new Phrase("traceId",cellFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("holderName",cellFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("holderSSN",cellFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("planName",cellFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("planStatus",cellFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("planStartDate",cellFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("planEndDate",cellFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("BenifitAmount",cellFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("DenialReason",cellFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("caseNo",cellFont));
        table.addCell(cell);

//        add data cells to pdf table
        table.addCell(String.valueOf(elgibiEntity.getEdTraceId()));
        table.addCell(String.valueOf(elgibiEntity.getCaseNo()));
        table.addCell(elgibiEntity.getHolderName());
        table.addCell(String.valueOf(elgibiEntity.getHolderSSN()));
        table.addCell(elgibiEntity.getPlanName());
        table.addCell(elgibiEntity.getPlanStatus());
        table.addCell(String.valueOf(elgibiEntity.getPlanStartDate()));
        table.addCell(String.valueOf(elgibiEntity.getPlanEndDate()));
        table.addCell(String.valueOf(elgibiEntity.getBenifitAmt()));
        table.addCell(elgibiEntity.getDenialReson());
//        add table to document
        document.add(table);
//        close the documnet
        document.close();

//        send the generated PDF doc as  the email message
        String subject = "plan approval/denial mail";
        String body = "hello Mr/Miss/Mrs. "+citizenEntity.getFullName()+" , this mail contain complete details plan approval or denital";
//        EmailUtils.sendMail(citizenEntity.getEmail(),subject,body,file);
//        update Cotrigger table
        updateCotrigger(elgibiEntity.getCaseNo(),file);

    }

    //      store pdf document in co_triggers table update triggers status to "complete"
    private void updateCotrigger(Integer caseNo, File file) throws  Exception{
//        check trigger avibility based on the case no
        CoTriggersEntity triggersEntity = triggerRepository.findByCaseNo(caseNo);

//        get byte[] representing pdf doc content
        byte[] pdfcontent = new byte[(int) (file.length())];
        FileInputStream fis = new FileInputStream(file);
        fis.read(pdfcontent);
        if(triggersEntity!=null){
            triggersEntity.setCoNoticePdf(pdfcontent);
            triggersEntity.setTriggerStatus("completed");
            triggerRepository.save(triggersEntity);
        }
        fis.close();
    }
}
