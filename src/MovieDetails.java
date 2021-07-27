import java.util.List;

public class MovieDetails {
  
	private String Title;
	private List<String> Generes;
	private String Mpa;
	private String Duration;
	private List<String> Directors;
    private List<String> Stars;
    
    
    
    public MovieDetails(String Title,List<String>Generes,String Mpa,String Duration,List<String> Directors,List<String> Stars)
    {
    	this.Title=Title;
    	this.Generes=Generes;
    	this.Mpa=Mpa;
    	this.Duration=Duration;
    	this.Directors=Directors;
    	this.Stars=Stars;
    }
	public String GetTitle()
	{
		return Title;
	}
	public List<String> GetGeneres()
	{
		return Generes;
	}
	public String GetMpa()
	{
		return Mpa;
	}
	public String GetDuration()
	{
		return Duration;
	}
	public List<String> GetDirectors()
	{
		return Directors;
	}
	public List<String> GetStars()
	{
		return Stars;
	}


	
}
