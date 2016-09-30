#rights=ADMIN
//------------------------------------------------------------------- 
// ==ServerScript==
// @name            RedirectTranscodingService
// @status off
// @description     
// @include        .*
// @exclude        
// @responsecode    200 307
// ==/ServerScript==
// --------------------------------------------------------------------
// Note: use httpMessage object methods to manipulate HTTP Message
// use debug(String s) method to trace items in service log (with log level >=FINE)
// ---------------
 
// ---------------
//Redirect Transcoding Service
import java.io.*;
import java.net.*;
import java.net.URL;
public static int m=0;
public static int limit = 6;

public void main(HttpMessage httpMessage)
{
    String code =null;
    int found=0;
    String UrlOfWeb = null;
    
    String requestheader="HTTP/1.1 200 OK\r\nServer: Twes+\r\nContent-Type: text/html;charset=ISO-8859-1\r\n\r\n";
    String httprequest   = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"><html><head><title>Redirect response example</title></head>"
                          + "<body><h1>Sorry but there is infinite redirect - Twes+ !!</h1>"
                              + "</body>"
                            + "</html>";
    String RespHeader =null;
    String RespBody = null;
    PrintStream out2 = null;
    code= httpMessage.getType();
    RespHeader= httpMessage.getResponseHeaders();
    RespBody = httpMessage.getBody();
    String[] responseParts=null;
    String newBody="";
    
    if(code.equals("307"))
    {
		httpMessage.setHeaders(requestheader);         
        httpMessage.setBody("<!--Twes+-->\n"+httprequest);
		int n=0;
		UrlOfWeb=getURL(RespHeader);
		newBody="<!--Twes+-->\n";
		try
		{
			UrlOfWeb=UrlOfWeb.trim();
			ProcessBuilder pb = new ProcessBuilder("/bin/sh","/home/pcroot/ScriptsEda/Redirect/RedirectionModule.sh",UrlOfWeb.trim());
			final Process process = pb.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;             
			int k=0;
         
			while((line = br.readLine()) != null)
			{              
				if(line.trim().isEmpty()){}
                else{newBody+=line+"\n";}
            }
        }
		catch(Exception e){e.printStackTrace();}
        
		if(newBody.equals("<!--Twes+-->\n"))
            System.out.println(newBody);
        else
            httpMessage.setBody(newBody);
    }
    System.out.println("\nDONE\n");
    
}

public static String getURL(String responseBody)
{
    String RLocation1_1[] = responseBody.split("Location: ",2);
    String RLocation2_1[] = RLocation1_1[1].split("Content-Length: ",2);
    return RLocation2_1[0];
}







