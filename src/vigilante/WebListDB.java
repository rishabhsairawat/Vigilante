package vigilante;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class WebListDB implements dbInterface {
	
	
	Connection conn;
	Statement stmt;
	ResultSet rs;
	
	WebListDB(){
		conn=null;
		stmt=null;
		rs=null;
	}
	
        @Override
	public void connect(){ 
 		try{
			  conn= DriverManager.getConnection(DB_URL);
 		}catch(SQLException e){
		}
 	}
	
	
        @Override
	public void close(){  
		try{
			if(rs!=null)
				rs.close();
			if(stmt!=null)
				stmt.close();
			if(conn!=null)
				conn.close();
 		}
		catch(SQLException e){
		}
 	}
	
	
	public void insert(String url){
		String sql;
		stmt=null;
		String name="";
		for(int i=0;i<url.length();i++){
			int flag=0;
			if(url.charAt(i)=='.'){
				for(int j=0;url.charAt(i+j)!='.';j++){
					name=name+url.charAt(i+j);
					flag=1;
				}
			}
			if(flag==1) break;
		}
		try{
				stmt= conn.createStatement();
				sql="insert into weblist values ("+name+","+url+");";
				int j=stmt.executeUpdate(sql);
				System.out.println("number of rows affected are: "+j);
			}
		catch(SQLException e){
			System.out.println("couldn't insert");
		}
	}
	
	public void delete(String url){
		String sql;
		stmt=null;
		try{
				stmt= conn.createStatement();
				sql="delete from weblist where url like "+url+";";
				int j=stmt.executeUpdate(sql);
				System.out.println("number of rows affected are: "+j);
			}
		catch(SQLException e){
			System.out.println("couldn't delete ");
		}
	}
	
	
        @Override
	public void deleteAll(){
		String sql;
		stmt=null;
		try{
				stmt= conn.createStatement();
				sql="delete from weblist;";
				int j=stmt.executeUpdate(sql);
				System.out.println("number of rows affected are: "+j);
			}
		catch(SQLException e){
			System.out.println("couldn't delete ");
		}
	}
	
	
	public ArrayList<Weblist> view()
	{
		String sql;
		stmt=null;
		rs=null;
		ArrayList<Weblist> a=new ArrayList<>();
		try{
			stmt= conn.createStatement();
			sql="select * from weblist";
			rs=stmt.executeQuery(sql);
			while(rs.next()){
				a.add(new Weblist(rs.getString("name"), rs.getString("url")));
			}
		}
		catch(SQLException e)
		{
		}
		return a;
	}

}
