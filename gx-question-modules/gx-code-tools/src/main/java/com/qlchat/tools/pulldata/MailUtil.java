package com.qlchat.tools.pulldata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.*;

/**
 * 发送邮件工具类
 */
public class MailUtil {
    protected static Logger logger = LoggerFactory.getLogger(MailUtil.class);

    /**
     *
     * @param subject 邮件标题
     * @param content 邮件内容
     * @param to new String[]{"kai.zhang@qlchat.com"}
     */
    public static void sendMail(String subject, String content, String[] to){
        try {
            // 发邮件配置
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("smtp.163.com");
            mailSender.setUsername("wzhangkaiyali@163.com");
            mailSender.setPassword("zhang757530727");

            MimeMessage smm = mailSender.createMimeMessage();
            MimeMessageHelper mmh = new MimeMessageHelper(smm, true);
            mmh.setFrom(mailSender.getUsername());
            mmh.setTo(to);
            mmh.setSubject(subject);
            smm.setText(content, "utf-8", "html");
            // 发送邮件
            mailSender.send(smm);
        }catch (Exception e){
            logger.error("发送邮件异常" + subject, e);
        }
    }

//    public static void sendMail(String subject , Exception e){
//        String sendMail = IConfigUtil.get("exception.mail.send");
//        String sendMailTo = IConfigUtil.get("exception.mail.to");
//        logger.info("发送邮件={}, 是否打开发送开关={}, 收件人={}", new Object[]{subject, sendMail, sendMailTo});
//        if(!"Y".equals(sendMail) || QlchatUtil.isEmpty(sendMail)){
//            return;
//        }
//        StringBuffer sb = new StringBuffer();
//        sb.append(e.toString()).append("</br>");
//        for(StackTraceElement ste : e.getStackTrace()){
//            sb.append(ste.toString()).append("</br>");
//        }
//        sendMail(subject, sb.toString(), sendMailTo.split(","));
//
//    }
}
