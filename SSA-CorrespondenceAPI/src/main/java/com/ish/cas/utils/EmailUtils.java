package com.ish.cas.utils;


import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

@Component
public class EmailUtils {
    @Autowired
    private  static  JavaMailSender sender;

    public static void sendMail(String mail, String subject, String body, File file)throws  Exception {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("toMail");
        helper.setSentDate(new Date());
        helper.setSubject(subject);
        helper.setText(body, true);
        helper.addAttachment(file.getName(), file);
        sender.send(message);

    }
}
