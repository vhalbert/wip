import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import java.sql.Connection;
import java.sql.DriverManager;

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


@SuppressWarnings("nls")
public class QS_UTIL {
	
	public static final String INSTALL_PATH = "INSTALL_PATH";
	public static final String SEARCH_VAR = "${install.path}";
	
	public static int DEFAULT_BUFFER_SIZE = 2048;
	
	public static String DEFAULT_HOST = hostAddress();
	public static int DEFAULT_PORT = 9990;
	public static int DEAULT_WAIT_TIME = 6000;  // 3 seconds
	public static int DEFAULT_RETRIES = 10;
	
	public static int HOSTPORT_NOT_FOUND = -1;
	public static int HOSTPORT_FOUND = 0;
	
	public static void main(String[] args) throws Exception {
         	int status = QS_UTIL.perform(args);
			System.out.println("QS_UTIL exit status: " + status );

        	System.exit(status);
 	}
	
	public static int perform(String[] args) throws Exception {
		boolean action1 = false;
		boolean action2 = false;
		boolean action3 = false;
		boolean action4 = false;
		boolean action5 = false;
		int rtn = -1;
		boolean error = false;
		if (args.length == 0) { 
			error = true;
		} else {

			if (args[0].equals("1") ) {
				action1 = true;
				if (args.length < 4 || args.length > 5 ) {
					error = true;
				} 
			} else if (args[0].equals("2") ) {
				action2 = true;
				if ( args.length < 2 || args.length > 4 ) {
					error = true;
				} 
				
			} else if (args[0].equals("3")) {
				action3 = true;
				if ( args.length > 2 ) {
					error = true;
				} 				
			} else if (args[0].equals("4")) {
				action4 = true;
				if ( args.length > 3 ) {
					error = true;
				} 	
			} else if (args[0].equals("5")) {
				action4 = true;
				if ( args.length > 3 ) {
					error = true;
				} 	
			} else if (args[0].equals("5")) {
				action4 = true;
				if ( args.length > 3 ) {
					error = true;
				} 					
			} else {
				error = true;
			}
		}
		
		if (error) {
			int x = args.length;
			String msg = "";
			if (x > 0) {
				for (int i = 0; i < x; i++) {
					msg = msg + " " + args[i];
				}
			}

			
			
			System.out.println("usage: QS_UTIL <action[1,2,3,4]> [options] ");
			if (action1) {
				System.out.println("where: action = 1 is to replace value in file");
				System.out.println("                QS_UTIL 1 <inputfile> <searchValue> <replaceValue> <false|true [useforwardslash]> " );
			}
			if (action2) {
				System.out.println("where: action = 2 is to ping the server to determine if its up and running");
				System.out.println("                QS_UTIL 2 <hostname> [port<default 9990>] [true<return immediately if down>] " );
			}
			if (action3) {
				System.out.println("where: action = 3 is to pause for an amount of time");
				System.out.println("                QS_UTIL 3 <seconds>" );
			}
			if (action4) {
				System.out.println("where: action = 4 is to download file from internet");
				System.out.println("                QS_UTIL 4 <url> <targetFile>" );
			}	
			if (action5) {
				System.out.println("where: action = 5 is to perform ping using Teiid JDBC Driver connection");
				System.out.println("                QS_UTIL 5 <jdbcurl> <username> <password> <pingfile>" );
			}
			System.out.println(action1 + " Args: [" + x + "]" + msg);
			throw new Exception("Invalid arguments");
			
		}
		
		if (action1) {
		//	String readFrom = null;
			String readFrom = args[1];
			String backupfile = readFrom + ".bak";

			File bkf = new File(backupfile);
			if (bkf.exists()) {
				System.out.println("No backup done, file exists " + backupfile );
		//		readFrom = backupfile;
			} else {
				System.out.println("Backup file " + readFrom + " to " +  backupfile);
				copy(readFrom, backupfile, true);
			}
			
			String replaceValue = args[3];
            if (args.length == 5) {
				if (args[4].toLowerCase().equals("true")) {	
					replaceValue =  replaceValue.replace("\\", "/");
				}            	
            }
			
			StringBuilder sb = readFile(readFrom);

			StringBuilder newsb = replaceAll(sb, "${" + args[2] + "}", replaceValue );
			write(newsb.toString(), readFrom);
			return 0;
		} else if (action2) {
			String host = DEFAULT_HOST;
			int port = DEFAULT_PORT;
			if (args.length > 1) {
				if (! args[1].toLowerCase().equals("localhost")) {	
					host = args[1];
				}
			}

			if (args.length > 2) {
				port = Integer.valueOf(args[2]);
			} 
            int retries = DEFAULT_RETRIES;
            if (args.length > 3) {
				if (args[3].toLowerCase().equals("true")) {	
					retries = 0;
				}            	
            }
			boolean isAlive = isAlive(host, port, retries);
			if (!isAlive) {
				System.out.println("Unable to detect the [host:port] " + host + ":" + port );
				return HOSTPORT_NOT_FOUND;
			}
			
			touchFile("hostport_found.txt");
			return HOSTPORT_FOUND;

		} else if (action3) {
			pauseForTime(args[1]);
			return 0;
		} else if (action4) {
			downloadFile(args[1], args[2]);
			return 0;
		} else if (action5) {
			boolean isAlive = testConnection(args[0], args[1], args[2], DEFAULT_RETRIES);
			
			if (!isAlive) {
				return HOSTPORT_NOT_FOUND;
			}
			
			touchFile(args[3]);
			return HOSTPORT_FOUND;

		}
		
		System.out.println("Processing error, no Action determined " );

		return -1;
	}
	
	static StringBuilder readFile(String filename) throws Exception {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
		try {
		  StringBuilder stringBuilder = new StringBuilder();
		  String line = null;
		 
		  while((line =bufferedReader.readLine())!=null){
		 
			  stringBuilder.append(line).append("\n");
		  }		
		
		return stringBuilder;
		
		} finally {
			bufferedReader.close();
		}

	}
	
	static String getInstallPath(Properties props) {
		return (props.getProperty(INSTALL_PATH) != null ? System.getProperties().getProperty(INSTALL_PATH) : "." );
	}
	
	static void setInstallPath(Properties props, String path) {
		props.setProperty(INSTALL_PATH, path);
	}	
    
	/*
	 * Replace a single occurrence of the search string with the replace string
	 * in the source string. If any of the strings is null or the search string
	 * is zero length, the source string is returned.
	 * @param source the source string whose contents will be altered
	 * @param search the string to search for in source
	 * @param replace the string to substitute for search if present
	 * @return source string with the *first* occurrence of the search string
	 * replaced with the replace string
	 */
	private static StringBuilder replace(StringBuilder source, String search, String replace) {
	    if (source != null && search != null && search.length() > 0 && replace != null) {
	        int start = source.indexOf(search);
            if (start > -1) {
                        System.out.println("Replacing " + search + " with " +  replace);
                        return source.replace(start, start + search.length(), replace);
	        } 
            System.out.println("*** WARNING **** Did not find match for  " + search);
             

	    } else {
                System.out.println("*** WARNING **** Did not find match for  " + search);
            }
	    return source;    
	} 
	
	/*
	 * Replace all occurrences of the search string with the replace string
	 * in the source string. If any of the strings is null or the search string
	 * is zero length, the source string is returned.
	 * @param source the source string whose contents will be altered
	 * @param search the string to search for in source
	 * @param replace the string to substitute for search if present
	 * @return source string with *all* occurrences of the search string
	 * replaced with the replace string
	 */
	public static StringBuilder replaceAll(StringBuilder source, String search, String replace) {
	    if (source == null || search == null || search.length() == 0 || replace == null) {
	    	return source;
	    }
        int start = source.indexOf(search);
        if (start > -1) {
	        while (start > -1) {
	            int end = start + search.length();
	            source.replace(start, end, replace);
	            start = source.indexOf(search, start + replace.length());
	        }
        }
	    return source;    
	}
	
    /**
     * Copy a file 
     * @param fromFileName
     * @param toFileName
     * @param overwrite whether to overwrite the destination file if it exists.
     * @throws IOException 
     * @since 4.3
     */
    public static void copy(String fromFileName, String toFileName, boolean overwrite) throws IOException {
        File toFile = new File(toFileName);
        
        if (toFile.exists()) {
            if (overwrite) {
                toFile.delete();
            } else {
                final String msg = "QS_Util.File_already_exists " + toFileName; //$NON-NLS-1$            
                throw new IOException(msg);
            }
        }
        
        File fromFile = new File(fromFileName);
        if (!fromFile.exists()) {
            throw new FileNotFoundException("QS_Util.File_does_not_exist " +  fromFile.getAbsolutePath()); //$NON-NLS-1$
        }
        
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fromFile);
            write(fis, toFileName);    
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
    
    /**
     *  Write an InputStream to a file.
     * @param is 
     * @param fileName 
     * @throws IOException 
     */
     public static void write(InputStream is, String fileName) throws IOException {
         File f = new File(fileName);
         write(is,f);
     }
    
    /**
     *  Write an InputStream to a file.
     * @param is 
     * @param f 
     * @throws IOException 
     */
     public static void write(InputStream is, File f) throws IOException {
 		write(is, f, DEFAULT_BUFFER_SIZE);    	
     }

 	/**
 	 *  Write an InputStream to a file.
 	 * @param is 
 	 * @param f 
 	 * @param bufferSize 
 	 * @throws IOException 
 	 */
 	 public static void write(InputStream is, File f, int bufferSize) throws IOException {
 		f.delete();
 		final File parentDir = f.getParentFile();
 		if (parentDir !=null) {
 			parentDir.mkdirs();
 		}

 		FileOutputStream fio = null;
 		BufferedOutputStream bos = null;
         try {
             fio = new FileOutputStream(f);
             bos = new BufferedOutputStream(fio);
             if (bufferSize > 0) {
         		byte[] buff = new byte[bufferSize];
         		int bytesRead;
         
         		// Simple read/write loop.
         		while(-1 != (bytesRead = is.read(buff, 0, buff.length))) {
         			bos.write(buff, 0, bytesRead);
         		}
             }
             bos.flush();
         } finally {
             if (bos != null) {
                 bos.close();
             }
             if (fio != null) {
                 fio.close();
             }
         }
 	 }
           
  private static void write(String value, String fileName) throws Exception {
	  BufferedWriter writer = null;
	  try
	  {
	      writer = new BufferedWriter( new FileWriter( fileName));
	      writer.write( value);

	  }
	  catch ( IOException e)
	  {
	  }
	  finally
	  {
	      try
	      {
	          if ( writer != null)
	          writer.close( );
	      }
	      catch ( IOException e)
	      {
	      }
	  }
  }
  	private static boolean testConnection(String url, String username, String password, int retries) throws Exception {
		int cnt = 0;
		Class.forName("org.teiid.jdbc.TeiidDriver");
	
		Connection conn = null;
		while (cnt <= retries) {
			System.out.println("Ping url" + url);
	  		try {
	  			
	  			conn = DriverManager.getConnection(url, username, password);
	  			return true;
	  		} catch (Exception e) {
	  			
	  		} finally {
	  			try {
	  				if (conn != null) {
	  					conn.close();
	  				}
	  			} catch (Exception e) {
	  				
	  			}
	  		}
	  		
            cnt++;
            // in cases where retries is set to 0 dont sleep
            if (cnt > retries) break;

			Thread.sleep(DEAULT_WAIT_TIME);
			
		}
		System.out.println("Could not ping url " + url);
		return false;

  	}
  
	private static boolean isAlive(String hostname, int port, int retries) throws Exception {
		int cnt = 0;
		System.out.println("Ping host:port " + hostname + ":" + port);
		while (cnt <= retries) {
			System.out.println("ping [#" + cnt + "] ...");

			boolean reachable = isReachableByPing(hostname, port);
		//	boolean reachable = isReachable(InetAddress.getByName(hostname).getHostAddress());
			if (reachable) return true;

            cnt++;
            // in cases where retries is set to 0 dont sleep
            if (cnt > retries) break;

			Thread.sleep(DEAULT_WAIT_TIME);
			

		}
		System.out.println("Could not ping host:port " + hostname + ":" + port);
		return false;
	}
	
	 public static boolean isReachable(String internetProtocolAddress) throws IOException
	    {
	        List<String> command = new ArrayList<String>();
	        command.add("ping");

            if(System.getProperty("os.name").startsWith("Windows"))   

	        {
	            command.add("-n");
	        } else
	        {
	            command.add("-c");
	        }

	        command.add("1");
	        command.add(internetProtocolAddress);

	        ProcessBuilder processBuilder = new ProcessBuilder(command);
	        Process process = processBuilder.start();

	        BufferedReader standardOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));

	        String outputLine;

	        while ((outputLine = standardOutput.readLine()) != null)
	        {
	            // Picks up Windows and Unix unreachable hosts
	            if (outputLine.toLowerCase().contains("destination host unreachable"))
	            {
	                return false;
	            }
	        }

	        return true;
	    }	
	public static boolean isReachableByPing(String host, int port) {
		Socket ignored = null;
		    try {
		    	ignored = new Socket(host, port);
		        return true;
		    } catch (IOException e) {
		        return false;
		    } finally {
                        if (ignored != null) {
                                try {
                                        ignored.close();
                                } catch(IOException ioe) {
                                }

                        }
                    }

		
//		try{
//	    	// nc -zv <hostname/ip> <port/port range>
//	            String cmd = "";
//	            if(System.getProperty("os.name").startsWith("Windows")) {   
//	                    // For Windows
//	                    cmd = "nc -zv " + host + " " + port;
//	            } else {
//	                    // For Linux and OSX
//	            	cmd = "nc -zv " + host + " " + port;
//	            }
//
//	            Process myProcess = Runtime.getRuntime().exec(cmd);
//	            myProcess.waitFor();
//
//	            if(myProcess.exitValue() == 0) {
//
//	                    return true;
//	            } 
//
//	             return false;
//	            
//
//	    } catch( Exception e ) {
//
//	            e.printStackTrace();
//	            return false;
//	    }
	}	

     
	private static void pauseForTime(String pauseTime) throws Exception {
		System.out.println("pausing for : " + pauseTime + " seconds");
			
		Thread.sleep( Integer.valueOf(pauseTime) * 1000);
	}
	
	private static void downloadFile(String source, String target) throws Exception {
		System.out.println("Starting download of " + source);

		try {
			FileOutputStream fos = new FileOutputStream(new File(target));

			HttpURLConnection connection = (HttpURLConnection) new URL(source).openConnection();
			
			BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
					//new URL(source).openStream());
		    byte data[] = new byte[1024];
		    int count;
		    while((count = in.read(data,0,1024)) != -1)
		    {
		        fos.write(data, 0, count);
		        
		        System.out.print("\b\b\b\b\b\b\b..."+count);
		    }
		    
		    fos.flush();
		    fos.close();
		    
			System.out.println("\rCompleted download to " + target);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
    public static String httpURLEncode(String s) throws Exception {
        try {
            return URLEncoder.encode(s, "UTF-8");
            //.replaceAll("\\+", "%20"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        } catch (UnsupportedEncodingException e) {
        	throw new Exception(e);
        }
    }
	public static String hostAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void touchFile(String filename)  {
	       File newfile = new File(filename);
	        if (newfile.exists()) {
	            return;
	        }
	        try {
				newfile.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
	}
}

 
