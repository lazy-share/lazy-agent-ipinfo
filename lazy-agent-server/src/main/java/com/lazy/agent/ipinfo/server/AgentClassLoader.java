package com.lazy.agent.ipinfo.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AgentClassLoader {


    public static void loadClass(Instrumentation instrumentation) {

        try {
            String libPathPre = "libs/";
            String agentJarFilePath = AgentClassLoader.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String agentJarDirPath = agentJarFilePath.substring(0, agentJarFilePath.lastIndexOf("/"));
            String libsLoadTempDir = agentJarDirPath + "/_agentTempLibs/";
            File libsLoadTempDirOfFile = new File(libsLoadTempDir);
            if (libsLoadTempDirOfFile.exists()) {
                libsLoadTempDirOfFile.delete();
            }
            libsLoadTempDirOfFile.mkdir();
            JarFile jf = new JarFile(agentJarFilePath);
            JarEntry je;
            File f;
            InputStream in = null;
            FileOutputStream fout = null;
            BufferedOutputStream out = null;
            byte[] buffer;
            List<String> libsPaths = new ArrayList<>();
            for (Enumeration<JarEntry> e = jf.entries(); e.hasMoreElements(); ) {
                je = e.nextElement();
                if (je.getName().startsWith(libPathPre) && je.getName().endsWith(".jar")) {
                    String outFileName = libsLoadTempDir + je.getName().replaceFirst(libPathPre, "");
                    f = new File(outFileName);
                    try {
                        in = jf.getInputStream(je);
                        fout = new FileOutputStream(f);
                        out = new BufferedOutputStream(fout);
                        buffer = new byte[2048];
                        int nBytes = 0;
                        while ((nBytes = in.read(buffer)) > 0) {
                            out.write(buffer, 0, nBytes);
                        }
                        libsPaths.add(outFileName);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    } finally {
                        if (null != out) {
                            out.flush();
                            out.close();
                            out = null;
                        }
                        if (null != fout) {
                            fout.flush();
                            fout.close();
                            fout = null;
                        }
                        if (null != in) {
                            in.close();
                            in = null;
                        }
                    }
                }
            }
            for (String libsPath : libsPaths) {
                instrumentation.appendToSystemClassLoaderSearch(new JarFile(libsPath));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
