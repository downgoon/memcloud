/**
 * 
 */
package io.memcloud.stats.notification.impl;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author ganghuawang
 * 邮件发送实现类
 */
public class EmailPusher {

	private MimeMessage mimeMsg; // MIME邮件对象

	private Session session; // 邮件会话对象

	private Properties props; // 系统属性
	
	private Multipart mp; // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象
	
	private static EmailPusher instance = null;
	
	private EmailPusher(){}
	
	public static EmailPusher getInstance(){
		if(instance == null){
			synchronized (EmailPusher.class) {
				if(instance == null)
					instance = new EmailPusher();
			}
		}
		return instance ;
	}
	
	public  void sendEmail(String email, String content){
		if(true) return ;
		if (props == null)
			props = System.getProperties(); // 获得系统属性对象
		// 设置SMTP主机
		props.put("mail.smtp.host", "10.1.33.241"); 
		props.put("mail.smtp.auth", "false");
		// 获得邮件会话对象
		session = Session.getDefaultInstance(props, null); 
		// 创建MIME邮件对象
		mimeMsg = new MimeMessage(session); 
		mp = new MimeMultipart();
		try {
			// 设置主题
			mimeMsg.setSubject("mailSubject",	"GBK");
			// 设置邮件正文
			BodyPart bp = new MimeBodyPart();
			bp.setContent(content, "text/html;charset=GBK");
			mp.addBodyPart(bp);
			// 设置发信人
			mimeMsg.setFrom(new InternetAddress("no-reply@example.com")); 
			// 设置接收人
			mimeMsg.setRecipients(Message.RecipientType.TO, email);
			
			
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();
			Session mailSession = Session.getInstance(props, null);
			Transport transport = mailSession.getTransport("smtp");
			// 连接SMTP
			transport.connect((String) props.get("mail.smtp.host"), "no-reply", "11111111");
			// 发送邮件
			transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
			transport.close();
		} catch (MessagingException e) {
			e.printStackTrace();
		} 
	}
	
	public static void main(String[] args) {
		EmailPusher.getInstance().sendEmail("ganghuawang@example.com", "test");
	}
	
}
