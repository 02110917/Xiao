package com.flying.xiao.constant;

public class Constant
{
	public static class ErrorCode{
		public static final int USER_LOGIN_ERROR=0X01;
		public static final int PUB_COMMENT_ERROR=0X02;
		public static final int PARAM_ERROR=0X03;//参数错误
		public static final int USER_NOT_LOGIN=0X04;//用户未登陆 或则session失效
	}
	
	public static class ContentType{
		public static final int CONTENT_TYPE_NEWS=0x01; //资讯
		public static final int CONTENT_TYPE_LOST=0x02; //失物
		public static final int CONTENT_TYPE_DIARY=0x03; //新鲜事
		public static final int CONTENT_TYPE_MARKET=0x04; //市场
		public static final int CONTENT_TYPE_ASK=0x05; // 问答
	}
	
	public static class WenzhangType{
//		public static final int WENZHANG_TYPE_
	}
	public static class HandlerMessageCode{
		
		public static final int NET_THROW_EXCEPTION=-1;
		public static final int LOGIN_FAILD=0;
		public static final int LOGIN_SUCCESS=1;
		public static final int MAIN_LOAD_DATA_ERROR=3;
		public static final int MAIN_LOAD_DATA_SUCCESS=4;
		public static final int CONTENT_DETAIL_LOAD_DATA_SUCCESS=5;
		public static final int CONTENT_DETAIL_LOAD_DATA_ERROR=6;
		public static final int PUB_COMMENT_SUCCESS=7;
		public static final int PUB_COMMENT_ERROR=8;
		public static final int USER_NOT_LOGIN=9;
		public static final int PRAISE_OPERATE_ERROR=10;
		public static final int PRAISE_OPERATE_SUCCESS=11;
		public static final int GET_MARKET_TYPE_SUCCESS=12;
		public static final int GET_MARKET_TYPE_ERROR=13;
		
	}
}
