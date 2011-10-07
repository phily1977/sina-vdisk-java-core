package vdiskcore;

import vdiskcore.vdiskcore.vdiskListener;



public class vdiskTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		vdiskcore vdc = new vdiskcore();
		
		vdiskListener  dProgress = new vdiskProgress();
		
		vdc.addVdiskListener(dProgress);
		
		if (!vdc.initailize("discovery_it@hotmail.com", "123456", "99464290", "a42ebeaf6524e0fb68e3063493d423d5"))
		{
			System.out.println(vdc.err_code + "|"+ vdc.err_message);
		}
		
		
		//download the files in default application directory
		vdiskResponse.listItems lstItems = vdc.getList("/MediaBox/Audio");
		
		
		for(int i=0;i<lstItems.getList().length;i++)
		{
			System.out.println(i+"|"+vdiskcore.token);
			vdiskResponse.listItem li = lstItems.getList()[i];
			
			
			
			if ((li.getType()!=null)&&(li.getType().equals("audio/mp3"))){
				//vdc.getFileInfoByName("/MediaBox/Audio", li.getName());
				vdc.downloadFileEx("/MediaBox/Audio", li.getName(), "D:\\MediaBox\\audio\\");
				
				System.out.println(vdc.err_code + "|" + vdc.err_message);
				
			}
		}
		
		//vdc.uploadFile("D:\\g194.png", "/", "yes");
		
  
		//vdc.getFileInfo(vdiskcore.token, "11179381");
		//vdiskResponse.Token token = (vdc.getToken("discovery_it@hotmail.com", "123456", "99464290", "a42ebeaf6524e0fb68e3063493d423d5",System.currentTimeMillis()));
		
		//System.out.println(token.getData().getToken());
		
		//vdiskResponse.DirId dirid = vdc.getDirId(token.getData().getToken(), "/MediaBox/Audio");
		
		//vdiskResponse.listFiles lstFiles = vdc.getList(token.getData().getToken(), Long.toString(dirid.getData().getId()));
	    vdc.close();
	}

	public class uploadProgress implements vdiskListener {
	    public void vdiskReceived(vdiskEvent event) {
	    	
	    	System.out.println(event.progress());
	    	
	        /*if( event.mood() == Mood.HAPPY )
	        {
	            System.out.println( "Sun is shining!" );
	        }
	        else if( event.mood() == Mood.ANNOYED )
	        {
	            System.out.println( "Cloudy Skies!" );
	        }
	        else
	        {
	            System.out.println( "Lightning rains from the heavens!" );
	        }*/
	    }
	}  	
	
}
