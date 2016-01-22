/**
* Maps a relational database table Person to a java object, Person
* This class was generated, do not edit it.
*
* nullCreated Thu Jan 21 16:22:49 CST 2016 From (catalog:schema:table): Portfolio:Accounts:Person
*
* @author	ReverseEngineerFactory
*/
import java.sql.*;
import java.util.*;
public class Person implements Serializable {
	private  java.lang.Integer Id;
	private  java.lang.String Email;
	private  java.lang.String Name;
	public java.lang.Integer getId( ) { return this.Id;}
	public void setId( java.lang.Integer id) { this.Id = id;}
	public java.lang.String getEmail( ) { return this.Email;}
	public void setEmail( java.lang.String email) { this.Email = email;}
	public java.lang.String getName( ) { return this.Name;}
	public void setName( java.lang.String name) { this.Name = name;}
} // class Person
