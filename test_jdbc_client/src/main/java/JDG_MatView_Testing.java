/*
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.teiid.jdbc.TeiidDataSource;
import org.teiid.jdbc.TeiidStatement;

@SuppressWarnings("nls")
public class JDG_MatView_Testing {
	
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	
	public static final String USERNAME_DEFAULT = "teiidUser";
	public static final String PASSWORD_DEFAULT = "redhat1!";
	
	public static void main(String[] args) throws Exception {
		if (args.length < 3) {
			System.out.println("usage: JDBCClient <host> <port> <vdb>");
			System.exit(-1);
		}

		System.out.println("Executing using the TeiidDriver");

                JDG_MatView_Testing test = new JDG_MatView_Testing();
                Connection connection = null;
                try {   
                        connection = test.getDriverConnection(args[0], args[1], args[2]);
  //                      test.insertCustomer(connection, 3000, 13000);
                        // allow mat to process
                        Thread.sleep(5000);
 //test.externalLongTtlUpdateTest(connection, 19980002, 922222, "CST01002");
                       test.performTest(connection);
	        } catch (SQLException e) {
		        e.printStackTrace();
	        } finally {
		        if (connection != null) {
                                try {
			            connection.close();
                                } catch(Exception x) {}
		        }
	        }
  
	}

        private void performTest(Connection connection) throws Exception {

                      int cnt = 0;
                        while(true) {
                             
		                boolean isSelect = externalMatViewTest(connection);

                               System.out.println(cnt + "Values match: " + isSelect);
                                Thread.sleep(3000);
System.out.println("started executed loadmatview");
         Statement st2 = connection.createStatement();
                st2.execute("exec SYSADMIN.loadMatView(schemaName=>'PersonMatModel',viewname=>'PersonMatView', invalidate=>'true')");
System.out.println("ended executed loadmatview");

Thread.sleep(3000);
           st2.close();
                                ++cnt;
                        }
        }
	
	static Connection getDriverConnection(String host, String port, String vdb) throws Exception {
		String url = "jdbc:teiid:"+vdb+"@mm://"+host+":"+port+";showplan=on"; //note showplan setting
		Class.forName("org.teiid.jdbc.TeiidDriver");
		
		return DriverManager.getConnection(url,getUserName(), getPassword());		
	}
	
	static String getUserName() {
		return (System.getProperties().getProperty(USERNAME) != null ? System.getProperties().getProperty(USERNAME) : USERNAME_DEFAULT );
	}
	
	static String getPassword() {
		return (System.getProperties().getProperty(PASSWORD) != null ? System.getProperties().getProperty(PASSWORD) : PASSWORD_DEFAULT );
	}
	
	public  boolean execute(Connection connection, String sql) throws Exception {
                 Statement st2 = connection.createStatement();
                st2.execute("exec SYSADMIN.loadMatView(schemaName=>'PersonMatModel',viewname=>'PersonMatView', invalidate=>'true')");
System.out.println("ended executed loadmatview");
           st2.close();
        boolean hasRs = true;
		try {
			Statement statement = connection.createStatement();
			
			hasRs = statement.execute(sql);
			
			if (!hasRs) {
				int cnt = statement.getUpdateCount();
				System.out.println("----------------\r");
				System.out.println("Updated #rows: " + cnt);
				System.out.println("----------------\r");
			} else {
				ResultSet results = statement.getResultSet();
				ResultSetMetaData metadata = results.getMetaData();
				int columns = metadata.getColumnCount();
				System.out.println("Results");
				for (int row = 1; results.next(); row++) {
					System.out.print(row + ": ");
					for (int i = 0; i < columns; i++) {
						if (i > 0) {
							System.out.print(",");
						}
						System.out.print(results.getString(i+1));
					}
					System.out.println();
				}
				results.close();
			}
			System.out.println("Query Plan");
			System.out.println(statement.unwrap(TeiidStatement.class).getPlanDescription());
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		return hasRs;
	}



    public boolean externalMatViewTest(Connection connection) throws Exception {
 

            Statement statement = connection.createStatement();
//System.out.println("executing SELECT COUNT(*) FROM PersonMatModel.PersonMatView ");
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM PersonMatModel.PersonMatView");
            
            boolean n = rs.next();
            if (! n) {
                throw new Exception("SELECT statement on external materialized view should return some rows.");
                }
            int viewCount = rs.getInt(1);


//System.out.println("executing SELECT COUNT(*) FROM PersonMatModel.PersonMatView OPTION NOCACHE ");
            ResultSet custCountRS = statement.executeQuery("SELECT COUNT(*) FROM PersonInfoModel.PersonInfo");
                n = custCountRS.next();
                if (! n) {
                        throw new Exception("SELECT statement on source view should return some rows.");
                }

            int count = custCountRS.getInt(1);
//System.out.println("found rows: " + count);
                if (count != viewCount) {
                        System.out.println("MatView: " + viewCount + " Cache: " + count);
                        throw new Exception("The materialized view and its underlaying table should return the same results.");
                }
	    statement.close();


        return true;
    }

    public void externalLongTtlUpdateTest(Connection connection, int id, int accountid, String ssn) throws Exception {
         Statement statement = null;
        try {

            statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT name FROM PersonMatModel.PersonMatView WHERE id=" + id);
           
            boolean n = rs.next();

            if (! n) {
                   rs.close();
                throw new Exception("SELECT statement on external materialized view should return some rows.");
                }

            String originalAmount = rs.getString(1);
            statement.execute("UPDATE ACCOUNTS.ACCOUNT SET account_id= " + accountid + " WHERE SSN='" + ssn + "'");
connection.commit();

            ResultSet rs2 = statement.executeQuery("SELECT name FROM PersonMatModel.PersonMatView WHERE id=" + id);

               n = rs2.next();
                if (! n) {
                     rs2.close();
                        throw new Exception("SELECT statement on external materialized view should return some rows.");
                }
 
            String currentAmount = rs2.getString(1);
                rs2.close();
              if (!originalAmount.equals(currentAmount)) {
                        throw new Exception("Data in materialized table shouldn't have been updated yet.");
                }
System.out.println("start executed loadmatview");

           Statement st2 = connection.createStatement();
                st2.execute("exec SYSADMIN.loadMatView(schemaName=>'PersonMatModel',viewname=>'PersonMatView', invalidate=>'true')");
System.out.println("ended executed loadmatview");
           st2.close();
            Thread.sleep(25000);

            ResultSet rs3 = statement.executeQuery("SELECT name FROM PersonMatModel.PersonMatView WHERE id=" + accountid);
            assertTrue(rs3.next(), "SELECT statement on external materialized view should return some rows.");
            String updatedAmount = rs3.getString(1);
            rs3.close();

 	    System.out.println("ended externalLongTtlUpdateTest");
	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
statement.close();
        }
    }

  
    public void externalLongTtlExplicitLoadTest(Connection connection) throws Exception {

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT name FROM PersonMatModel.PersonMatView WHERE id=20000019;");

           boolean n = rs.next();
            if (! n) {
                throw new Exception("SELECT statement on external materialized view should return some rows.");
                }
 
            String originalAmount = rs.getString(1);
            statement.execute("UPDATE ACCOUNTS.ACCOUNT SET account_id=92222222 WHERE SSN='CST01019'");

            ResultSet rs2 = statement.executeQuery("SELECT name FROM PersonMatModel.PersonMatView WHERE id=20000019");

               n = rs2.next();
                if (! n) {
                        throw new Exception("SELECT statement on external materialized view should return some rows.");
                }

            String currentAmount = rs2.getString(1);

             if (!originalAmount.equals(currentAmount)) {
                        throw new Exception("Data in materialized table shouldn't have been updated yet.");
                }

            statement
                .execute("exec SYSADMIN.loadMatView(schemaName=>'View',viewname=>'external_long_ttl', invalidate=>'true')");

            ResultSet rs3 = statement.executeQuery("SELECT name FROM PersonMatModel.PersonMatView WHERE id=92222222");

               n = rs3.next();
                if (! n) {
                        throw new Exception("SELECT statement on external materialized view should return some rows.");
                }

             String updatedAmount = rs3.getString(1);

            if (originalAmount.equals(updatedAmount)) {
                        throw new Exception( "Data in materialized table should have been updated already(original:" + originalAmount + ",updated:"
                        + updatedAmount + ").");
                }


  //          statement.execute("UPDATE dv_matviews_orders SET amount=amount+50 WHERE customer_id=1");

   //         statement
  //              .execute("exec SYSADMIN.loadMatView(schemaName=>'View',viewname=>'external_long_ttl', invalidate=>'true')");

  //          ResultSet rs4 = statement.executeQuery("SELECT totalAmount FROM external_long_ttl WHERE customerId=1");
  //            n = rs4.next();
   //             if (! n) {
   //                     throw new Exception("SELECT statement on external materialized view should return some rows.");
   //             }

   //          int updatedAmount2 = rs4.getInt(1);
//
   //        if (updatedAmount >= updatedAmount2) {
    //                    throw new Exception("Data in materialized table should have been updated already.");
     //           }
 
	    statement.close();
	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
		if (connection != null) {
			connection.close();
		}
	}
    }

        
        private void insertCustomer(Connection connection, int starting, int ending) throws Exception {

Statement statement = null;

        int cnt = starting;
            while(cnt <= ending) {

try {
           statement = connection.createStatement();
           statement.execute(
"INSERT INTO CUSTOMER (SSN,FIRSTNAME,LASTNAME,ST_ADDRESS,APT_NUMBER,CITY,STATE,ZIPCODE,PHONE) VALUES ('CST" + cnt + "','Joseph','Smith','1234 Main Street','Apartment 56','New York','New York','10174','(646)555-1776')" );

statement.close();
statement = null;
                insertAccount(connection, cnt+5000, cnt);

	} finally {
		if (statement != null) {
			statement.close();
		}
	}
                ++cnt;
        
                 }


        }

      private void insertAccount(Connection connection, int account, int customer) throws Exception {


Statement statement = null;

try {
           statement = connection.createStatement();
           statement.execute("INSERT INTO ACCOUNT (ACCOUNT_ID,SSN,STATUS,TYPE,DATEOPENED,DATECLOSED) VALUES (" + account + ",'CST" + customer + "','Personal  ','Active    ', '1998-02-01 00:00:00.000', NULL);");

        statement.close();
    statement = null;

	} finally {
		if (statement != null) {
			statement.close();
		}
	}



        }

    private void assertTrue(boolean assertWhat, String message) throws Exception {
        if (!assertWhat) {
                throw new Exception(message);
        }
    }

    private void assertFalse(boolean assertWhat, String message) throws Exception {
        if (assertWhat) {
                throw new Exception(message);
        }
    }


}
