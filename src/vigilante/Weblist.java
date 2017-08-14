package vigilante;

public class Weblist {
	String name;
	String url;
	
	public Weblist(String name,String url){
		this.url=url;
		this.name=name;
	}
	
	public void seturl(String a){
		url=a;
	}
	public String geturl(){
		return url;
	}
	public void setname(String a){
		name=a;
	}
	public String getname(){
		return name;
	}
}
