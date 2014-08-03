package com.flying.xiao.manager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import com.flying.xiao.constant.Constant;
import com.flying.xiao.entity.XContent;


public class ContentManager
{
	public static ContentManager contentManager ;
	private List<XContent> newsContentList;
	private List<XContent> lostContentList;
	private List<XContent> diaryContentList;
	private List<XContent> marketContentList;
	private List<XContent> askContentList;
	private List<List<XContent>> contentList ;
	private ContentManager(){
		newsContentList=new ArrayList<XContent>();
		lostContentList=new ArrayList<XContent>();
		diaryContentList=new ArrayList<XContent>();
		marketContentList=new ArrayList<XContent>();
		askContentList=new ArrayList<XContent>();
	}
	public static ContentManager getContentMangerShare(){
		if(contentManager==null)
			contentManager=new ContentManager();
		return contentManager;
	}
	
	public List<XContent> getContentListByType(int conType){
		switch (conType)
		{
		case Constant.ContentType.CONTENT_TYPE_NEWS:
			return getNewsContentList();
		case Constant.ContentType.CONTENT_TYPE_LOST:
			return getLostContentList();
		case Constant.ContentType.CONTENT_TYPE_DIARY:
			return contentManager.getDiaryContentList();
		case Constant.ContentType.CONTENT_TYPE_MARKET:
			return contentManager.getMarketContentList();
		case Constant.ContentType.CONTENT_TYPE_ASK:
			return contentManager.getAskContentList();
		default:
			return null;
		}
	}
	public List<XContent> getNewsContentList()
	{
		return newsContentList;
	}
	public void setNewsContentList(List<XContent> newsContentList)
	{
		this.newsContentList = newsContentList;
	}
	public List<XContent> getLostContentList()
	{
		return lostContentList;
	}
	public void setLostContentList(List<XContent> lostContentList)
	{
		this.lostContentList = lostContentList;
	}
	public List<XContent> getDiaryContentList()
	{
		return diaryContentList;
	}
	public void setDiaryContentList(List<XContent> diaryContentList)
	{
		this.diaryContentList = diaryContentList;
	}
	public List<XContent> getMarketContentList()
	{
		return marketContentList;
	}
	public void setMarketContentList(List<XContent> marketContentList)
	{
		this.marketContentList = marketContentList;
	}
	public List<XContent> getAskContentList()
	{
		return askContentList;
	}
	public void setAskContentList(List<XContent> askContentList)
	{
		this.askContentList = askContentList;
	}
	/**
	 * 将对象保存
	 */
	public void writeList(String path){
		contentList=new ArrayList<List<XContent>>();
		contentList.add(newsContentList);
		contentList.add(lostContentList);
		contentList.add(diaryContentList);
		contentList.add(marketContentList);
		contentList.add(askContentList);
		ObjectOutputStream objectOut = null;
		try
		{
			objectOut = new ObjectOutputStream(new FileOutputStream(path));
			objectOut.writeObject(contentList);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			if (objectOut != null)
			{
				try
				{
					objectOut.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 读取保存的对象
	 */
	public void readList(String path){
		ObjectInputStream objIn = null;
		try
		{
			objIn = new ObjectInputStream(new FileInputStream(path));

			contentList = (List<List<XContent>>) objIn.readObject();

		} catch (StreamCorruptedException e)
		{
			e.printStackTrace();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		if (objIn != null)
		{
			try
			{
				objIn.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		if(contentList!=null&&contentList.size()==5){
			newsContentList=contentList.get(0);
			lostContentList=contentList.get(1);
			diaryContentList=contentList.get(2);
			marketContentList=contentList.get(3);
			askContentList=contentList.get(4);
		}
	}
}
