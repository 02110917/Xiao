package com.flying.xiao.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.flying.xiao.app.AppContext;
import com.flying.xiao.app.AppException;
import com.flying.xiao.common.URLs;
import com.flying.xiao.entity.Base;
import com.flying.xiao.entity.XComment;
import com.flying.xiao.entity.XContent;
import com.flying.xiao.entity.XGoodType;
import com.flying.xiao.entity.XPraise;
import com.flying.xiao.entity.XUserInfo;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

@SuppressWarnings("deprecation")
public class HttpUtil
{
	public static final String UTF_8 = "UTF-8";
	private final static int TIMEOUT_CONNECTION = 20000;
	private final static int TIMEOUT_SOCKET = 20000;
	private final static int RETRY_TIME = 3;

	private static String appCookie;

	public static void cleanCookie()
	{
		appCookie = "";
	}

	private static String getCookie(AppContext appContext)
	{
		if (appCookie == null || appCookie == "")
		{
			appCookie = appContext.readCookie();
		}
		return appCookie;
	}

	private static HttpClient getHttpClient()
	{
		HttpParams httpParams = new BasicHttpParams();
		// 设置 连接超时时间
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_CONNECTION);
		// 设置 读数据超时时间
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_SOCKET);
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		return httpClient;
	}

	private static HttpGet getHttpGet(String url, String cookie)
	{
		HttpGet httpGet = new HttpGet(url);
		if (cookie != null && (!cookie.equals("")))
			httpGet.setHeader("Cookie", cookie);
		return httpGet;
	}

	private static HttpPost getHttpPost(String url, String cookie)
	{
		HttpPost httpPost = new HttpPost(url);
		if (cookie != null && (!cookie.equals("")))
			httpPost.setHeader("Cookie", cookie);
		return httpPost;
	}

	private static String _MakeURL(String p_url, Map<String, Object> params)
	{
		StringBuilder url = new StringBuilder(p_url);
		if (url.indexOf("?") < 0)
			url.append('?');

		for (String name : params.keySet())
		{
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
			// 不做URLEncoder处理
			// url.append(URLEncoder.encode(String.valueOf(params.get(name)),
			// UTF_8));
		}
		return url.toString().replace("?&", "?");
	}

	/**
	 * get请求URL
	 * 
	 * @param url
	 * @throws AppException
	 */
	private synchronized static String http_get(AppContext appContext, String url) throws AppException
	{
		// System.out.println("get_url==> "+url);
		String cookie = getCookie(appContext);
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		int time = 0;
		do
		{
			try
			{
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, cookie);
				HttpResponse response = httpClient.execute(httpGet);
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK)
				{
					throw AppException.http(statusCode);
				}
				return EntityUtils.toString(response.getEntity(), UTF_8);
			} catch (IOException e)
			{
				time++;
				if (time < RETRY_TIME)
				{
					try
					{
						Thread.sleep(1000);
					} catch (InterruptedException e1)
					{
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally
			{
				// 释放连接
				httpGet.abort();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		return null;

	}

	private static byte[] http_get(String url) throws AppException
	{
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		int time = 0;
		do
		{
			try
			{
				httpClient = getHttpClient();
				httpGet = new HttpGet(url);
				HttpResponse response = httpClient.execute(httpGet);
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK)
				{
					throw AppException.http(statusCode);
				}
				return EntityUtils.toByteArray(response.getEntity());
			} catch (IOException e)
			{
				time++;
				if (time < RETRY_TIME)
				{
					try
					{
						Thread.sleep(1000);
					} catch (InterruptedException e1)
					{
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally
			{
				// 释放连接
				httpGet.abort();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		return null;
	}

	private synchronized static String http_post(AppContext appContext, String url, Map<String, String> params)
			throws AppException
	{
		String cookie = getCookie(appContext);
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		// post表单参数处理
		if (params != null && params.size() > 0)
		{
			for (String key : params.keySet())
			{
				NameValuePair pair = new BasicNameValuePair(key, params.get(key));
				list.add(pair);
			}
		}
		// 生成 HTTP 实体
		try
		{
			HttpEntity httpEntity = new UrlEncodedFormEntity(list, UTF_8);
			int time = 0;
			do
			{
				try
				{
					httpClient = getHttpClient();
					httpPost = getHttpPost(url, cookie);
					httpPost.setEntity(httpEntity);
					HttpResponse response = httpClient.execute(httpPost);
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode != HttpStatus.SC_OK)
					{
						throw AppException.http(statusCode);
					} else if (statusCode == HttpStatus.SC_OK)
					{
						List<Cookie> cookies = ((DefaultHttpClient) httpClient).getCookieStore().getCookies();
						String tmpcookies = "";
						for (Cookie ck : cookies)
						{
							tmpcookies += ck.getName() + "=" + ck.getValue();
							System.out.println("coolie----" + ck.toString());
						}
						// 保存cookie
						if (appContext != null && tmpcookies != "")
						{
							appContext.writeCookie(tmpcookies);
							appCookie = tmpcookies;
						}
					}
					return EntityUtils.toString(response.getEntity(), UTF_8);
				} catch (IOException e)
				{
					time++;
					if (time < RETRY_TIME)
					{
						try
						{
							Thread.sleep(1000);
						} catch (InterruptedException e1)
						{
						}
						continue;
					}
					// 发生网络异常
					e.printStackTrace();
					throw AppException.network(e);
				} finally
				{
					// 释放连接
					httpPost.abort();
					httpClient = null;
				}
			} while (time < RETRY_TIME);
		} catch (UnsupportedEncodingException e2)
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		return null;
	}

	/**
	 * 公用post方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	private static HttpEntity http_post(AppContext appContext, String url, Map<String, String> params,
			Map<String, File> files) throws AppException
	{
		// System.out.println("post_url==> "+url);
		String cookie = getCookie(appContext);
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		// post表单参数处理
		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
		multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		multipartEntityBuilder.setCharset(Charset.forName(UTF_8));
		if (params != null && params.size() > 0)
		{
			for (String key : params.keySet())
			{
				multipartEntityBuilder.addTextBody(key, params.get(key),
						ContentType.create("text/plain", Charset.forName(UTF_8)));
			}
		}

		// 发送的文件
		if (files != null && files.size() > 0)
		{
			for (String key : files.keySet())
			{
				multipartEntityBuilder.addBinaryBody(key, files.get(key));
			}
		}
		// 生成 HTTP 实体
		HttpEntity httpEntity = multipartEntityBuilder.build();
		int time = 0;
		do
		{
			try
			{
				httpClient = getHttpClient();
				httpPost = getHttpPost(url, cookie);
				httpPost.setEntity(httpEntity);
				HttpResponse response = httpClient.execute(httpPost);
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK)
				{
					throw AppException.http(statusCode);
				} else if (statusCode == HttpStatus.SC_OK)
				{
					List<Cookie> cookies = ((DefaultHttpClient) httpClient).getCookieStore().getCookies();
					String tmpcookies = "";
					for (Cookie ck : cookies)
					{
						tmpcookies += ck.toString() + ";";
						System.out.println("coolie----" + ck.toString());
					}
					// 保存cookie
					if (appContext != null && tmpcookies != "")
					{
						appContext.writeCookie(tmpcookies);
						appCookie = tmpcookies;
					}
				}
				return response.getEntity();
			} catch (IOException e)
			{
				time++;
				if (time < RETRY_TIME)
				{
					try
					{
						Thread.sleep(1000);
					} catch (InterruptedException e1)
					{
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally
			{
				// 释放连接
				httpPost.abort();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		return null;
	}

	/**
	 * 获取网络图片
	 * 
	 * @param appContext
	 * @param url
	 * @return
	 * @throws AppException
	 */
	public static Bitmap getNetBitmap(AppContext appContext, String url) throws AppException
	{
		// System.out.println("image_url==> "+url);
		Bitmap bitmap = null;
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		int time = 0;
		do
		{
			try
			{
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, null);
				HttpResponse response = httpClient.execute(httpGet);
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK)
				{
					throw AppException.http(statusCode);
				}
				InputStream in = response.getEntity().getContent();
				bitmap = BitmapFactory.decodeStream(in);
			} catch (IOException e)
			{
				time++;
				if (time < RETRY_TIME)
				{
					try
					{
						Thread.sleep(1000);
					} catch (InterruptedException e1)
					{
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally
			{
				// 释放连接
				httpGet.abort();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		return bitmap;
	}

	/**
	 * 登录， 自动处理cookie
	 * 
	 * @param appContext
	 * @param username
	 * @param pwd
	 * @return
	 * @throws AppException
	 */
	public static XUserInfo login(AppContext appContext, String username, String pwd) throws AppException
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("password", pwd);
		params.put("type", "login");
		String loginurl = URLs.URL_LOGIN;
		try
		{
			String result = http_post(appContext, loginurl, params);
			System.out.println("json---" + result);
			XUserInfo xUser = new XUserInfo();
			xUser = (XUserInfo) xUser.jsonToBase(result);
			return xUser;
		} catch (Exception e)
		{
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}

	public static List<XContent> getContentData(AppContext appContext, int type, int page)
	{
		List<XContent> xConList = null;
		String url = URLs.URL_MAIN_GET_CONTENT+"?type="+type+"&page=" + page;;
		
		try
		{
			String result = http_get(appContext, url);
			Gson gson = new Gson();
			try
			{
			xConList = gson.fromJson(result, new TypeToken<List<XContent>>()
					
			{
			}.getType());
			}catch(JsonSyntaxException e){
				e.printStackTrace();
			}
		} catch (AppException e)
		{
			e.printStackTrace();
		}
		return xConList;
	}

	public static String getContentDetail(AppContext appContext, String url)
	{
		String result = "";
		try
		{
			result = http_get(appContext, url);
			System.out.println("json---" + result);
		} catch (AppException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 从网络获取图片字节数组
	 * 
	 * @param url
	 * @return
	 */
	public static byte[] loadByteArrayFromNetwork(AppContext appContext, String url)
	{

		try
		{
			return http_get(url);
		} catch (Exception e)
		{
			return null;
		}

	}

	/**
	 * 发表评论
	 * 
	 * @param appContext
	 * @param userId
	 * @param contentId
	 * @param commentInfo
	 * @return
	 * @throws AppException
	 */
	public static XComment pubConmment(AppContext appContext, long userId, long contentId,
			String commentInfo, long replyId) throws AppException
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("contentid", contentId + "");
		params.put("userId", userId + "");
		params.put("commentInfo", commentInfo);
		if (replyId != 0)
		{
			params.put("replyId", replyId + "");
		}
		String url = URLs.URL_PUB_COMMENT;
		try
		{
			String result = http_post(appContext, url, params);
			System.out.println("json---" + result);
			XComment base = new XComment();
			base = (XComment) base.jsonToBase(result);
			return base;
		} catch (Exception e)
		{
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}

	/**
	 * 赞
	 * 
	 * @param userId
	 * @param contentId
	 * @param isCancel
	 *            是否取消赞
	 * @throws AppException
	 */
	public static XPraise praiseContent(AppContext appContext, long userId, long contentId, boolean isCancel)
			throws AppException
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("contentid", contentId + "");
		params.put("userId", userId + "");
		params.put("isCancel", isCancel + "");
		String url = URLs.URL_PRAISE_OPERATE;
		try
		{
			String result = http_post(appContext, url, params);
			System.out.println("json---" + result);
			XPraise base = new XPraise();
			base = (XPraise) base.jsonToBase(result);
			return base;
		} catch (Exception e)
		{
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	/**
	 * 收藏操作
	 * @param appContext
	 * @param contentId 内容id
	 * @param isCancel 是否是取消收藏
	 * @author zhangmin
	 * @return
	 * @throws AppException 
	 */
	public static Base collectionOperate(AppContext appContext,long contentId, boolean isCancel) throws AppException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("contentid", contentId + "");
		params.put("isCancel", isCancel + "");
		String url = URLs.URL_CollectionOperate;
		try
		{
			String result = http_post(appContext, url, params);
			System.out.println("json---" + result);
			Base base = new Base();
			base = (Base) base.jsonToBase(result);
			return base;
		} catch (Exception e)
		{
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}

	/**'
	 * 获取二手物品类型列表
	 * @param appContext
	 * @return
	 * @author zhangmin
	 * @throws AppException
	 */
	public static List<XGoodType> getMarketType(AppContext appContext) throws AppException
	{
		String result;
		try
		{
			result = http_get(appContext, URLs.URL_GET_MARKET_TYPE);
			System.out.println("json---" + result);
			Gson gson = new Gson();
			List<XGoodType> xtypesList = gson.fromJson(result, new TypeToken<List<XGoodType>>()
			{
			}.getType());
			return xtypesList;
		} catch (AppException e)
		{
			e.printStackTrace();
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	/**
	 * 获取评论列表
	 * @param appContext
	 * @param contentid
	 * @param page
	 * @return
	 * @throws AppException
	 */
	public static List<XComment> getComments(AppContext appContext,long contentid,int page) throws AppException{
		List<XComment> commentList=null;
		String result="";
		String url=URLs.URL_GETCOMMENTS+"?contentid="+contentid+"&page="+page;
		try
		{
			result=http_get(appContext, url);
			System.out.println("json---" + result);
			Gson gson = new Gson();
			try
			{
				commentList = gson.fromJson(result, new TypeToken<List<XComment>>()
			{
			}.getType());
			}
			catch(JsonSyntaxException e){
				e.printStackTrace();
			}
		} catch (AppException e)
		{
			e.printStackTrace();
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
		return commentList ;
	}
	
	/**
	 * 
	 * @param appContext
	 * @param userType 用户类型 1:个人  2:部门 	3:商家
	 * @param page 页数
	 * @return
	 * @throws AppException 
	 */
	public static List<XUserInfo> getUserInfos(AppContext appContext,int userType,int page) throws AppException{
		List<XUserInfo> userList=null;
		String result="";
		String url=URLs.URL_GETUSERINFOS+"?type="+userType+"&page="+page;
		try
		{
			result=http_get(appContext, url);
			System.out.println("json---" + result);
			Gson gson = new Gson();
			try
			{
				userList = gson.fromJson(result, new TypeToken<List<XUserInfo>>()
			{
			}.getType());
			}
			catch(JsonSyntaxException e){
				e.printStackTrace();
			}
		} catch (AppException e)
		{
			e.printStackTrace();
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
		return userList;
	}
	public static Base addFriend(AppContext appContext,long userId) throws AppException{
		Base base=null;
		String url=URLs.URL_ADD_FRIEND+"?userId="+userId;
		String json=http_get(appContext,url);
		try
		{
			base=new Base() ;
			base=base.jsonToBase(json);
		}catch(JsonSyntaxException e){
			e.printStackTrace();
		}
		return base ;
	}
}
