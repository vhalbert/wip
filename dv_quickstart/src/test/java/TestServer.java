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

import java.net.ServerSocket;
import java.net.Socket;

/**
 */
@SuppressWarnings("nls")
public class TestServer {
	protected static int PORT = 11311;
	protected static final int TIMEOUT = 0;
	
	protected static ServerSocket SERVER;
	protected static Socket CLIENT;
	
	protected static Executor EXEC;


	public static synchronized void createServer(int port) throws Exception {
//		String hostAddress = hostAddress();
//		String hostPort = Integer.toString(hostPort());
//		String timeoutStr = Integer.toString(TIMEOUT);
		PORT = port;

		EXEC = new Executor(port);
		
		Thread t1 = new Thread(EXEC);
	    t1.start();
		

	}
	
	public static synchronized void stopServer() throws Exception {
		try {
			SERVER.close();
		} catch (Exception e) {
			
		}
	}

	public static int hostPort() {
		return PORT;
	}

	public static String hostName() {
		return "LOCALHOST";
	}

//	public static String hostAddress() {
//		try {
//			return InetAddress.getLocalHost().getHostAddress();
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//	}

	public static class Executor implements Runnable {
		private int exec_port;
		public Executor(int port) {
			exec_port = port;
		}
		/**
		 * {@inheritDoc}
		 *
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
			SERVER = new ServerSocket(exec_port);
			//accepting the client request on the given port(2223)
			CLIENT = SERVER.accept();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
