package com.enbiz.common.base.mail;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

public class MailSender {
	private MailProperties mailProperties;
	private TemplateEngine templateEngine;
	
	public MailSender(MailProperties mailProperties, TemplateEngine templateEngine) {
		this.mailProperties = mailProperties;
		this.templateEngine = templateEngine;
	}

	/**
	 * 작성된 html 컨텐츠를 메일로 전송합니다. MLB 전용.
	 * @param toMail
	 * @param subject
	 * @param contents
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	public void sendMlb(String toMail, String subject, String contents) throws UnsupportedEncodingException, MessagingException {
		send(mailProperties.getFromMailMlb(), mailProperties.getFromNameMlb(), toMail, subject, contents);
	}
	
	/**
	 * 작성된 html 컨텐츠를 메일로 전송합니다. DX 전용.
	 * @param toMail
	 * @param subject
	 * @param contents
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	public void sendDx(String toMail, String subject, String contents) throws UnsupportedEncodingException, MessagingException {
		send(mailProperties.getFromMailDx(), mailProperties.getFromNameDx(), toMail, subject, contents);
	}
	
	/**
	 * 작성된 html 컨텐츠를 메일로 전송합니다.
	 * @param fromMail
	 * @param fromName
	 * @param toMail
	 * @param subject
	 * @param contents
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	public void send(String fromMail, String fromName, String toMail, String subject, String contents) throws UnsupportedEncodingException, MessagingException {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mailProperties.getHost());
        javaMailSender.setPort(mailProperties.getPort());
        javaMailSender.setUsername(mailProperties.getUserName());
        javaMailSender.setPassword(mailProperties.getPassword());

        InternetAddress from = new InternetAddress(fromMail, fromName);
        InternetAddress to = new InternetAddress(toMail);
        
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
        messageHelper.setFrom(from);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(contents, true);

        javaMailSender.send(mimeMessage);
	}	

	/**
	 * 템플릿과 템플릿 변수를 조합하여 컨텐츠를 구성하여 메일로 전송합니다. MLB 전용.
	 * @param toMail
	 * @param subject
	 * @param template
	 * @param variables
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	public void sendTemplateMlb(String toMail, String subject, String template, Map<String, Object> variables) throws UnsupportedEncodingException, MessagingException {
		sendTemplate(mailProperties.getFromMailMlb(), mailProperties.getFromNameMlb(), toMail, subject, template, variables);
	}

	/**
	 * 템플릿과 템플릿 변수를 조합하여 컨텐츠를 구성하여 메일로 전송합니다. DX 전용.
	 * @param toMail
	 * @param subject
	 * @param template
	 * @param variables
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	public void sendTemplateDx(String toMail, String subject, String template, Map<String, Object> variables) throws UnsupportedEncodingException, MessagingException {
		sendTemplate(mailProperties.getFromMailDx(), mailProperties.getFromNameDx(), toMail, subject, template, variables);
	}

	/**
	 * 템플릿과 템플릿 변수를 조합하여 컨텐츠를 구성하여 메일로 전송합니다.
	 * @param fromMail
	 * @param fromName
	 * @param toMail
	 * @param subject
	 * @param template
	 * @param variables
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	public void sendTemplate(String fromMail, String fromName, String toMail, String subject, String template, Map<String, Object> variables) throws UnsupportedEncodingException, MessagingException {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mailProperties.getHost());
        javaMailSender.setPort(mailProperties.getPort());
        javaMailSender.setUsername(mailProperties.getUserName());
        javaMailSender.setPassword(mailProperties.getPassword());

        InternetAddress from = new InternetAddress(fromMail, fromName);
        InternetAddress to = new InternetAddress(toMail);
        
        Context context = new Context();
        context.setVariables(variables);
        String contents = templateEngine.process(template, context);
        
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
        messageHelper.setFrom(from);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(contents, true);

        javaMailSender.send(mimeMessage);
	}	
	
}
