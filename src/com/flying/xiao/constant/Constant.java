package com.flying.xiao.constant;

public class Constant
{
	public static final int MAX_PAGE_COUNT=5;
	public static class ErrorCode{
		public static final int USER_LOGIN_ERROR=0X01;
		public static final int PUB_COMMENT_ERROR=0X02;
		public static final int PARAM_ERROR=0X03;//参数错误
		public static final int USER_NOT_LOGIN=0X04;//用户未登陆 或则session失效
		public static final int PRAISE_OPERATE_ERROR=0X05;//赞出错
		public static final int GET_CONTENT_DETAIL_NO_CONTENT=0X06;
		public static final int GET_MARKET_DETAIL_NO_MARKET=0X07;
		public static final int SAVE_CONTENT_ERROR=0X08;
		public static final int UPDATE_CONTENT_ERROR=0X09;
		public static final int GET_COLLECTION_ERROR=0X0A; 
		public static final int USER_REGIEST_ERROR=0X0B;
		public static final int GET_USERINFOS_ERROR=0X0C;
		public static final int USER_NOT_FOUNT=0X0D;
		public static final int SAVE_ERROR=0X0E;
		public static final int ADD_FRIEND_IS_YOUR_FRIEND_ALERADY=0X0F;
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
	public static class UserType{
		public static final int User_TYPE_PESONAL=1;  //个人
		public static final int User_TYPE_DEPARTMENT=2; //部门
		public static final int User_TYPE_BUSINESS=3; // 商家
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
		public static final int COLLECTION_OPERATE_SUCCESS=14;
		public static final int COLLECTION_OPERATE_FAIL=15;
		public static final int GET_COMMENTS_FAIL=16;
		public static final int GET_COMMENT_SUCCESS=17;
		public static final int GET_USERINFOS_DEPARTMENT_SUCCESS=18;
		public static final int GET_USERINFOS_SUCCESS=188;
		public static final int GET_USERINFOS_FAIL=19;
		public static final int ADD_FRIEND_FAIL=20;
		public static final int ADD_FRIEND_SUCCESS=21;
		public static final int ADD_FRIEND_IS_YOUR_FRIEND_ALERADY=22;
		
	}
}
