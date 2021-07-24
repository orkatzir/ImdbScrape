import java.awt.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class scrape {

	public static void main(String[] args) {
		// TODO Auto-generated method 
		if(args.length!=2)
		{
			System.out.println("One or two of the arguments is not valid, please read readem file again.");
			return;
		}
		String Search=args[0];
		int MaxResults=Integer.parseInt(args[1]);
		int ResultsNum=0;
		String Output="";
		//String Searchurl= "https://www.imdb.com/find?q="+Search+"&s=tt&ttype=ft&exact=true&ref_=fn_tt_ex";//search address	
		String Searchurl="https://www.imdb.com/find?q="+Search+"&s=tt&ttype=ft&ref_=fn_ft";
		try 
		{
		      File myObj = new File("MovieResults.txt");
		      myObj.createNewFile();
		      FileWriter myWriter = new FileWriter("MovieResults.txt");
		      

		      Document doc = Jsoup.connect(Searchurl).get();//jsoup connect to search address
		      for( Element row:doc.select("table.findList tr"))
	            {

		    	  final String FullTitle=row.select(".result_text ").text();
	              final String Title=row.select(".result_text a").text();
	              Elements tds = row.select("td");
	              String link = tds.get(0).getElementsByTag("a").attr("href");
	              if(!FullTitle.contains("(in development)") && Title.toLowerCase().contains(Search.toLowerCase()))// not include in development movies
	              {
			    	if(ResultsNum==MaxResults)
			           break;
	            	Output+='●'+" "+Title+'|'; // Add Title
	            	String url= "https://www.imdb.com"+link;//movie page address
	    			Document document = Jsoup.connect(url).get();//jsoup connect to movie page
	    			Elements GenereEl=document.getElementsByAttributeValue("data-testid","genres").select("span.ipc-chip__text");
	    			for (Element Genere:GenereEl)
        			{
        				Output+=Genere.text()+',';// Add Genres List
        			}
        			Output=Output.substring(0,Output.length()-1);
        			Output+="|";
	    			Element  DetailsElm = document.getElementsByClass("ipc-inline-list ipc-inline-list--show-dividers TitleBlockMetaData__MetaDataList-sc-12ein40-0 dxizHm baseAlt").first();
	    			if(DetailsElm==null||DetailsElm.childNodeSize()==1)
	    				Output+="Empty|Empty|";
	    			else
	    			{
	    				if(DetailsElm.childNodeSize()==2 && DetailsElm.child(1).childNodeSize()==2)//mpa exists,duration empty
	    				{
	    					Output+=DetailsElm.child(1).child(0).text()+"|Empty|";
	    				}
	    				if(DetailsElm.childNodeSize()==2 && DetailsElm.child(1).childNodeSize()==1)//Duration exitst,Mpa empty
	    				{
	    					Output+="Empty|"+ DetailsElm.child(1).text()+"|";
	    				}
	    				if(DetailsElm.childNodeSize()==3)//all data exists
	    				{
	    					Output+=DetailsElm.child(1).child(0).text()+"|";
	    					Output+=DetailsElm.child(2).text()+"|";
	    					
	    				}
	    				
	    				

	    			}
	    			
	    			

       
	    			Elements DirectorsEl = document.getElementsByClass("ipc-inline-list ipc-inline-list--show-dividers ipc-inline-list--inline ipc-metadata-list-item__list-content baseAlt").first().children();
	    			if(DirectorsEl!=null)
	    			{
	    			for (Element Directors:DirectorsEl){				 
	    				Output+=Directors.text()+',';//add director/s	  				
	    			Output=Output.substring(0,Output.length()-1);
	    			}
	    			}
	    			else
	    				Output+="Empty";
        			Output+="|";
                    Elements StarsEl=document.getElementsByAttributeValue("data-testid", "title-cast-item__actor");
                    if(StarsEl!=null)
                    {
                    int i=0;
	    			for (Element Stars:StarsEl)	
	    			{
	    				Output+=Stars.text()+',';//add stars
	    				i++;
	    				if(i==4)
	    					break;
	    			}
	    			Output=Output.substring(0,Output.length()-1);
                    }
                    else
                    {
                    	Output+="Empty";
                    }
	    			Output+='\n';
        			myWriter.write(Output);
        			Output="";
        			ResultsNum++;
                    }
	              
                    
	 
	              
		      
	            }
		      System.out.println(ResultsNum+" Results Were Found");
		      System.out.println("MovieResults.txt Was Created Succesfully");
		      myWriter.close();
		      
         
		    



		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
