/*
 * Sina vdisk java core library
 * 
 * This is the first released version of the GPL
 * 
 * Phil Wang,10/2/2011
 * email:discovery_it@hotmail.com
 */
package vdiskcore;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

public class vdiskcore {
	
	private static String HOST_URL = "http://openapi.vdisk.me";
	
	private static String GET_TOKEN_URL = HOST_URL +"/?m=auth&a=get_token";
	private static String KEEP_TOKEN_URL = HOST_URL +"/?m=user&a=keep_token";
	private static String GET_LIST_URL = HOST_URL +"/?m=dir&a=getlist";
	private static String GET_DIRID_URL = HOST_URL +"/?m=dir&a=get_dirid_with_path";
	private static String GET_FILEINFO_URL = HOST_URL +"/?m=file&a=get_file_info";
	private static String UPLOAD_FILE_URL = HOST_URL + "/?m=file&a=upload_file";
	
	
	private static int URL_READTIMEOUT = 10000; //Read timeout is used in milliseconds
	
	
	public static String token="";
	 
	public static keepTokenThread ktt=null;
	
	//the events listeners Array
	private List<vdiskListener> _listeners = new ArrayList<vdiskListener>();
	
	private double _progress;
	private long _speed;
	
	//the error code and message  
	public int err_code = 0;
	public String err_message = "";
	
	public boolean initailize(String account,String password,String appkey,String  appsecret)
	{
		vdiskResponse.Token t = getToken(account,password,appkey,appsecret,System.currentTimeMillis());
		
		if (t!=null){
			vdiskcore.token = t.getData().getToken();
			
			//start the keeptoken thread;
            ktt = new keepTokenThread(vdiskcore.token);
            ktt.start();
			
			return true;
		}
		else
		{
			return false;
		}
			
	}
	
	/*
	* Get Token
	* if err return null, the err_code and err_message will be set.
	*/
   private vdiskResponse.Token getToken(String account,String password,String appkey,String  appsecret,long time_c) 
   {
	   //time_c = System.currentTimeMillis();
	   
	   URL gt_url;
		try {
			gt_url = new URL(GET_TOKEN_URL);
	
	       HttpURLConnection yc = (HttpURLConnection) gt_url.openConnection();
	       
           yc.setDoOutput(true);  
           yc.setDoInput(true);  
	       
           //POST params
	       params ps = new params();
	       ps.add("account",account);
	       ps.add("password", password);
	       ps.add("appkey", appkey);
	       ps.add("time",Long.toString(time_c));

	       
	       String strSignature = vdiskcore.Encrypt("account="+account +"&appkey="+appkey+"&password="+password+"&time="+Long.toString(time_c)+"", "hmacSHA256",appsecret);
	       
	       ps.add("signature", strSignature);
	       
	       System.out.println(ps.getParseString());
	       
	       yc.setRequestMethod("POST");
	       //
	       
	       yc.connect();
	       
           PrintStream send = new PrintStream(yc.getOutputStream());  
           send.print(ps.getParseString());  
           send.close();  

	       //
	       
	       
	       
	       BufferedReader in = new BufferedReader(
	                               new InputStreamReader(
	                               yc.getInputStream()));
	       String inputLine;
	
	       String buffer="";
	       
	       while ((inputLine = in.readLine()) != null) 
	       {
	           System.out.println(inputLine);
	           buffer +=inputLine;
	       }
	       in.close();
	       
	       Gson gs = new Gson();
	       
	       vdiskResponse.Token gt = gs.fromJson(buffer, vdiskResponse.Token.class);
	       
	       if (gt!=null)
	    	   vdiskcore.token = gt.getData().getToken();
	       
		   return gt;		
		   
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			this.err_code = 10001;
			this.err_message = e.getMessage();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.err_code = 10002;
			this.err_message = e.getMessage();
		}
		return null;
   }
	
   
   /*
    * close the keep token thread.
    */
   @SuppressWarnings("deprecation")
public void close()
   {
	   ktt.stop();
   }
   
   
   /*
    * Get file and directory objects in a directory
    */
   public vdiskResponse.listItems getList(String path)
   {
	   System.out.println("GETDIRID:" + vdiskcore.token + "," + path);
	   
	   vdiskResponse.DirId did = getDirId(vdiskcore.token,path);
	   
	   if (did!=null)
		   return getList(vdiskcore.token,Long.toString(getDirId(vdiskcore.token,path).getData().getId())).getData();
	   else
		   return null;
   }
   
   /*
    * retrieve the files in the directory not include its subdirectory
    */
   private vdiskResponse.listFiles getList(String token,String dir_id)
   {
	   
   
	   URL url;
		try {
			url = new URL(GET_LIST_URL);
	
	       HttpURLConnection yc = (HttpURLConnection) url.openConnection();
	       
           yc.setDoOutput(true);  
           yc.setDoInput(true);  
	       
           //POST params
	       params ps = new params();
	       ps.add("token",token);
	       ps.add("dir_id", dir_id);

	       yc.setRequestMethod("POST");
	       //
	       
	       yc.connect();
	       
           PrintStream send = new PrintStream(yc.getOutputStream());  
           send.print(ps.getParseString());  
           send.close();  

	       //
	       
	       
	       
	       BufferedReader in = new BufferedReader(
	                               new InputStreamReader(
	                               yc.getInputStream()));
	       String inputLine;
	
	       String buffer="";
	       
	       while ((inputLine = in.readLine()) != null) 
	       {
	           System.out.println(inputLine);
	           buffer +=inputLine;
	       }
	       in.close();
	       
	       Gson gs = new Gson();
	       
	       vdiskResponse.listFiles lstFiles = gs.fromJson(buffer, vdiskResponse.listFiles.class);
	       
       
		   return lstFiles;			
		   
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
   }
   
   /*
    * get file information by filename and path,return listItem object
    */
   public vdiskResponse.listItem getFileInfoByName(String path,String fileName)
   {
	   vdiskResponse.listItems lstItems = getList(path);
	   
	   if (lstItems!=null){
		   String fid="";
		   
		   for(int i=0;i<lstItems.getList().length;i++)
		   if (lstItems.getList()[i].getName().equals(fileName))
		   {
			   fid = lstItems.getList()[i].getId();
			   break;
		   }
		   
		   if (fid!="")
			   return  getFileInfo(vdiskcore.token,fid).getData();
	   }
	   
	   return null;
   }
   
   public void downloadFile(String path,String fileName,String destPath)
   {
	   String downloadUrl;
	   try{
	   //get file download url
		   downloadUrl = getFileInfoByName(path,fileName).getS3_Url();
	   }catch(Exception e)
	   {
		   e.printStackTrace();
		   return;
	   }
	   
	   
	   //download the file to destpath
	   URL url;
	try {
		    url = new URL(downloadUrl);
		    URLConnection uc = url.openConnection();
		    String contentType = uc.getContentType();
		    int contentLength = uc.getContentLength();
		    if (contentType.startsWith("text/") || contentLength == -1) {
		      throw new IOException("This is not a binary file.");
		    }
		    InputStream raw = uc.getInputStream();
		    InputStream in = new BufferedInputStream(raw);
		    byte[] data = new byte[contentLength];
		    int bytesRead = 0;
		    int offset = 0;
		    		    
		    while (offset < contentLength) {
		      bytesRead = in.read(data, offset, data.length - offset);
		      if (bytesRead == -1)
		        break;
		      offset += bytesRead;
		    }
		    in.close();
	
		    if (offset != contentLength) {
		      throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
		    }
	
		    //String filename = fileName;
		    FileOutputStream out = new FileOutputStream(destPath+fileName);
		    out.write(data);
		    out.flush();
		    out.close();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	   
	   
   }
   
   /*
    * download the bigger file
    */
   public synchronized void downloadFileEx(String path,String fileName,String destPath)
   {
	   
	   System.out.println("downloadFileEx START:" + vdiskcore.token + "," + path);
	   String downloadUrl;
	   
	   //get file download url from S3_Url attribute of FileInfo
	   vdiskResponse.listItem fileinfo = getFileInfoByName(path,fileName);
	   
	   if (fileinfo!=null){
		   downloadUrl = fileinfo.getS3_Url();
	   }else
	   {
		   return;
	   }
	   
		   
		   
	   
	   
	   //download the file to destpath
	   URL url;
	   try {
		   long b_t = System.currentTimeMillis();
		   
		    url = new URL(downloadUrl);
		    URLConnection uc = url.openConnection();
		    
		    uc.setReadTimeout(URL_READTIMEOUT);
		    
		    String contentType = uc.getContentType();
		    int contentLength = uc.getContentLength();
		    if (contentType.startsWith("text/") || contentLength == -1) {
		      throw new IOException("This is not a binary file.");
		    }
		    
		    InputStream raw = uc.getInputStream();
		    InputStream in = new BufferedInputStream(raw);
		    //byte[] data = new byte[contentLength];
		    int bytesRead = 0;
		    int offset = 0;
		    		
		    FileOutputStream out = new FileOutputStream(destPath+fileName);
		    
		    
		    
		    while (offset < contentLength) {
		    	byte [] tmp = new byte[8192];
		    	bytesRead = in.read(tmp);
		    	
		    	if (bytesRead<8192)
		    		tmp = Arrays.copyOf(tmp, bytesRead);
		        
		    	//write the file
			    out.write(tmp);
			    
		      
		      if (bytesRead == -1)
		        break;
		      offset += bytesRead;
		      
		      //calculate the download progress, speed and raise the progress event
		      this._progress = (double)offset / contentLength;
		      this._speed =(long)((double)offset / (System.currentTimeMillis()+1-b_t) * 1000);
		      _fireProgressEvent();
		      
		      
		    }
		    in.close();
	
		    if (offset != contentLength) {
		    	System.out.println("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
		      throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
		    }
	
		    //String filename = fileName;
		    
		    out.flush();
		    out.close();
		    
		    System.out.println("downloadFileEx END:" + vdiskcore.token + "," + path + "\n\r");

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			this.err_code = 40001;
			this.err_message = e.getMessage();
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			this.err_code = 40002;
			this.err_message = e.getMessage();
		}   
	   
   }
   
   /*
    * upload the file that's size not bigger than 10MB
    */
   public void uploadFile(String localFile,String remoteDir,String isCover)
   {
	   
	   //get directory id
	   URI url;
	   
	   try {
		   
	   url = new URI(UPLOAD_FILE_URL);
	 
	   
	   HttpClient httpclient = new DefaultHttpClient();
	   HttpPost httppost = new HttpPost(url);

	   FileBody bin = new FileBody(new File(localFile));
	   StringBody token = new StringBody(vdiskcore.token);
	   StringBody dir = new StringBody(remoteDir);
	   StringBody cover = new StringBody(isCover);

	   
	   MultipartEntity reqEntity = new MultipartEntity();
	   reqEntity.addPart("token", token);
	   reqEntity.addPart("file", bin);
	   reqEntity.addPart("dir", dir);
	   reqEntity.addPart("cover", cover);
	   
	   
	   httppost.setEntity(reqEntity);
	   
	   HttpResponse response;
		
		response = httpclient.execute(httppost);
		HttpEntity resEntity = response.getEntity();
		
		byte [] buffer = new byte[8192];
		int readBytes = resEntity.getContent().read(buffer);
		buffer = Arrays.copyOf(buffer, readBytes);
		
		System.out.println(new String(buffer,"UTF-8"));
		
	} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (URISyntaxException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
   }
   
   
   /*
    * upload the file that's size more than 10MB
    * NOT Implement
    */
   public void uploadFileEx(String localFile,String remoteDir,String isCover)
   {
	   
   }
   
   
   /*
    * get single file information
    */
   public vdiskResponse.fileInfo getFileInfo(String token,String fileid)
   {
	   URL url;
		try {
			url = new URL(GET_FILEINFO_URL);
	
	       HttpURLConnection yc = (HttpURLConnection) url.openConnection();
	       
           yc.setDoOutput(true);  
           yc.setDoInput(true);  
	       
           //POST params
	       params ps = new params();
	       ps.add("token",token);
	       ps.add("fid", fileid);

	       yc.setRequestMethod("POST");
	       //
	       
	       yc.connect();
	       
           PrintStream send = new PrintStream(yc.getOutputStream());  
           send.print(ps.getParseString());  
           send.close();  

	       //
	       
	       
	       
	       BufferedReader in = new BufferedReader(
	                               new InputStreamReader(
	                               yc.getInputStream()));
	       String inputLine;
	
	       String buffer="";
	       
	       while ((inputLine = in.readLine()) != null) 
	       {
	           System.out.println(inputLine);
	           buffer +=inputLine;
	       }
	       in.close();
	       
	       Gson gs = new Gson();
	       
	       vdiskResponse.fileInfo fileInfo = gs.fromJson(buffer, vdiskResponse.fileInfo.class);
	       
       
		   return fileInfo;			
		   
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
   }
   
   
   /*
    * get Directory Id
    */
   private vdiskResponse.DirId getDirId(String token,String path)
   {
	   
	   System.out.println("GETDIRID:" + token + "," + path);
	   URL url;
		try {
			url = new URL(GET_DIRID_URL);
	
	       HttpURLConnection yc = (HttpURLConnection) url.openConnection();
	       
           yc.setDoOutput(true);  
           yc.setDoInput(true);  
	       
           //POST params
	       params ps = new params();
	       ps.add("token",token);
	       ps.add("path", path);

	       yc.setRequestMethod("POST");
	       //
	       
	       yc.connect();
	       
           PrintStream send = new PrintStream(yc.getOutputStream());  
           send.print(ps.getParseString());  
           send.close();  

	       //
	       
	       
	       
	       BufferedReader in = new BufferedReader(
	                               new InputStreamReader(
	                               yc.getInputStream()));
	       String inputLine;
	   	
	       String buffer="";
	       
	       while ((inputLine = in.readLine()) != null) 
	       {
	           System.out.println(inputLine);
	           buffer +=inputLine;
	       }
	       in.close();
	       
	       Gson gs = new Gson();
	       
	       vdiskResponse.DirId dirid = gs.fromJson(buffer, vdiskResponse.DirId.class);
	       
       
		   return dirid;			
		   
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			this.err_code = 20001;
			this.err_message = e.getMessage();
		} catch (NoRouteToHostException e)
		{
			e.printStackTrace();
			
			this.err_code = 20002;
			this.err_message = e.getMessage();
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			this.err_code = 20003;
			this.err_message = e.getMessage();
		}
		return null;
   }
   
   /*
    * POST参数格式类
    */
   private class param
   {
	   public String name;
	   public String value;
	   
	   param(String _name,String _value)
	   {
		   name=_name;
		   value=_value;
	   }
   }
   
   private class params
   {
	   ArrayList <param> p= new ArrayList<param>();
	   
	   public void add(String name,String value)
	   {
		   p.add(new param(name,value));
	   }
	   
	   public String getParseString()
	   {
		   String returnString = "";
		   for (int i=0;i<p.size();i++)
		   {
			   try {
				returnString += p.get(i).name +"="+URLEncoder.encode(p.get(i).value,"UTF-8") + "&";
				//returnString += p.get(i).name +"="+p.get(i).value + "&";
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   }
		   
		   return returnString.substring(0,returnString.length()-1);
	   }
   }

   
   private synchronized void _fireProgressEvent() {
       vdiskEvent vde = new vdiskEvent( this, _progress,_speed);
       Iterator<vdiskListener> listeners = _listeners.iterator();
       while( listeners.hasNext() ) {
           ( (vdiskListener) listeners.next() ).vdiskReceived( vde );
       }
   }
   
   public synchronized void addVdiskListener( vdiskListener l ) {
       _listeners.add( l );
   }
   
   public synchronized void removeVdiskListener( vdiskListener l ) {
       _listeners.remove( l );
   }
   
   public interface vdiskListener 
   {
       public void vdiskReceived( vdiskEvent event );
   }
   
   /**
    * 对字符串加密,加密算法使用MD5,SHA-1,SHA-256,默认使用hmacSHA256
    * 
    * @param strSrc
    *            要加密的字符串
    * @param encName
    *            加密类型
    * @return
    */ 
   public static String Encrypt(String strSrc, String encName,String appkey) {
       Mac md = null;
       String strDes = null;
       
       
       try {
    	   
    	   byte[] bt = strSrc.getBytes("UTF-8");
    	   
           if (encName == null || encName.equals("")) {
               encName = "hmacSHA256";
           }
           SecretKey key = new SecretKeySpec(appkey.getBytes("UTF-8"), "HmacSHA256" );
           
           md = Mac.getInstance(encName);
           md.init(key);
           
           md.update(bt);
           strDes = bytes2Hex(md.doFinal()); // to HexString
       } catch (NoSuchAlgorithmException e) {
           return null;
       } catch (InvalidKeyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       return strDes;
   }

   public static String bytes2Hex(byte[] bts) {
       String des = "";
       String tmp = null;
       for (int i = 0; i < bts.length; i++) {
           tmp = (Integer.toHexString(bts[i] & 0xFF));
           if (tmp.length() == 1) {
               des += "0";
           }
           des += tmp;
       }
       return des;
   }
   
   /*
    * 
    */
   private class keepTokenThread extends Thread
   {
	   private String token;
	   
	   public keepTokenThread(String token)
	   {
		   this.token = token;
	   }
	   
	   public String getToken()
	   {
		   return token;
	   }
	   
	   public void setToken(String token)
	   {
		   this.token = token;
	   }
	   
		public void run()
		{
			while(true){
				try {
					Thread.sleep(100000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("KEEP-TOKEN:" + token);
				
				vdiskResponse.kToken t = this.keepToken(this.token);
				
				vdiskcore.token = this.token;
				
				if (t!=null){
					try{
					System.out.println("KEEP-TOKEN RESPONSE:" + t.getData().getUid());
					}catch(NullPointerException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		
		/*
		 * KeepToken	
		 */
	   private vdiskResponse.kToken keepToken(String token) 
	   {
		   
		   URL url;
			try {
				url = new URL(KEEP_TOKEN_URL);
		
		       HttpURLConnection yc = (HttpURLConnection) url.openConnection();
		       
	           yc.setDoOutput(true);  
	           yc.setDoInput(true);  
		       
	           //POST params
		       params ps = new params();
		       ps.add("token",token);


		       
		       yc.setRequestMethod("POST");
		       //
		       
		       yc.connect();
		       
	           PrintStream send = new PrintStream(yc.getOutputStream());  
	           send.print(ps.getParseString());  
	           send.close();  

		       //
		       
		       
		       
		       BufferedReader in = new BufferedReader(
		                               new InputStreamReader(
		                               yc.getInputStream()));
		       String inputLine;
		
		       String buffer="";
		       
		       while ((inputLine = in.readLine()) != null) 
		       {
		           System.out.println(inputLine);
		           buffer +=inputLine;
		       }
		       in.close();
		       
		       Gson gs = new Gson();
		       
		       vdiskResponse.kToken gt = gs.fromJson(buffer, vdiskResponse.kToken.class);
		       
		       if (gt!=null)
		    	   vdiskcore.token = gt.getData().getToken();
		       
			   return gt;		
			   
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (NullPointerException e)
			{
				e.printStackTrace();
			}
			
			return null;
	   }
   }
   
}
