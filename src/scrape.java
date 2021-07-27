
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class scrape {

	public static void main(String[] args) {
		// TODO Auto-generated method 
		if(args.length!=2)
		{
			System.out.println("Number Of Parameters Is Not Correct, Please Read Readem File Again.");
			return;
		}
		String SearchVal=args[0];
		if(Integer.parseInt(args[1])<=0)
		{
			System.out.println("Number of Results is not valid,Please Enter Number greater then 0");
			return;
		}
		int MaxResults=Integer.parseInt(args[1]);
		List <MovieTitle> Titles=SearchImdb(SearchVal,MaxResults);
		if(Titles.size()==0)
		{
			System.out.println("No Results Were Found.");
			return;			
		}
		List <MovieDetails> Details=GetMovieDetails(Titles);
	    try {
			PrintToFile(Details);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error With File Creation Or Writing To File!");
		}
		
		
	}






	private static List<MovieTitle> SearchImdb(String searchVal, int maxResults) {
		// TODO Auto-generated method stub
		List<MovieTitle> Titles = new ArrayList<MovieTitle>();
		int index=0;
		String Searchurl="https://www.imdb.com/find?q="+searchVal+"&s=tt&ttype=ft&ref_=fn_ft";
		 try {
						 
	     Document doc = Jsoup.connect(Searchurl).get();//jsoup connect to search address

		 for( Element row:doc.select("table.findList tr"))
         {
			 final String FullTitle=row.select(".result_text ").text();
			 final String Title=row.select(".result_text a").text();
			 if(!FullTitle.contains("(in development)") && Title.toLowerCase().contains(searchVal.toLowerCase()))
			 {			 	 
			 index++;			 
			 Titles.add(new MovieTitle(Title,row.select("td").get(0).getElementsByTag("a").attr("href")));			 
			 if(index==maxResults)
				 break;
			 }
         }
		 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 return Titles;
	
	}
	private static List<MovieDetails> GetMovieDetails(List<MovieTitle> titles) {
		
		// TODO Auto-generated method stub
		List<MovieDetails> movieDetails=new ArrayList<MovieDetails>();
		Document document;
		int i=0;
		
		for(MovieTitle E:titles)
		{
			
			String Title=new String(),Mpa=new String(),Duration=new String();
			List <String> Generes = new ArrayList<String>(),Stars=new ArrayList<String>(),Directors=new ArrayList<String>();
        	String url= "https://www.imdb.com"+E.getUrl();//movie page address
			try {
				 
			document= Jsoup.connect(url).get();
			Title=E.getTitle();
			Elements GenereEL=document.getElementsByAttributeValue("data-testid","genres").select("span.ipc-chip__text");
			for (Element Genere:GenereEL)
			{
				Generes.add(Genere.text());// Add Genres List
			}
			Element  DetailsElm = document.getElementsByClass("ipc-inline-list ipc-inline-list--show-dividers TitleBlockMetaData__MetaDataList-sc-12ein40-0 dxizHm baseAlt").first();
			if(DetailsElm==null||DetailsElm.childNodeSize()==1)
				{
				   Mpa="Empty";
				   Duration="Empty";
				}
			else
			{
				if(DetailsElm.childNodeSize()==2 && DetailsElm.child(1).childNodeSize()==2)//mpa exists,duration empty
				{
					Mpa=DetailsElm.child(1).child(0).text();
					Duration="Empty";
				}
				if(DetailsElm.childNodeSize()==2 && DetailsElm.child(1).childNodeSize()==1)//Duration exitst,Mpa empty
				{
					Mpa="Empty";
					Duration=DetailsElm.child(1).text();
				}
				if(DetailsElm.childNodeSize()==3)//all data exists
				{
					Mpa=DetailsElm.child(1).child(0).text();
					Duration=DetailsElm.child(2).text();
					
				}
			}
			Elements DirectorsEL = document.getElementsByClass("ipc-inline-list ipc-inline-list--show-dividers ipc-inline-list--inline ipc-metadata-list-item__list-content baseAlt").first().children();
			if(DirectorsEL!=null)
			{
			for (Element Director:DirectorsEL){				 
				Directors.add(Director.text());//add director/s	  							
			}}
			else
				Directors.add("Empty");
            Elements StarsEL=document.getElementsByAttributeValue("data-testid", "title-cast-item__actor");
            if(StarsEL!=null)
            {
			for (Element Star:StarsEL)	
			{
				Stars.add(Star.text());//add stars
				i++;
				if(i==4)
					break;
			}}
            else
            {
            	Stars.add("Empty");
            }
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			movieDetails.add(new MovieDetails(Title,Generes,Mpa,Duration,Directors,Stars));

		}
		return movieDetails;
		
	}
	
	private static void PrintToFile(List<MovieDetails> Details) throws IOException {
		// TODO Auto-generated method stub

		File file=new File("MovieResults.txt");
		FileWriter myWriter = new FileWriter("MovieResults.txt");
		String Output="";
		if(Details.size()==0)
		{
			System.out.println("No Results Were Found!!");
			return;
		}
			
		file.createNewFile();		    	
		for(MovieDetails M:Details)
		{
			Output+='●'+" "+M.GetTitle()+'|';
			int G_Size=	M.GetGeneres().size();
			for(int i=0;i<G_Size;i++)
			{
				if(i!=(G_Size-1))
				   Output+=M.GetGeneres().get(i)+',';
				else
					Output+=M.GetGeneres().get(i)+'|';
			}
			
			Output+=M.GetMpa()+'|';
			Output+=M.GetDuration()+'|';
			int D_Size=	M.GetDirectors().size();
			for(int i=0;i<D_Size;i++)
			{
				if(i!=(D_Size-1))
				   Output+=M.GetDirectors().get(i)+',';
				else
					Output+=M.GetDirectors().get(i)+'|';
			}
			
			int S_Size=	M.GetStars().size();
			if(S_Size>4)
				S_Size=4;
			for(int i=0;i<S_Size;i++)
			{
				if(i!=(S_Size-1))
				   Output+=M.GetStars().get(i)+',';
				else
					Output+=M.GetStars().get(i);
			}
			
			Output+='\n';
			myWriter.write(Output);
			Output="";

			}
		   System.out.println("MovieResults.txt Was Created Succesfully");
		   myWriter.close();
			
			
		}
		
	}




