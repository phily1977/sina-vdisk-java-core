package vdiskcore;

import org.apache.commons.lang.StringEscapeUtils;

public class vdiskResponse {
	public class Token
	{
		private String err_code;
		private String err_message;
		private itemData data;
		
		public Token()
		{
			
		}
		
		public Token(String err_code,String err_message,itemData data)
		{
			this.err_code= err_code;
			this.err_message = err_message;
			this.data = data;
		}
		
		public String getErrCode()
		{
			return err_code;
		}
		
		public void setErrCode(String err_code)
		{
			this.err_code = err_code;
		}
		
		public String getErrMessage()
		{
			return err_message;
		}
		
		public void setErrMessage(String err_message){
			
			this.err_message= err_message;
		}
		
		public itemData getData()
		{
			return data;
		}
		
		public void setData(itemData data)
		{
			this.data = data;
		}
	}

	public class itemData
	{
		private String token;
		private String uid;
		private long time;
		private String is_active;
		private String appkey;
		
		public itemData(){
		
		}
		
		public itemData(String token,String uid,long time,String is_active,String appkey)
		{
			this.token = token;
			this.uid = uid;
			this.time = time;
			this.is_active = is_active;
			this.appkey = appkey;
		}
		
		public String getToken()
		{
			return this.token;
		}
		
		public void setToken(String token)
		{
			this.token = token;
		}
		
		public String getUid()
		{
			return this.uid;
		}
		
		public void setUid(String uid)
		{
			this.uid = uid;
		}
		
		public long getTime()
		{
			return this.time;
		}
		
		public void setTime(long time)
		{
			this.time = time;
		}
		
		public String getIsActive()
		{
			return this.is_active;
		}
		
		public void setIsActive(String is_active)
		{
			this.is_active= is_active;
		}
		
		public String getAppkey()
		{
			return this.appkey;
		}
		
		public void setAppkey(String appkey)
		{
			this.appkey = appkey;
		}
	}
	
	public class kToken{
		private String err_code;
		private String err_message;
		private itemData data;
		private String dologid;
		private String [] dologdir;
		
		public kToken(){}
		
		public kToken(String err_code,String err_message,itemData data,String dologid,String [] dologdir)
		{
			this.err_code= err_code;
			this.err_message = err_message;
			this.data = data;
			this.dologid = dologid;
			this.dologdir = dologdir;
		}
		
		public String getErrCode()
		{
			return err_code;
		}
		
		public void setErrCode(String err_code)
		{
			this.err_code = err_code;
		}
		
		public String getErrMessage()
		{
			return err_message;
		}
		
		public void setErrMessage(String err_message){
			
			this.err_message= err_message;
		}
		
		public String getDologid()
		{
			return this.dologid;
		}
		
		public void setDologid(String dologid)
		{
			this.dologid = dologid;
		}
		
		public String [] getDologdir()
		{
			return this.dologdir;
		}
		
		public void setDologdir(String [] dologdir)
		{
			this.dologdir = dologdir;
		}
		
		public itemData getData()
		{
			return data;
		}
		
		public void setData(itemData data)
		{
			this.data = data;
		}
	}
	
	
	public class DirId
	{
		private String err_code;
		private String err_message;
		private diridData data;
		private String dologid;
		private String [] dologdir;
		
		public DirId()
		{
			
		}
		
		public DirId(String err_code,String err_message,diridData data,String dologid,String [] dologdir)
		{
			this.err_code= err_code;
			this.err_message = err_message;
			this.data = data;
			this.dologid = dologid;
			this.dologdir = dologdir;
		}
		
		public String getErrCode()
		{
			return err_code;
		}
		
		public void setErrCode(String err_code)
		{
			this.err_code = err_code;
		}
		
		public String getErrMessage()
		{
			return err_message;
		}
		
		public void setErrMessage(String err_message){
			
			this.err_message= err_message;
		}
		
		public String getDologid()
		{
			return this.dologid;
		}
		
		public void setDologid(String dologid)
		{
			this.dologid = dologid;
		}
		
		public String [] getDologdir()
		{
			return this.dologdir;
		}
		
		public void setDologdir(String [] dologdir)
		{
			this.dologdir = dologdir;
		}
		
		public diridData getData()
		{
			return data;
		}
		
		public void setData(diridData data)
		{
			this.data = data;
		}
	}
	
	public class diridData
	{
		private long id;
		
		public long getId()
		{
			return id;
		}
	}

/*
 *  list files
 */
	public class listFiles
	{
		private String err_code;
		private String err_message;
		private listItems data;
		private pageInfo pageinfo;
		private String dologid;
		private String [] dologdir;
		
		public listFiles()
		{
			
		}
		
		public listFiles(String err_code,String err_message,listItems data,pageInfo pageinfo,String dologid,String [] dologdir)
		{
			this.err_code= err_code;
			this.err_message = err_message;
			this.data = data;
			this.pageinfo = pageinfo;
			this.dologid = dologid;
			this.dologdir = dologdir;
		}
		
		public String getErrCode()
		{
			return err_code;
		}
		
		public void setErrCode(String err_code)
		{
			this.err_code = err_code;
		}
		
		public String getErrMessage()
		{
			return err_message;
		}
		
		public void setErrMessage(String err_message){
			
			this.err_message= err_message;
		}
		
		public String getDologid()
		{
			return this.dologid;
		}
		
		public void setDologid(String dologid)
		{
			this.dologid = dologid;
		}
		
		public String [] getDologdir()
		{
			return this.dologdir;
		}
		
		public void setDologdir(String [] dologdir)
		{
			this.dologdir = dologdir;
		}
		
		public listItems  getData()
		{
			return data;
		}
		
		public void setData(listItems data)
		{
			this.data = data;
		}
		
		
		public pageInfo getPageInfo()
		{
			return pageinfo;
		}
		
		public void setPageInfo(pageInfo pageinfo)
		{
			this.pageinfo = pageinfo;
		}
		
	}
	
	public class listItems
	{
		private listItem [] list;
		
		public listItems(){}
		
		public listItems(listItem [] list)
		{
			this.list = list;
		}
		
		public listItem [] getList()
		{
			return list;
		}
		
		public void setList(listItem [] list)
		{
			this.list = list;
		}
	}
	
	public class listItem{
		private String id;
		private String name;
		private String dir_id;
		private String ctime;
		private String ltime;
		private String size;
		private String type;
		private String md5;
		private String sha1;
		private String length;
		private String url;
		private String s3_url;
		
		public listItem()
		{
			
		}
		
		public listItem(String id,String name,String dir_id,String ctime,String ltime,String size,String type,String md5,String sha1,String length,String url)
		{
			this.id= id;
			this.name =name;
			this.dir_id = dir_id;
			this.ctime = ctime;
			this.ltime = ltime;
			this.size = size;
			this.type = type;
			this.md5 = md5;
			this.sha1 = sha1;
			this.length = length;
			this.url = url;
		}
		
		public String getId()
		{
			return id;
		}
		
		public void setId(String id)
		{
			this.id = id;
		}
		
		public String getName()
		{
			return StringEscapeUtils.unescapeXml(name);
		}
		
		public void setName(String name)
		{
			this.name = name;
		}
		
		public String getDir_id()
		{
			return dir_id;
		}
		
		public void setDir_id(String dir_id)
		{
			this.dir_id = dir_id;
		}
		
		public String getCtime()
		{
			return ctime;
		}
		
		public void setCtime(String ctime)
		{
			this.ctime = ctime;
		}
		
		public String getLtime()
		{
			return ltime;
		}
		
		public void setLtime(String ctime)
		{
			this.ctime = ctime;
		}
		
		public String getSize()
		{
			return size;
		}
		
		public void setSize(String size)
		{
			this.size = size;
		}
		
		public String getType()
		{
			return StringEscapeUtils.unescapeXml(type);
		}
		
		public void setType(String type)
		{
			this.type = type;
		}
		
		public String getMd5()
		{
			return md5;
		}
		
		public void setMd5(String md5)
		{
			this.md5 = md5;
		}
		
		public String getSha1()
		{
			return sha1;
		}
		
		public void setSha1(String sha1)
		{
			this.sha1 = sha1;
		}
		
		public String getLength()
		{
			return length;
		}
		
		public void setLength(String length)
		{
			this.length = length;
		}
		
		public String getUrl()
		{
			return url;
		}
		
		public void setUrl(String url)
		{
			this.url = url;
		}
		
		public String getS3_Url()
		{
			return s3_url;
		}
		
		public void setS3_Url(String s3_url)
		{
			this.s3_url = s3_url;
		}
	}
	
	public class fileInfo{
		private String err_code;
		private String err_message;
		private listItem data;
		private String dologid;
		private String [] dologdir;
		
		public fileInfo(){}
		
		public fileInfo(String err_code,String err_message,listItem data,String dologid,String [] dologdir)
		{
			this.err_code= err_code;
			this.err_message = err_message;
			this.data = data;
			this.dologid = dologid;
			this.dologdir = dologdir;
		}
		
		public String getErrCode()
		{
			return err_code;
		}
		
		public void setErrCode(String err_code)
		{
			this.err_code = err_code;
		}
		
		public String getErrMessage()
		{
			return err_message;
		}
		
		public void setErrMessage(String err_message){
			
			this.err_message= err_message;
		}
		
		public String getDologid()
		{
			return this.dologid;
		}
		
		public void setDologid(String dologid)
		{
			this.dologid = dologid;
		}
		
		public String [] getDologdir()
		{
			return this.dologdir;
		}
		
		public void setDologdir(String [] dologdir)
		{
			this.dologdir = dologdir;
		}
		
		public listItem getData()
		{
			return data;
		}
		
		public void setData(listItem data)
		{
			this.data = data;
		}
	}
	
	public class pageInfo{
		private int page;
		private int pageSize;
		private int rstotal;
		private int pageTotal;
		
		public pageInfo(){}
		
		public pageInfo(int page,int pageSize,int rstotal,int pageTotal)
		{
			this.page = page;
			this.pageSize = pageSize;
			this.rstotal = rstotal;
			this.pageTotal = pageTotal;
		}
		
		public int getPage()
		{
			return page;
		}
		
		public void setPage(int page)
		{
			this.page = page;
		}
		
		public int getPageSize()
		{
			return pageSize;
		}
		
		public void setPageSize(int pageSize)
		{
			this.pageSize = pageSize;
		}
		
		public int getRstotal()
		{
			return rstotal;
		}
		
		public void setRstotal(int rstotal)
		{
			this.rstotal = rstotal;
		}
		
		public int getPageTotal()
		{
			return pageTotal;
		}
		
		public void setPageTotal(int pageTotal)
		{
			this.pageTotal = pageTotal;
		}
	
	}

}
