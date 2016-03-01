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
package org.teiid.reverseeng.pojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.teiid.core.util.FileUtils;
import org.teiid.core.util.StringUtil;

/**
 * Reference for dynamically compiling and assembly
 * https://github.com/Teiid-Designer/teiid-designer/blob/
 * 2639eaed21353797fd9b2eb5b57ab9bee2f388cc/plugins/org.teiid.designer.dqp.ui/src/org/teiid/designer/runtime/ui/wizards/webservices/util/DefaultWebArchiveBuilderImpl.java#L582
 * 
 * @author vanhalbert
 *
 */
public class DynamicCompilation {

	private Map<File, String> files = new HashMap<File, String>();

	public void addFile(File f, String classname) {
		files.put(f, classname);
	}

	public void compile(File loc, String pathLoc, String pojoJarName)
			throws Exception {

		t(loc);

		File[] files = FileUtils.findAllFilesInDirectoryHavingExtension(
				loc.getCanonicalPath(), ".class");

		File archive = new File(pojoJarName);
		createJarArchive(archive, files, pathLoc);

	}

	private void t(File path) throws Exception {

		// Compile classes
		JavaCompiler compilerTool = ToolProvider.getSystemJavaCompiler();
		if (compilerTool != null) {
			StandardJavaFileManager fileManager = compilerTool
					.getStandardFileManager(null, null, null);

			String strClassPath = System.getProperty("java.class.path");
			List<String> tokens = StringUtil.getTokens(strClassPath,
					File.pathSeparator);

			//	        String pathToPojoJar = path.getCanonicalPath() + File.separator + jarName; //$NON-NLS-1$
			List<File> classPaths = new ArrayList<File>();

			for (String t : tokens) {
				File pojoJar = new File(t);
				if (pojoJar.getName().equals("rt.jar")
						|| pojoJar.getName().contains("protostream")
						|| pojoJar.getName().contains("hibernate")) {
					classPaths.add(pojoJar);
				}
			}

			// StandardLocation.CLASS_PATH, classPaths);

			// prepare the source files to compile
			List<File> sourceFileList = new ArrayList<File>();
			for (File f : files.keySet()) {
				sourceFileList.add(f);

				System.out
						.println("*** Compiling file: " + f.getAbsolutePath());
			}

			Iterable<? extends JavaFileObject> compilationUnits = fileManager
					.getJavaFileObjectsFromFiles(sourceFileList);
			/*
			 * Create a diagnostic controller, which holds the compilation
			 * problems
			 */
			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
			CompilationTask task = compilerTool.getTask(null, fileManager,
					diagnostics, null, null, compilationUnits);
			task.call();
			List<Diagnostic<? extends JavaFileObject>> diagnosticList = diagnostics
					.getDiagnostics();
			for (Diagnostic<? extends JavaFileObject> diagnostic : diagnosticList) {
				diagnostic.getKind();
				if (diagnostic.getKind().equals(Kind.ERROR)) {
					throw new Exception(diagnostic.getMessage(null));
				}
			}

			fileManager.close();

		}
	}

	public static int BUFFER_SIZE = 10240;

	protected void createJarArchive(File archiveFile, File[] tobeJared,
			String pathLoc) {
		try {
			byte buffer[] = new byte[BUFFER_SIZE];
			// Open archive file
			FileOutputStream stream = new FileOutputStream(archiveFile);
			JarOutputStream out = new JarOutputStream(stream, new Manifest());

			for (int i = 0; i < tobeJared.length; i++) {
				if (tobeJared[i] == null || !tobeJared[i].exists()
						|| tobeJared[i].isDirectory())
					continue; // Just in case...

				// Add archive entry
				String fname = pathLoc + tobeJared[i].getName();
				JarEntry jarAdd = new JarEntry(fname);

				System.out.println("*** Added file to jar: " + fname);

				jarAdd.setTime(tobeJared[i].lastModified());
				out.putNextEntry(jarAdd);

				// Write file to archive
				FileInputStream in = new FileInputStream(tobeJared[i]);
				while (true) {
					int nRead = in.read(buffer, 0, buffer.length);
					if (nRead <= 0)
						break;
					out.write(buffer, 0, nRead);
				}
				in.close();
			}

			out.close();
			stream.close();
			System.out.println("Created JAR " + archiveFile.getAbsolutePath());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error: " + ex.getMessage());
		}
	}

}
