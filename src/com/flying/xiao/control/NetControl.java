package com.flying.xiao.control;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.flying.xiao.app.AppContext;
import com.flying.xiao.app.AppException;
import com.flying.xiao.common.URLs;
import com.flying.xiao.constant.Constant;
import com.flying.xiao.entity.Base;
import com.flying.xiao.entity.XComment;
import com.flying.xiao.entity.XContent;
import com.flying.xiao.entity.XContentDetail;
import com.flying.xiao.entity.XGoodType;
import com.flying.xiao.entity.XPraise;
import com.flying.xiao.entity.XUserInfo;
import com.flying.xiao.http.HttpUtil;

public class NetControl
{
	private static NetControl control=null ;
	private static AppContext appContext=null;
	private NetControl(){}
	public static NetControl getShare(Context context){
		if(control==null)
			control=new NetControl() ;
		appContext=(AppContext) context.getApplicationContext() ;
		return control;
	}
	
	public void login(final String userName,final String password){
		new Thread(){
			@Override
			public void run() {
				Message msg =new Message();
				try {
					
	                XUserInfo user =HttpUtil.login(appContext, userName, password);
	                if(user==null||user.getErrorCode()!=0){
	                	msg.what=Constant.HandlerMessageCode.LOGIN_FAILD;
	                	msg.obj = user;
	                }
	                else{
	                	msg.what = Constant.HandlerMessageCode.LOGIN_SUCCESS;//成功
	                	msg.obj = user;
	                	appContext.writeUserInfo(user); 
	                }
	            } catch (AppException e) {
	            	e.printStackTrace();
			    	msg.what = Constant.HandlerMessageCode.NET_THROW_EXCEPTION;
			    	msg.obj = e;
	            }
				appContext.baseActivity.mHandler.sendMessage(msg);
				
			}
		}.start();
	}
	/**
	 * 获取主页 内容标题显示
	 * @param type
	 * @param page
	 * @param handler
	 */
	public   void getContentData(final int type,final int page,final Handler handler){
		new Thread(){
			@Override
			public void run() {
				Message msg =new Message();
				List<XContent> xConList =HttpUtil.getContentData(appContext, type, page);
				if(xConList==null||xConList.size()==0){
					msg.what=Constant.HandlerMessageCode.MAIN_LOAD_DATA_ERROR;
					msg.obj = "获取信息出错...";
				}
				else{
					msg.what = Constant.HandlerMessageCode.MAIN_LOAD_DATA_SUCCESS;//成功
					msg.obj = xConList;
					System.out.println("发送消息--"+type);
				}
				handler.sendMessage(msg);
				
			}
		}.start();
	}
	
	/**
	 * 获取内容详情
	 * @param contentType
	 * @param contentId
	 */
	public void getContentDetail(int contentType,long contentId){
		//http://192.168.0.8:8080/XiaoServer/servlet/GetContentDetail?type=1&id=17
		final String url=URLs.URL_GET_CONTENT_DETAIL+"?type="+contentType+"&id="+contentId;
		new Thread(){
			@Override
			public void run() {
				Message msg =new Message();
				XContentDetail conDetail =HttpUtil.getContentDetail(appContext,url);
				if(conDetail==null){
					msg.what=Constant.HandlerMessageCode.CONTENT_DETAIL_LOAD_DATA_ERROR;
					msg.obj = "获取信息出错...";
				}
				else{
					msg.what=Constant.HandlerMessageCode.CONTENT_DETAIL_LOAD_DATA_SUCCESS;
					msg.obj = conDetail;
				}
				appContext.baseActivity.mHandler.sendMessage(msg);
				
			}
		}.start();
	}
	/**
	 * 提交评论
	 * @param userId 发表评论user
	 * @param contentId 内容id 
	 * @param commentInfo  评论内容
	 * @param replyId  回复的评论id
	 * @param handler  ui接收消息
	 */
	public void pubComment(final long userId,final long contentId,final String commentInfo,final long replyId,final Handler handler){
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				Message msg =new Message();
				try
				{
					XComment base=HttpUtil.pubConmment(appContext, userId, contentId, commentInfo,replyId);
					if(base.getErrorCode()!=0)
					{
					if(base.getErrorCode()==Constant.ErrorCode.USER_NOT_LOGIN){
						msg.what=Constant.HandlerMessageCode.USER_NOT_LOGIN;
					}
					else{
						msg.what=Constant.HandlerMessageCode.PUB_COMMENT_ERROR;
					}
					msg.obj=base.getErrorMsg();
					}
					else
					{
						msg.what=Constant.HandlerMessageCode.PUB_COMMENT_SUCCESS;
						msg.obj=base;
					}
				} catch (AppException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what=Constant.HandlerMessageCode.PUB_COMMENT_ERROR;
					msg.obj="发表评论失败..";
				}
				handler.sendMessage(msg);
			}
		}).start();
	}
	/**
	 * 提交评论
	 * @param userId 发表评论user
	 * @param contentId 内容id 
	 * @param commentInfo  评论内容
	 * @param replyId  回复的评论id
	 */
	public void pubComment(final long userId,final long contentId,final String commentInfo,final long replyId){
		pubComment(userId, contentId, commentInfo, replyId, appContext.baseActivity.mHandler);
	}
	public void praiseOpreate(final XPraise xp,final boolean isCancel,final Handler handler){
		final long userId=xp.getUserInfo().getId();
		final long contentId=xp.getContentId();
		
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				Message msg =new Message();
				try
				{
					XPraise base=HttpUtil.praiseContent(appContext, userId, contentId, isCancel);
					if(base.getErrorCode()!=0)
					{
					if(base.getErrorCode()==Constant.ErrorCode.USER_NOT_LOGIN){
						msg.what=Constant.HandlerMessageCode.USER_NOT_LOGIN;
					}
					else{
						msg.what=Constant.HandlerMessageCode.PRAISE_OPERATE_ERROR;
					}
					msg.obj=xp;
					}
					else
					{
						msg.what=Constant.HandlerMessageCode.PRAISE_OPERATE_SUCCESS;
						msg.obj=base;
					}
				} catch (AppException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what=Constant.HandlerMessageCode.PRAISE_OPERATE_ERROR;
					msg.obj=xp;
				}
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	/**
	 * 获取market type
	 * @param type
	 * @param page
	 * @param handler
	 */
	public   void getMarketTypes(final Handler handler){
		new Thread(){
			@Override
			public void run() {
				Message msg =new Message();
				List<XGoodType> xtypeList;
				try
				{
					xtypeList = HttpUtil.getMarketType(appContext);
					if(xtypeList==null||xtypeList.size()==0){
						msg.what=Constant.HandlerMessageCode.GET_MARKET_TYPE_ERROR;
						msg.obj = "获取信息出错...";
					}
					else{
						msg.what = Constant.HandlerMessageCode.GET_MARKET_TYPE_SUCCESS;//成功
						msg.obj = xtypeList;
					}
				} catch (AppException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what=Constant.HandlerMessageCode.GET_MARKET_TYPE_ERROR;
					msg.obj=e;
				}
				handler.sendMessage(msg);
				
			}
		}.start();
	}
}
