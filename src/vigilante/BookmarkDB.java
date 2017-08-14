package vigilante;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BookmarkDB implements dbInterface {
	
	
	Connection conn=null;
	Statement stmt=null;
	ResultSet rs=null;
	
	public BookmarkDB(){
		
	}
	
	
        @Override
	public void connect(){ 
 		try{
 			Class.forName(JDBC_DRIVER);
			conn= DriverManager.getConnection(DB_URL);
                       
 		}catch(ClassNotFoundException | SQLException e){
                   
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
	
	
	public void insert(String name,String url){
		String sql;
		stmt=null;
		try{
				stmt= conn.createStatement();
				sql="insert into bookmarks values ('"+name+"','"+url+"');";
				int j=stmt.executeUpdate(sql);
				System.out.println("number of rows affected are: "+j);
			}
		catch(SQLException e){
			System.out.println("couldn't insert");
		}
	}
	
	public void delete(String name){
		String sql;
		stmt=null;
		try{
				stmt= conn.createStatement();
				sql="delete from bookmarks where name like '"+name+"';";
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
				sql="delete from bookmarks;";
				int j=stmt.executeUpdate(sql);
				System.out.println("number of rows affected are: "+j);
                            
			}
		catch(SQLException e){
			System.out.println("couldn't delete ");
		}
	}
	

	public ArrayList<Bookmark> view()
	{
		String sql;
		stmt=null;
		ArrayList<Bookmark> a=new ArrayList<>();
		try{
			stmt= conn.createStatement();
			sql="select * from bookmarks";
			rs=stmt.executeQuery(sql);
                        System.out.println("Inside Bookmarks");
			while(rs.next()){
				a.add(new Bookmark(rs.getString("name"), rs.getString("url")));
			}
		}
		catch(SQLException e)
		{
		}
		return a;
	}

}
