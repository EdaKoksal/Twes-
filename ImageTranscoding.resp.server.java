#rights=ADMIN
//------------------------------------------------------------------- 
// ==ServerScript==
// @name            ImageTranscoding
// @status on
// @description     
// @include        .*
// @exclude        
// @responsecode    200
// ==/ServerScript==
// --------------------------------------------------------------------
// Note: use httpMessage object methods to manipulate HTTP Message
// use debug(String s) method to trace items in service log (with log level >=FINE)
// ---------------
 
// ---------------
import java.util.*;
import java.io.*;
import java.util.concurrent.*;
public void main(HttpMessage httpMessage)
{
    String RequestHeader,baseName,body,ResponseHeader,originalURL;
    try
    {
        if(httpMessage.getResponseHeader("Content-Type").contains("text/html"))
        {                     
            body = httpMessage.getBody();
            if(body.indexOf("<img") <=0)
            {
                RequestHeader = httpMessage.getRequestHeaders();                
                baseName = FindRequestName(RequestHeader);
                originalURL = FindOriginalURL(RequestHeader); 
                return;
            }
            else
            {
                RequestHeader = httpMessage.getRequestHeaders(); 
                baseName = FindRequestName(RequestHeader);
                originalURL = FindOriginalURL(RequestHeader);
                int percentage = 100;
                percentage = FindTheDevice(RequestHeader);            
                body = ModifyThePage(body, baseName,originalURL,percentage);
            }
            httpMessage.setBody(body);
        }
        httpMessage.addHeader("Server","GreasySpoon_EDA");
    }
    catch(Exception e){debug(e.getMessage());} 
    System.out.println("\nTwes+ is Done!\n");  
    System.out.println("\n*************************\n");
}

public static int FindTheDevice(String requestHeader)
{
    String userAgent="User-Agent:";
    int percentage=100;
    String device1="Windows NT"; // Desktop
    String device2="GT-I9505"; //Samsung S4
    String device3="Nexus 7"; //Nexus 7
    String device4="iPad"; //iPad
    String device5="GT-S5830i"; //Samsung ACE
   
    if(requestHeader.contains(userAgent))
    {
         String baseName_p1 [] = requestHeader.split(userAgent,2);
         if(baseName_p1[1].contains(device1)){percentage=100;}
         else if(baseName_p1[1].contains(device2)){percentage=50;}
         else if(baseName_p1[1].contains(device5)){percentage=50;}
    }
    return percentage;
}

public static String FindRequestName(String requestHeader)
{
    String keyword = "//",keyword2="\\.",keyword3="com",name="Eda",name2="Eda";
    String keywordCOM = "com", keywordORG="org";
    String baseName_p1 [] = requestHeader.split(keyword,2);
    String baseName_p2 [] = baseName_p1[1].split(keyword2,2);
    String IPurl= "164.225.16.3/Consolidation/";
     
    
    if(baseName_p2[0].equals("www"))
    {
        String baseName_p3 [] = baseName_p2[1].split(keyword2,2);
        name = baseName_p3[0];         
    }
    else
    {
        name = baseName_p2[0]; 
    }
     
    if(baseName_p1[1].contains(".html"))
    {
        String baseName_p3 [] = baseName_p1[1].split(" ",2);
        String baseName_p4 [] = baseName_p3[0].split(".html",2);
        int p = baseName_p4[0].lastIndexOf("/");
        name2=baseName_p4[0].substring(p+1); 
        name=name+name2; 
    }      
    return name;
}

public static String FindOriginalURL(String requestHeader)
{
    String keyword = "//",keyword2="\\.",keyword3=" HT",name="Eda";
    String keywordCOM = "com", keywordORG="org";
    String baseName_p1 [] = requestHeader.split(keyword,2);
    String baseName_p2 [] = baseName_p1[1].split(keyword3,2);
    String urlOriginal = null;
    
	if(baseName_p2[0].contains(".html"))
    {
        int p = baseName_p2[0].lastIndexOf("/");
        baseName_p2[0]=baseName_p2[0].substring(0,p)+"/";
    }
    return baseName_p2[0];
}

public static String ModifyThePage(String body, String nameRequest, String originalURL,int percentage)
{
	body = Level2_AddEmptyStyle(body);   
    String body2 = Level2_FindImageDetailsFromTag(body, nameRequest, originalURL,percentage); 
    return body2;
}


public static String Level2_AddEmptyStyle( String bodyPart)
{
    String keyword = "</head>";
    String BodyStyleDivision [];
    BodyStyleDivision = bodyPart.split(keyword,2);
    String styleAdding = "\n<style>\n<!--EDA-->\n</style>\n";
    bodyPart = BodyStyleDivision[0] + styleAdding + keyword + BodyStyleDivision[1
    return bodyPart;
}

public static String Level2_FindImageDetailsFromTag(String bodyPart, String nameRequest,String originalURL,int percentage)
{
	String keyword = "<img";
	String keyword2 = "src=\"";     
	String bodyPart1, bodyPart2;
	String bodyPart2_1 [];
	String bodyPart3_1 [];
	String bodyPart4_1 [];
	String tm;
	String width,height,addition,name,newName,urlImage="";
	String type [] = new String[3];
	type[0] = null; type[1] = null;type[2] = null;

	int i=1;
	String numi = null;     
	int iPNG=1, iJPG=1, iJPEG=1, iGIF=1, iNONE=1;
	String numiPNG="1", numiJPG="1",numiJPEG="1",numiGIF="1",numiNONE="1";
   
	int iPNG_Out=1, iJPG_Out=1, iJPEG_Out=1, iGIF_Out=1, iNONE_Out=1;
	String numiPNG_Out="1", numiJPG_Out="1",numiJPEG_Out="1",numiGIF_Out="1",numiNONE_Out="1";
   
	String inbody=null;
	int total = 0,problem=0,total2=0;
	PrintStream out = null;
	PrintStream out2 = null;
    
	ArrayList imageDetails = new ArrayList();
	String [] newDimensions = new String[4];
	int index = bodyPart.indexOf(keyword);
     
	while(index >=0)
	{
		int inbodyLength = 0;
		inbody=null;
		bodyPart1 = bodyPart.substring(0,index);
		bodyPart2 = bodyPart.substring(index);
		bodyPart2_1 = bodyPart2.split(">",2);
        
		if(bodyPart2_1[0].contains("data:image"))
		{System.out.println("\ndata:image\n"); problem++;System.out.println("\n problem:" + problem +"\n");}
		else if(bodyPart2_1[0].contains("data-bind"))
		{System.out.println("\ndata-bind\n"); problem++;System.out.println("\n problem:" + problem +"\n");}
		else if(bodyPart2_1[0].contains("data-link"))
		{System.out.println("\ndata-link\n"); problem++;System.out.println("\n problem:" + problem +"\n");}
		else if(bodyPart2_1[0].contains("src=\"{"))
		{System.out.println("\nsrc=\"{\n"); problem++;System.out.println("\n problem:" + problem +"\n");}
		else if(bodyPart2_1[0].contains("src=\'{"))
		{System.out.println("\nsrc=\'{\n"); problem++;System.out.println("\n problem:" + problem +"\n");}
		else if(bodyPart2_1[0].contains("src=\"about:blank"))
		{System.out.println("\nsrc=\"about:blank\n"); problem++;System.out.println("\n problem:" + problem +"\n");}
		else if(bodyPart2_1[0].contains("src=\"https"))
		{System.out.println("\nsrc=\"https\n"); problem++;System.out.println("\n problem:" + problem +"\n");}
		else if(bodyPart2_1[0].contains("src=\"http"))
		{System.out.println("\nsrc=\"http\n"); problem++;System.out.println("\n problem:" + problem +"\n");}
		else if(bodyPart2_1[0].contains("data-thumb"))
		{System.out.println("\ndata-thumb\n"); problem++;System.out.println("\n problem:" + problem +"\n");}
		else
		{
			width = Level3_FindImageWidth(bodyPart2_1[0]);
			height = Level3_FindImageHeight(bodyPart2_1[0]);
			name = Level3_FindNameOfImage(bodyPart2_1[0]);
			if(name.contains("?") && (name.contains("&") || name.contains(";")))
			{problem++;}
			else
			{
				type[0] = null; type[1] = null; type[2] = null;
				type = Level3_ImageType(bodyPart2_1[0]);
				urlImage = Level3_FindURLOfImage(bodyPart2_1[0],originalURL);
				total++;
				String details [] = new String[8];
            
				details[0]= name;
				details[2]= width;
				details[3]= height;             
				details[4]= type[0];
				details[7]= type[2];
            
				int foundDetails=0;
				String arraym []=null;
				Iterator itrm = imageDetails.iterator();
				while(itrm.hasNext())
				{
					arraym = new String[7];
					arraym = (String[])itrm.next();
					if(arraym[0].equals(name))
					{
						foundDetails=1;
						break;
					}
				}
    
				if(foundDetails==0) 
				{
					try
					{
						ProcessBuilder pb = new ProcessBuilder("/bin/sh","/home/pcroot/ScriptsEda/Consolidation/SupportingModule.sh",urlImage,width,height,nameRequest,details[7],numiPNG,numiJPG,numiJPEG,numiGIF,String.valueOf(total),String.valueOf(percentage));
						final Process process = pb.start();
						BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
						String line;             
						int k=0;
                  
						while((line = br.readLine()) != null)
						{
							newDimensions[k] = line;
							k++;                
                        }
					}
					catch(Exception e){e.printStackTrace();}
                    if(!newDimensions[2].equals("NONE") )
					{
						String ImageType []; 
						ImageType = newDimensions[2].split(" and ",3);
						details[0]= name;
						details[1]= ImageType[0];
						details[2]= newDimensions[0];
						details[3]= newDimensions[1];
						details[4]= type[0];
						int imageID=0;
                  
						if(ImageType[1].equals("PNG"))
						{
							imageID=iPNG/10;
							details[5] = "OutPNG"+nameRequest+"_"+Integer.toString(imageID)+".png";
							iPNG++;
							numiPNG = Integer.toString(iPNG);
						}
						else if(ImageType[1].equals("JPEG") || ImageType[1].equals("JPG"))
						{
							imageID=iJPEG/10;
							details[5] = "OutJPEG"+nameRequest+"_"+Integer.toString(imageID)+".jpeg";
							iJPEG++;
							numiJPEG = Integer.toString(iJPEG);
						}
						else if(ImageType[1].equals("GIF"))
						{
							imageID=iGIF/10;
							details[5] = "OutGIF"+nameRequest+"_"+Integer.toString(imageID)+".gif";
							iGIF++;
							numiGIF = Integer.toString(iGIF);
						}
						String cssString [];
						cssString = ImageType[0].split("\\.",2);                    
						details[6] = cssString[0];
						details[7] = ImageType[1];
						try
						{
							String fileLocation = "/home/pcroot/logsEda/Consolidation/"+nameRequest+".txt";
							out = new PrintStream(new BufferedOutputStream(new FileOutputStream(fileLocation,true)));
							out.println("\n\n"+ total);
							out.println("\n\tName: "+ details[0]);
							out.println("\n\tNew Name: "+ details[1]);
							out.println("\n\twidth: "+ details[2]);
							out.println("\n\theigth: "+ details[3]);
							out.println("\n\ttype: "+ details[4]);
							out.println("\n\toutput: "+ details[5]);
							out.println("\n\tcss: "+ details[6]);
							out.println("\n\turlImage: "+ urlImage); 
							out.println("\n\toriginalURL:"+ originalURL);
							out.println("\n\ttypeCapital: "+ details[7]);
							out.println("\n\tPass or Fail: "+ newDimensions[2]);     
							out.close();
						}
						catch(Exception e)
						{e.printStackTrace();}
						imageDetails.add(details);
						inbody = Level3_3FindTheParts(bodyPart2_1[0],details);
                        
						bodyPart3_1 = bodyPart2_1[0].split(keyword2,2);         
						bodyPart4_1 = bodyPart3_1[1].split("\"",2);         
						String newPart = "";
						inbodyLength = inbody.length();
						bodyPart = bodyPart1 + inbody + bodyPart2_1[1];
                       
					}
                    else
					{
						problem++;
                    }
                }
				else if(foundDetails==1)
				{
					inbody = Level3_3FindTheParts(bodyPart2_1[0],arraym);
                    bodyPart3_1 = bodyPart2_1[0].split(keyword2,2);         
					bodyPart4_1 = bodyPart3_1[1].split("\"",2);         
					String newPart = "";
					inbodyLength = inbody.length();
					bodyPart = bodyPart1 + inbody + bodyPart2_1[1];
                }
            }      
        } 
		if(inbodyLength!=0)
			index = bodyPart.indexOf(keyword, index+inbodyLength);
		else
			index = bodyPart.indexOf(keyword, index + keyword.length());
		total2++;
    } 
      
    //CONSOLIDATION    
    System.out.println("\nCONSOLIDATION STARTED\n");
    String percent = "100"; 
	try
    {      
		ProcessBuilder pb2 = new ProcessBuilder("/bin/sh","/home/pcroot/ScriptsEda/Consolidation/ConsolidationModule.sh",nameRequest,percent);
		final Process process2 = pb2.start();
		process2.waitFor();
    }
	catch(Exception e){e.printStackTrace();}
    bodyPart = Part6_AddingCSSPart( bodyPart,imageDetails,percentage);
    //CONSOLIDATION
    
    try
    {
		String fileLocation = "/home/pcroot/logsEda/Consolidation/"+nameRequest+"FinalReport.txt";
        out2 = new PrintStream(new BufferedOutputStream(new FileOutputStream(fileLocation,true)));
        out2.println("\n\n\tFinal Total"+ total);
        out2.println("\n\tFinal problems: "+ problem);
        out2.println("\n\tUpdated Body: \n\n"+ bodyPart);
        out2.close();
    }
    catch(Exception e)
    {e.printStackTrace();}
	return bodyPart;
}

public static String Level3_FindImageWidth(String bodyPartImg)
{
	String width = "0";
    String bodyPart2_1 [];
    String widthNum_1 [];
    String delimWidth = "width=\"";
    String widthNum[];
    if(bodyPartImg.indexOf(delimWidth)<=0)
		return width;
    else
    {
        bodyPart2_1 = bodyPartImg.split(delimWidth,2);
        widthNum = bodyPart2_1[1].split("\"",2);
        if(widthNum[0].contains("px"))
        {
            widthNum_1=widthNum[0].split("px",2);
            return widthNum_1[0];
        }
        else
			return widthNum[0];
    }
}


public static String Level3_FindImageHeight(String bodyPartImg)
{
    String height = "0";
    String bodyPart2_1 [];
    String heightNum_1 [];
    String delimHeight = "height=\"";
    String heightNum[];
    if(bodyPartImg.indexOf(delimHeight)<=0)
        return height;
    else
    {
        bodyPart2_1 = bodyPartImg.split(delimHeight,2);
        heightNum = bodyPart2_1[1].split("\"",2);
        if(heightNum[0].contains("px"))
        {
            heightNum_1=heightNum[0].split("px",2);
            return heightNum_1[0];
        }
        else
            return heightNum[0];
    }
}


public static String Level3_FindNameOfImage(String bodyPartImg)
{
	String name = null;
    String keyword1 = "src=\"";
    String keyword2 = "src=\'";
    int index = 0;
    String nameDivision = "Twes+";
    
    if (bodyPartImg.contains(keyword1))
    {   
        String srcDivision [] = bodyPartImg.split(keyword1,2);
        String urlDivision [] = srcDivision[1].split("\"",2);
        index = urlDivision[0].lastIndexOf("/");
        nameDivision = urlDivision[0].substring(index+1);
    }
    else if(bodyPartImg.contains(keyword2))
    {
        String srcDivision [] = bodyPartImg.split(keyword2,2);
        String urlDivision [] = srcDivision[1].split("\'",2);
        index = urlDivision[0].lastIndexOf("/");
        nameDivision = urlDivision[0].substring(index+1);
    }  
    return nameDivision;   
}


public static String [] Level3_ImageType(String bodyPart)
{
    String type [] = new String[3];
    type[0] = null;type[1]=null;type[2] = null;
    int PNGIndex=0,JPGIndex=0,JPEGIndex=0,GIFIndex=0;
    PNGIndex = bodyPart.indexOf(".png");
    JPGIndex = bodyPart.indexOf(".jpg");
    JPEGIndex = bodyPart.indexOf(".jpeg");
    GIFIndex = bodyPart.indexOf(".gif");
     
     
    if(PNGIndex>0 || JPGIndex>0 || JPEGIndex>0 || GIFIndex>0)
    {
		int minIndex=99999;
        if(PNGIndex>0) 
        {
			type[0] = ".png";
			type[1] = "@EDApng@";
			type[2] = "PNG";
			minIndex=PNGIndex;
        }
        if(JPGIndex>0 && JPGIndex<minIndex) 
        {
			type[0]=null;
			type[1]=null;
			type[2] = null;
			type[0] = ".jpg";
			type[1] = "@EDAjpg@";
			type[2] = "JPG";
			minIndex=JPGIndex;
        }    
        if(JPEGIndex>0 && JPEGIndex<minIndex) 
        {
			type[0]=null;
			type[1]=null;
			type[2] = null;
			type[0] = ".jpeg";
			type[1] = "@EDAjpeg@";
			type[2] = "JPEG";
			minIndex=JPEGIndex;
        }  
        if(GIFIndex>0 && GIFIndex<minIndex) 
        {
			type[0]=null;
			type[1]=null;
			type[2] = null;
			type[0] = ".gif";
			type[1] = "@EDAgif@";
			type[2] = "GIF";
			minIndex=GIFIndex;
        }
    }
    else 
    {
		type[0]=null;
		type[1]=null;
		type[2] = null;
		type[0] = "";
		type[1] = "@EDAnone@";
		type[2] = "NONE";
    }
	return type;
}


public static String Level3_FindURLOfImage(String bodyPartImg,String originalURL)
{
    String name = null;
    String keyword3 = "data-url=\"";
    String keyword2 = "src=\"";
    String keyword1 = "src=\'";
    int index = 0,b=0;
    String urlDiv = null;
    String srcDivision [] = null;
    String urlDivision[] =null;
    if (bodyPartImg.contains(keyword1))
    {   
        srcDivision = bodyPartImg.split(keyword1,2);
		urlDivision = srcDivision[1].split("\'",2);
    }
    else if(bodyPartImg.contains(keyword2))
    {
        srcDivision = bodyPartImg.split(keyword2,2);
		urlDivision = srcDivision[1].split("\"",2);
    }
    else if(bodyPartImg.contains(keyword3))
    {
        srcDivision = bodyPartImg.split(keyword3,2);
		urlDivision = srcDivision[1].split("\"",2);
    }  
        
    String location = "http://164.225.16.2/EDA";
    
    if(urlDivision[0] == null)
		return urlDiv="EMPTY";
    else
    { 
		urlDiv = urlDivision[0];
        
        if(urlDiv.startsWith("http")){b=1;}
        else if(urlDiv.startsWith("https")){b=1;}
        else if(urlDiv.startsWith("//")){urlDiv = "http:"+urlDiv;}
        else if(urlDiv.startsWith("/"))
		{
			String urlDivision2 [] = urlDiv.split(".",2);
            urlDiv = originalURL+urlDivision2[1];
        }
        else if(urlDiv.startsWith(".")) 
        {
            String urlDivision2 [] = urlDiv.split(".",2);
            urlDiv = "192.168.56.101"+urlDivision2[1];
        }
		else
		{
			urlDiv = originalURL+urlDiv;
        }
    } 
    return urlDiv;  
}

public static String Part6_AddingCSSPart(String bodyPart, ArrayList imageDetails,int percentage)
{
    int heightPNG = 0,heightJPG = 0,heightJPEG = 0,heightGIF = 0;
    int CounterPNG = 0,CounterJPG = 0,CounterJPEG = 0,CounterGIF = 0;
    String heightStringPNG= "0",heightStringJPG = "0", heightStringJPEG = "0", heightStringGIF = "0";
    String keyword = "<style>\n<!--EDA-->\n";
    String adding = "div { float:center; align:center; text-align: center;}\n";
    String pound="\u0023";
    Iterator itr = imageDetails.iterator();
    while(itr.hasNext())
    {
		String array [] = new String[7];
        array = (String[])itr.next();
        String myName="EDA"; // needed for the server part
        if(array[4].equals(".png"))
        {
			CounterPNG++;
            if(CounterPNG==10)
            {
                CounterPNG=0;
                heightPNG=0;
                heightStringPNG= "0";
            }
            if(percentage == 100)
                adding = adding + "\n."+array[6]+"{width: "+array[2]+"px; height: "+array[3] + "px; border: 0px; background:url(\'" +myName+array[5] +"\') 0 -" + heightStringPNG + "px;}\n";
			else              
                adding = adding + "\n."+array[6]+"{width: "+array[2]+"px; height: "+array[3] + "px; border: 0px; background:url(\'" +myName+array[5] +"\') 0 -" + heightStringPNG + "px; transform: scale(1.5,1.5);transform-origin:0 0;}\n";
			heightPNG = heightPNG + Integer.parseInt(array[3]);
            heightStringPNG=String.valueOf(heightPNG);
        }
        else if(array[4].equals(".jpeg") || array[4].equals(".jpg"))
        {
            CounterJPEG++;
            if(CounterJPEG==10)
            {
                  CounterJPEG=0;
                  heightJPEG=0;
                  heightStringJPEG = "0";
            }
            if(percentage == 100)
                adding = adding + "\n."+array[6]+"{width: "+array[2]+"px; height: "+array[3] + "px; border: 0px; background:url(\'" +myName+array[5] +"\') 0 -" + heightStringJPEG + "px;}\n";
            else
				adding = adding + "\n."+array[6]+"{width: "+array[2]+"px; height: "+array[3] + "px; border: 0px; background:url(\'" +myName+array[5] +"\') 0 -" + heightStringJPEG + "px; transform: scale(1.5,1.5); transform-origin:0 0;}\n";            
            
            heightJPEG = heightJPEG + Integer.parseInt(array[3]);
            heightStringJPEG=String.valueOf(heightJPEG);
        }
		else if(array[4].equals(".gif"))
        {
			CounterGIF++;
            if(CounterGIF==10)
            {
                CounterGIF=0;
                heightGIF=0;
                heightStringGIF = "0";
            }
            if(percentage == 100)
                adding = adding + "\n."+array[6]+"{width: "+array[2]+"px; height: "+array[3] + "px; border: 0px; background:url(\'" +myName+array[5] +"\') 0 -" + heightStringGIF + "px;}\n";
            else
                adding = adding + "\n."+array[6]+"{width: "+array[2]+"px; height: "+array[3] + "px; border: 0px; background:url(\'" +myName+array[5] +"\') 0 -" + heightStringGIF + "px; transform: scale(1.5,1.5); transform-origin:0 0;}\n";                   
            
            heightGIF = heightGIF + Integer.parseInt(array[3]);
            heightStringGIF=String.valueOf(heightGIF);
        }
    }
    adding = adding + "\n";
    int index = bodyPart.indexOf(keyword);
    String srcDivision [] = bodyPart.split(keyword,2);
    bodyPart = srcDivision[0] + keyword + adding + srcDivision[1];
    return bodyPart;
}

public static String Level3_3FindTheParts(String imgTag,String [] imageDetails)
{
    String keyword = "<img";
    String location = "http://164.225.16.2/EDA";
    String keyword1 = "src=";
    String idPart=" class=\""+imageDetails[6]+"\" src=\""+location+"spacer.gif\"";
    String keywordHeight = "height";
    String keywordWidth = "width";
    String keywordClass="class";
    String datasrc="data-src";
    String srcDivision1=null;
    String srcDivision2 = null;
    String sortPart [] = null;
    
    if(imgTag.contains(keywordClass))
    {
        String newTag [] = null;
        String newTag1 [] = null;
        newTag = imgTag.split(keywordClass,2);
        if(newTag[1].contains(" "))
        {
            newTag1 = newTag[1].split(" ",2);
            imgTag = newTag[0] + newTag1[1];
        }
        else
            imgTag=newTag[0];
    }
     
    if(imgTag.contains(datasrc))
    {
        String newTag [] = null;
        String newTag1 [] = null;
        newTag = imgTag.split(datasrc,2);
        if(newTag[1].contains(" "))
        {
            newTag1 = newTag[1].split(" ",2);
            imgTag = newTag[0] + newTag1[1];
        }
        else
            imgTag=newTag[0];
    }
     
    if(imgTag.contains(keyword1))
    {
        String newTag [] = null;
        String newTag1 [] = null;
        newTag = imgTag.split(keyword1,2);
        if(newTag[1].contains(" "))
        {
            newTag1 = newTag[1].split(" ",2);
            imgTag = newTag[0] + newTag1[1];
        }
        else
            imgTag=newTag[0];
    }
     
    int index = imgTag.indexOf(keyword);
    index=index+4;
    srcDivision1 = imgTag.substring(0,index);
    srcDivision2 = imgTag.substring(index);
    imgTag=null;
    imgTag = srcDivision1 + idPart + srcDivision2 + ">";    
    return imgTag;
}

public static String Part4_AddStyleDetailsinBody(String [] imageDetails)
{
	String inbody = null;
    String location = "http://164.225.16.2/EDA";//needed for the server part 
	inbody = "\n<img class=\""+imageDetails[6]+"\" src=\""+location+"spacer.gif\" />\n";
	return inbody;
}

public static String Part4_AddStyleDetails(ArrayList imageDetails,String bodyPart)
{
    String keyword = "</style>\n</head>";
    String addCss = null;
    String styleAddition = null;
    int heigthPNG = 0,heigthJPG=0,heigthJPEG=0,heigthGIF=0;
    String numiPNG="0", numiJPG="0",numiJPEG="0",numiGIF="0";
    
    int index = bodyPart.indexOf(keyword);
    String bodyPart1, bodyPart2;
    bodyPart1 = bodyPart.substring(0,index);
    bodyPart2 = bodyPart.substring(index);
    
  
    Iterator itr = imageDetails.iterator();
    itr = imageDetails.iterator();
    
    while(itr.hasNext())
    {
        String array [] = new String[7];
        array = (String[])itr.next();
        addCss = null;
        addCss = "";
        if(array[4].equals(".png"))
        {
			addCss = "." + array[6] + "{width: "+array[2]+"px; height: "+array[3]+"px; border: 0px; background:url('"+array[5]+"') 0 -"+numiPNG+"px;}";
            heigthPNG = (Integer.parseInt(array[3])) + (Integer.parseInt(numiPNG));
            numiPNG = String.valueOf(heigthPNG);
        }
        else if(array[4].equals(".jpeg") || array[4].equals(".jpg"))
        {
            addCss = "." + array[6] + "{width: "+array[2]+"px; height: "+array[3]+"px; border: 0px; background:url('"+array[5]+"') 0 -"+numiJPEG+"px;}";
            heigthJPEG = Integer.parseInt(array[3]) + Integer.parseInt(numiJPEG);
            numiJPEG = String.valueOf(heigthJPEG);
        }
        else if(array[4].equals(".gif"))
        {
            addCss = "." + array[6] + "{width: "+array[2]+"px; height: "+array[3]+"px; border: 0px; background:url('"+array[5]+"') 0 -"+numiGIF+"px;}";
            heigthGIF = Integer.parseInt(array[3]) + Integer.parseInt(numiGIF);
            numiGIF = String.valueOf(heigthGIF);                        
        }
        else
        {
			System.out.println("-"); 
        }
        
        if(styleAddition == null)
			styleAddition = addCss + "\n";
        else
            styleAddition += addCss + "\n";
    }
    bodyPart = bodyPart1 + styleAddition + bodyPart2;
    return bodyPart;
}


 
