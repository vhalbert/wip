import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Properties;
import java.net.URLEncoder;

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
	
	public static String DEFAULT_HOST = "localhost";
	public static int DEFAULT_PORT = 9999;
	public static int DEAULT_WAIT_TIME = 6000;  // 3 seconds
	public static int DEFAULT_RETRIES = 10;
	
	public static void main(String[] args) throws Exception {
		boolean failure = QS_UTIL.perform(args);
		
		if (failure) {
			System.exit(-1);
		}
	}
	
	public static boolean perform(String[] args) throws Exception {
		boolean action1 = false;
		boolean action2 = false;
		boolean action3 = false;
		boolean action4 = false;
		boolean error = false;
		if (args.length == 0) { 
			error = true;
		} else {

			if (args[0].equals("1") ) {
				action1 = true;
				if (args.length != 3 ) {
					error = true;
				} 
			} else if (args[0].equals("2") ) {
				action2 = true;
				if ( args.length > 3 ) {
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
				System.out.println("where: action = 1 is to backup the current xml file");
				System.out.println("                QS_UTIL 1 <xmlinputfile> <installpath> " );
			}
			if (action2) {
				System.out.println("where: action = 2 is to ping the server to determine if its up and running");
				System.out.println("                QS_UTIL 2 [hostname:localhost[port:31000]]" );
			}
			if (action3) {
				System.out.println("where: action = 3 is to pause for an amount of time");
				System.out.println("                QS_UTIL 3 <seconds>" );
			}
			if (action4) {
				System.out.println("where: action = 4 is to download file from internet");
				System.out.println("                QS_UTIL 4 <url> <targetFile>" );
			}			
			System.out.println(action1 + " Args: [" + x + "]" + msg);
			return error;
			
		}
		
		if (action1) {
			String readFrom = null;
			String xmlfile = args[1];
			String backupfile = xmlfile + ".bak";

			File bkf = new File(backupfile);
			if (bkf.exists()) {
				System.out.println("No backup done, file exists " + backupfile );
				readFrom = backupfile;
			} else {
				System.out.println("Backup file " + xmlfile + " to " +  backupfile);
				copy(xmlfile, backupfile, true);
				readFrom = xmlfile;
			}
			
			StringBuilder sb = readFile(readFrom);
			
			StringBuilder newsb = replace(sb,SEARCH_VAR, args[2] );
			write(newsb.toString(), xmlfile);
		} else if (action2) {
			String host = DEFAULT_HOST;
			int port = DEFAULT_PORT;
//			boolean isAlive = isAlive(host, port);
			if (args.length == 2) {
				host = args[1];
			}
			if (args.length == 3) {
				port = Integer.valueOf(args[2]);
			}
			boolean isAlive = isAlive(host, port);
			if (!isAlive) {
				System.out.println("Unable to detect the [host:port] " + host + ":" + port );
				return true;
			}
			Thread.sleep(DEAULT_WAIT_TIME);
		} else if (action3) {
			pauseForTime(args[1]);
		} else if (action4) {
			downloadFile(args[1], args[2]);
		}
		
		return error;
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
                return source.replace(start, start + search.length(), replace);
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
  
	private static boolean isAlive(String hostname, int port) throws Exception {
		int cnt = 0;
		System.out.println("Ping host:port " + hostname + ":" + port);
		while (cnt < DEFAULT_RETRIES) {
			System.out.println("ping [#" + cnt + "] ...");

			boolean reachable = isReachableByPing(hostname, port);
			if (reachable) return true;

			Thread.sleep(DEAULT_WAIT_TIME);
			cnt++;

		}
		System.out.println("Could not ping host:port " + hostname + ":" + port);
		return false;
	}
	
	public static boolean isReachableByPing(String host, int port) {
	    try{
	    	// nc -zv <hostname/ip> <port/port range>
	            String cmd = "";
	            if(System.getProperty("os.name").startsWith("Windows")) {   
	                    // For Windows
	                    cmd = "nc -zv " + host + " " + port;
	            } else {
	                    // For Linux and OSX
	            	cmd = "nc -zv " + host + " " + port;
	            }

	            Process myProcess = Runtime.getRuntime().exec(cmd);
	            myProcess.waitFor();

	            if(myProcess.exitValue() == 0) {

	                    return true;
	            } 

	             return false;
	            

	    } catch( Exception e ) {

	            e.printStackTrace();
	            return false;
	    }
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


}
