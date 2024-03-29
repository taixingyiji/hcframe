package com.taixingyiji.base.common.utils;


import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class MailOperate {
    /**
      * 以文本格式发送邮件
      * @param mailInfo 待发送的邮件的信息
      */
        public boolean sendTextMail(MailBean mailInfo) {
          // 判断是否需要身份认证
          MailAuthenticator authenticator = null;
          Properties pro = mailInfo.getProperties();
          if (mailInfo.isValidate()) {
          // 如果需要身份认证，则创建一个密码验证器
            authenticator = new MailAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
          }
          // 根据邮件会话属性和密码验证器构造一个发送邮件的session
          Session sendMailSession = Session.getDefaultInstance(pro,authenticator);
          try {
          // 根据session创建一个邮件消息
          Message mailMessage = new MimeMessage(sendMailSession);
          // 创建邮件发送者地址
          Address from = new InternetAddress(mailInfo.getFromAddress());
          // 设置邮件消息的发送者
          mailMessage.setFrom(from);
          // 创建邮件的接收者地址，并设置到邮件消息中
          List<String> toAddress=null;
          if(mailInfo.getToAddress().size()>1){
              toAddress=mailInfo.getToAddress();
              Address[] address =new InternetAddress[toAddress.size()];
                 for (int i = 0; i < toAddress.size(); i++) {
                     address[i]=new InternetAddress(toAddress.get(i));
                }
              mailMessage.setRecipients(Message.RecipientType.TO,address);
          }else{
              toAddress=mailInfo.getToAddress();
          Address to = new InternetAddress(toAddress.get(0));
          // Message.RecipientType.TO属性表示接收者的类型为TO
          mailMessage.setRecipient(Message.RecipientType.TO,to);
          }
          // 设置邮件消息的主题
          mailMessage.setSubject(mailInfo.getSubject());
          // 设置邮件消息发送的时间
          mailMessage.setSentDate(new Date());
          // 设置邮件消息的主要内容
          String mailContent = mailInfo.getContent();
          mailMessage.setText(mailContent);
          // 发送邮件
          Transport.send(mailMessage);
                return true;
          } catch (MessagingException ex) {
//              ex.printStackTrace();
              return false;
          }
//          return false;
        }

        /**
          * 以HTML格式发送邮件
          * @param mailInfo 待发送的邮件信息
         * @throws UnsupportedEncodingException
          */
        public static boolean sendHtmlMail(MailBean mailInfo) throws UnsupportedEncodingException{
          // 判断是否需要身份认证
          MailAuthenticator authenticator = null;
          Properties pro = mailInfo.getProperties();
          //如果需要身份认证，则创建一个密码验证器
          if (mailInfo.isValidate()) {
            authenticator = new MailAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
          }
          // 根据邮件会话属性和密码验证器构造一个发送邮件的session
          Session sendMailSession = Session.getInstance(pro,authenticator);

          try {
          // 根据session创建一个邮件消息
          Message mailMessage = new MimeMessage(sendMailSession);
          // 创建邮件发送者地址
          Address from = new InternetAddress(mailInfo.getFromAddress());
          // 设置邮件消息的发送者
          mailMessage.setFrom(from);
          // 创建邮件的接收者地址，并设置到邮件消息中
          List<String> toAddress=null;
          if(mailInfo.getToAddress().size()>1){
              toAddress=mailInfo.getToAddress();
             Address[] address =new InternetAddress[toAddress.size()];
             for (int i = 0; i < toAddress.size(); i++) {
                 address[i]=new InternetAddress(toAddress.get(i));
            }
              mailMessage.setRecipients(Message.RecipientType.TO,address);
          }else{
              toAddress=mailInfo.getToAddress();
          Address to = new InternetAddress(toAddress.get(0));
          // Message.RecipientType.TO属性表示接收者的类型为TO
          mailMessage.setRecipient(Message.RecipientType.TO,to);
          }

          // 设置邮件消息的主题
          mailMessage.setSubject(mailInfo.getSubject());
          // 设置邮件消息发送的时间
          mailMessage.setSentDate(new Date());
          // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
          Multipart mainPart = new MimeMultipart();
          // 创建一个包含HTML内容的MimeBodyPart
          BodyPart html = new MimeBodyPart();
          // 设置HTML内容
          html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
          mainPart.addBodyPart(html);
          if(mailInfo.getAttachFileNames()!=null){
              List<String> attachFileNames = mailInfo.getAttachFileNames();
              for (String path : attachFileNames) {
                  html=new MimeBodyPart();
                  FileDataSource fds=new FileDataSource(path); //得到数据源
                  html.setDataHandler(new DataHandler(fds)); //得到附件本身并至入BodyPart
                  //此处是为了解决附件中文名乱码的问题
                  String fileName= MimeUtility.encodeText(fds.getName());
                  html.setFileName(fileName);  //得到文件名同样至入BodyPart
                  mainPart.addBodyPart(html);
            }
          }

          // 将MiniMultipart对象设置为邮件内容
          mailMessage.setContent(mainPart);
          // 发送邮件
          Transport.send(mailMessage);
          return true;
          } catch (MessagingException ex) {
              ex.printStackTrace();
          }
          return false;
        }


}
