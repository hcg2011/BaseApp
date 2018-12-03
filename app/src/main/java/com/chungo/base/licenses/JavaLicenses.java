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
    public static final String INDEX_START = "/*";
    public static final String INDEX_CONTENT = " *";
    public static final String INDEX_END = "*/";
    public static final String INDEX_CLASS = "package";
    public static final String INDEX_IMPORT = "import";
    static String licenseStr = "";

    private static final String LICENSE_PATH = "G:/workSpace/BaseApp/app/src/main/java/com/chungo/base/licenses/license.txt";
    private static final String LICENSE_TARGETS = "G:\\workSpace\\BaseApp\\app\\src\\main\\java\\com\\chungo\\base\\test";


    public static void main(String[] args) {

        try {
            File license = new File(LICENSE_PATH);
            readLicenses(licenseStr, license);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        File f = new File(LICENSE_TARGETS);
        System.out.println(f.getAbsolutePath());
        performAddLicenses(f);
    }

    private static void readLicenses(String licenseStr, File license) throws IOException {
        InputStreamReader read = new InputStreamReader(new FileInputStream(license), "utf-8");
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = "";
        while ((lineTxt = bufferedReader.readLine()) != null) {
            licenseStr += lineTxt + "\n";
        }
        read.close();
    }

    public static void performAddLicenses(File f) {

        if (f != null) {
            if (f.isDirectory()) {
                File[] fileArray = f.listFiles();
                if (fileArray != null) {
                    for (int i = 0; i < fileArray.length; i++) {
                        File file = fileArray[i];
                        if (file.isDirectory()) {
                            performAddLicenses(file);
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
            if (cheackLicenses(file))
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
     * 批量删除license
     *
     * @param f
     */
    public static void performRemoveLicenses(File f) {

        if (f != null) {
            if (f.isDirectory()) {
                File[] fileArray = f.listFiles();
                if (fileArray != null) {
                    for (int i = 0; i < fileArray.length; i++) {
                        File file = fileArray[i];

                        if (file.isDirectory()) {
                            performAddLicenses(file);
                        } else {
                            try {
                                removeLicense(file);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else {
                try {
                    removeLicense(f);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean removeLicense(File file) throws Exception {
        RandomAccessFile raf;
        RandomAccessFile temp;
        String license = obtainLicenses(file);
        if (license == null || license.length() <= 0)
            return false;
        File cacheFile = new File(file.getAbsolutePath() + ".temp");
        raf = new RandomAccessFile(file, "rw");//整个文件
        temp = new RandomAccessFile(cacheFile, "rw");//整个文件

        byte[] b = license.getBytes();//获得文件的大小
        temp.setLength(raf.length() - b.length);

        for (long i = b.length; i < raf.length(); i++) {//获得内容
            raf.seek(i);
            byte cache = raf.readByte();
            temp.writeByte(cache);
        }
        raf.close();
        temp.close();
        file.delete();
        cacheFile.renameTo(file);
        return true;
    }

    private static String obtainLicenses(File file) throws IOException {
        String licenses = "";
        String lineTxt = "";
        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            while ((lineTxt = bufferedReader.readLine()) != null) {
                if (lineTxt.startsWith(INDEX_CLASS)
                        || lineTxt.startsWith(INDEX_IMPORT))
                    break;
                if (lineTxt.startsWith(INDEX_START)
                        || lineTxt.startsWith(INDEX_CONTENT)
                        || lineTxt.startsWith(INDEX_END))
                    licenses += lineTxt + "\n";
            }
            read.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return licenses;
    }

    /**
     * @param file
     * @return
     * @throws IOException
     */
    private static boolean cheackLicenses(File file) throws IOException {
        String lineTxt = "";
        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            while ((lineTxt = bufferedReader.readLine()) != null) {
                if (lineTxt.startsWith("/*"))
                    return true;
                else
                    break;
            }
            read.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}