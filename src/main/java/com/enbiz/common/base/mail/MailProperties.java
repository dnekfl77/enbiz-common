package com.enbiz.common.base.mail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailProperties {
	private String host;
	private int port;
	private String userName;
	private String password;
	private String fromMailMlb;
	private String fromNameMlb;
	private String fromMailDx;
	private String fromNameDx;
}
