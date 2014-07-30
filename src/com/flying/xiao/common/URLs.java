package com.flying.xiao.common;
import java.io.Serializable;

public class URLs implements Serializable {
	
	public final static String HOST = "http://la.xiaolife.net:8080";
	public final static String HTTP = "http://";
	public final static String HTTPS = "https://";
	
	private final static String URL_SPLITTER = "/";
	private final static String URL_UNDERLINE = "_";
	public final static String URL_MAIN_NEWS =HOST+ "/XiaoServer/servlet/GetContent?type=news";
	public final static String URL_MAIN_LOST =HOST+ "/XiaoServer/servlet/GetContent?type=lost";
	public final static String URL_MAIN_DIARY =HOST+ "/XiaoServer/servlet/GetContent?type=diary";
	public final static String URL_MAIN_MARKET =HOST+ "/XiaoServer/servlet/GetContent?type=market";
	public final static String URL_MAIN_ASK =HOST+ "/XiaoServer/servlet/GetContent?type=ask";
	public final static String URL_LOGIN =HOST+ "/XiaoServer/servlet/UserServlet";
	public final static String URL_GET_CONTENT_DETAIL =HOST+ "/XiaoServer/servlet/GetContentDetail";
	public final static String URL_PUB_COMMENT =HOST+ "/XiaoServer/servlet/PubComment";
	public final static String URL_PRAISE_OPERATE =HOST+ "/XiaoServer/servlet/PraiseOperate";//?contentid=23&userId=4&isCancel=false
	public final static String URL_GET_MARKET_TYPE =HOST+ "/XiaoServer/servlet/GetMarketType";
	
	}