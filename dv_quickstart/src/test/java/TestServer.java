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

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 */
@SuppressWarnings("nls")
public class TestServer {
	protected static int PORT = 11311;
	protected static String IPADDRESS;
	protected static final int TIMEOUT = 0;
	
	protected static ServerSocket SERVER;
	protected static Socket CLIENT;
	
	protected static Executor EXEC;


	public static synchronized void createServer(int port) throws Exception {
		PORT = port;

		EXEC = new Executor(port);
		
		Thread t1 = new Thread(EXEC);
	    t1.start();

	}
	
	public static synchronized void stopServer() throws Exception {
		try {
			EXEC.stopRunning();

		} catch (Exception e) {
			
		}
	}

	public static int hostPort() {
		return PORT;
	}

	public static String hostName() {
		try {
			IPADDRESS = InetAddress.getByName("localhost").getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return IPADDRESS;
//		return "LOCALHOST";
	}

//	public static String hostAddress() {
//		try {
//			return InetAddress.getLocalHost().getHostAddress();
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//	}

	public static class Executor extends Thread {
		private int exec_port;
		private boolean running = false;
		public Executor(int port) {
			exec_port = port;
			running = true;
		}
		
		public void stopRunning() {
			running = false;
		}
		/**
		 *
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				SERVER = new ServerSocket(exec_port, 5, InetAddress.getByName("localhost"));
			} catch (Throwable e) {
				// if port already used, then stop running this instance
				running = false;
				return;				
			}

			while (running) {
				try {
					CLIENT = SERVER.accept();
				} catch (Throwable e) {
					// if port already used, then stop running this instance
					running = false;
				}
			}
			
			try {
				CLIENT.close();
			} catch (IOException e) {
			}
			
			try {
				SERVER.close();
			} catch (IOException e) {
			}
		}
		
	}
	
}
