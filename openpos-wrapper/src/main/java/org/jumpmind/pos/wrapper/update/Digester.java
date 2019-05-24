package org.jumpmind.pos.wrapper.update;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

final public class Digester {

    private final static Logger log = Logger.getLogger(Digester.class.getName());

    protected static final String XLATE = "0123456789abcdef";
    protected static final int DIGEST_BUFFER_SIZE = 5 * 1025;

    private Digester() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            File appDir = new File(args[0]);
            if (appDir.exists() && appDir.isDirectory()) {
                File digestFile = new File(appDir, "download.txt");
                digestFile.delete();
                digestFile.getParentFile().mkdirs();
                digestFile.createNewFile();
                processDir(digestFile, appDir);
            }
        }
    }

    protected static void processDir(File digestFile, File dir) throws Exception {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                processDir(digestFile, file);
            } else {
                String digest = computeDigest(file);
                String relativeFilePath = file.getPath().substring(digestFile.getParentFile().getPath().length());
                Files.write(Paths.get(digestFile.getPath()), (relativeFilePath + " = " + digest + "\n").getBytes(), StandardOpenOption.APPEND);
            }
        }
    }

    public static String computeDigest(File target) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.reset();
        byte[] buffer = new byte[DIGEST_BUFFER_SIZE];
        int read;

        boolean isJar = isJar(target.getPath());
        boolean isPacked200Jar = isPacked200Jar(target.getPath());

        // if this is a jar, we need to compute the digest in a "timestamp and
        // file order" agnostic
        // manner to properly correlate jardiff patched jars with their
        // unpatched originals
        if (isJar || isPacked200Jar) {
            File tmpJarFile = null;
            JarFile jar = null;
            try {
                // if this is a compressed jar file, uncompress it to compute
                // the jar file digest
                if (isPacked200Jar) {
                    tmpJarFile = new File(target.getPath() + ".tmp");
                    unpackPacked200Jar(target, tmpJarFile);
                    jar = new JarFile(tmpJarFile);
                } else {
                    jar = new JarFile(target);
                }

                List<JarEntry> entries = Collections.list(jar.entries());
                Collections.sort(entries, ENTRY_COMP);
                for (JarEntry entry : entries) {
                    try (InputStream in = jar.getInputStream(entry)) {
                        while ((read = in.read(buffer)) != -1) {
                            md.update(buffer, 0, read);
                        }
                    }
                }

            } finally {
                if (jar != null) {
                    try {
                        jar.close();
                    } catch (IOException ioe) {
                        log.log(Level.WARNING, "Error closing jar " + jar.getName(), ioe);
                    }
                }
                if (tmpJarFile != null) {
                    deleteHarder(tmpJarFile);
                }
            }

        } else {
            try (FileInputStream fin = new FileInputStream(target)) {
                while ((read = fin.read(buffer)) != -1) {
                    md.update(buffer, 0, read);
                }
            }
        }
        return hexlate(md.digest());
    }
    
    protected static void unpackPacked200Jar (File packedJar, File target) throws IOException
    {
        InputStream packedJarIn = null;
        FileOutputStream extractedJarFileOut = null;
        JarOutputStream jarOutputStream = null;
        try {
            extractedJarFileOut = new FileOutputStream(target);
            jarOutputStream = new JarOutputStream(extractedJarFileOut);
            packedJarIn = new FileInputStream(packedJar);
            if (packedJar.getName().endsWith(".gz") || packedJar.getName().endsWith(".gz_new")) {
                packedJarIn = new GZIPInputStream(packedJarIn);
            }
            Pack200.Unpacker unpacker = Pack200.newUnpacker();
            unpacker.unpack(packedJarIn, jarOutputStream);

        } finally {
            close(jarOutputStream);
            close(extractedJarFileOut);
            close(packedJarIn);
        }
    }
    
    protected static boolean deleteHarder (File file) {
        // if at first you don't succeed... try, try again
        return file.delete() || file.delete();
    }
    
    protected static void close (Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    protected static boolean isJar(String path) {
        return path.endsWith(".jar") || path.endsWith(".jar_new");
    }

    protected static boolean isPacked200Jar(String path) {
        return path.endsWith(".jar.pack") || path.endsWith(".jar.pack_new") || path.endsWith(".jar.pack.gz")
                || path.endsWith(".jar.pack.gz_new");
    }

    public static String hexlate(byte[] bytes, int count) {
        if (bytes == null) {
            return "";
        }

        count = Math.min(count, bytes.length);
        char[] chars = new char[count * 2];

        for (int i = 0; i < count; i++) {
            int val = bytes[i];
            if (val < 0) {
                val += 256;
            }
            chars[2 * i] = XLATE.charAt(val / 16);
            chars[2 * i + 1] = XLATE.charAt(val % 16);
        }

        return new String(chars);
    }

    public static String hexlate(byte[] bytes) {
        return (bytes == null) ? "" : hexlate(bytes, bytes.length);
    }

    protected static final Comparator<JarEntry> ENTRY_COMP = new Comparator<JarEntry>() {
        @Override
        public int compare(JarEntry e1, JarEntry e2) {
            return e1.getName().compareTo(e2.getName());
        }
    };
}
