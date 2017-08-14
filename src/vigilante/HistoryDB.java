package vigilante;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class HistoryDB implements dbInterface {
    
    
    Connection conn;
    Statement stmt;
    ResultSet rs;
    
    HistoryDB(){
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
        try{
                stmt= conn.createStatement();
                sql="insert into history ('url','time')values ('"+url+"',datetime('now','localtime'));";
                System.out.println(sql);
                int j=stmt.executeUpdate(sql);
               
                System.out.println("number of rows affected are: "+j);
            }
        catch(SQLException e){
            System.err.print(e);
            System.out.println("couldn't insert");
        }
    }
    
    public void delete(String url,String time){
        String sql;
        stmt=null;
        try{
                stmt= conn.createStatement();
                sql="delete from history where url = '"+url+"' and time='"+time+"';";
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
                sql="delete from history;";
                int j=stmt.executeUpdate(sql);
                System.out.println("number of rows affected are: "+j);
                
                
            }
        catch(SQLException e){
            System.out.println("couldn't delete ");
        }
    }
    
    
    public ArrayList<History> view()
    {
        String sql;
        ArrayList<History> a=new ArrayList<>();
        stmt=null;
        rs=null;
        try{
            stmt= conn.createStatement();
            sql="select * from history order by time desc";
            rs=stmt.executeQuery(sql);
            System.out.println("Inside history Class");
            while(rs.next()){
                a.add(new History(rs.getString("url"), rs.getString("time")));
            }
        }
        catch(SQLException e)
        {
        }
        return a;
    }
    
    
    public ArrayList<History> viewone(String url)
    {
        String sql;
        stmt=null;
       ArrayList<History> a=new ArrayList<>();
        rs=null;
        try{
            stmt= conn.createStatement();
            sql="select * from history where url like '%"+url+"%';";
            rs=stmt.executeQuery(sql);
            while(rs.next()){
                a.add(new History(rs.getString("url"), rs.getString("time")));
            }
        }
        catch(SQLException e)
        {
        }
        return a;
    }

}
