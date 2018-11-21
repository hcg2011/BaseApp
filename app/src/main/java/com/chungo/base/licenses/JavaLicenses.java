package com.chungo.base.licenses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

/**
 * 统一添加Licenses信息
 */
public class JavaLicenses {

    public static final String LICENSE_PATH = "app/license.txt";
    public static final String LICENSE_TARGETS = "app/src/main/java/com/chugno/base/test";
    static String licenseStr = "";

    public static void main(String[] args) {

        try {
            File license = new File(LICENSE_PATH);
            readLicenses(license);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        File f = new File(LICENSE_TARGETS);
        System.out.println(f.getAbsolutePath());
        print(f);
    }

    private static void readLicenses(File license) throws IOException {
        InputStreamReader read = new InputStreamReader(new FileInputStream(license), "utf-8");
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = "";
        while ((lineTxt = bufferedReader.readLine()) != null) {
            licenseStr += lineTxt + "\n";
        }
        read.close();
    }

    public static void print(File f) {

        if (f != null) {
            if (f.isDirectory()) {
                File[] fileArray = f.listFiles();
                if (fileArray != null) {
                    for (int i = 0; i < fileArray.length; i++) {
                        File file = fileArray[i];

                        if (file.isDirectory()) {
                            print(file);
                        } else {
                            addLicense(0, licenseStr, file);
                        }
                    }
                }
            } else {
                addLicense(0, licenseStr, f);
            }
        }
    }

    public static void addLicense(long skip, String str, File file) {
        try {

            if (removeOldLicenses(file))
                return;
            if (writeLicense(skip, str, file))
                return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean writeLicense(long skip, String str, File file) throws IOException {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            if (skip < 0 || skip > raf.length()) {
                System.out.println("skip error");
                return false;
            }
            byte[] b = str.getBytes();
            raf.setLength(raf.length() + b.length);
            for (long i = raf.length() - 1; i > b.length + skip - 1; i--) {
                raf.seek(i - b.length);
                byte temp = raf.readByte();
                raf.seek(i);
                raf.writeByte(temp);
            }
            raf.seek(skip);
            raf.write(b);
            raf.close();
            raf = null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (raf == null)
                return true;
            raf.close();
            return false;
        }
    }

    /**
     * 这里还有点问题
     *
     * @param file
     * @return
     * @throws IOException
     */
    private static boolean removeOldLicenses(File file) throws IOException {
        String lineTxt = "";
        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            lineTxt = "";
            while ((lineTxt = bufferedReader.readLine()) != null) {
                if (!lineTxt.startsWith("/*") || lineTxt.startsWith("package"))
                    break;

                licenseStr += lineTxt + "\n";
            }
            read.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}