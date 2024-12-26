package org.example;

import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception{
        InputStream ins  = new FileInputStream(new File("/Users/kenny/IdeaProjects/example/src/main/resources/" +
                "小猫钓鱼.txt"));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins));
//        bufferedReader.lines().forEach(string -> {
//            String trim = getString(string);
//            if(trim.isEmpty()) {
//                return;
//            }
//            System.out.println(trim.replace("。","。\n").replace("；", "；\n").replace("？", "？\n"));
//        });
        // mcxr(bufferedReader);
        enArt(bufferedReader);

    }

    private static void enArt(BufferedReader bufferedReader) {
        bufferedReader.lines().forEach(string -> {
            String line = "";
            for (String s : string.trim().split(" ")) {
                String nextLine = line + s ;
                if (nextLine.length() > 90) {
                    System.out.println(line.trim());
                    line = s + " ";
                } else {
                    line = nextLine + " ";
                }
            }
            System.out.println(line);
        });
    }

    private static void mcxr(BufferedReader bufferedReader) {
        bufferedReader.lines().forEach(string -> {
            String trim = getString(string);
            if (trim.isEmpty()) {
                return;
            }
            int i = 0;
            int len = 55;
            for (; i + len < trim.length(); i+= len) {
                System.out.println(trim.substring(i, i+ len));
            }
            System.out.println(trim.substring(i, trim.length()).trim());
        });
    }

    private static String getString(String string) {
        String replace = string.replace("　", " ").replace("⑴", "")
                .replace("⑵", "")
                .replace("⑶", "")
                .replace("⑷", "")
                .replace("⑸", "")
                .replace("⑻", "")
                .replace("⑼", "")
                .replace("⑽", "")
                .replace("⑾", "")
                .replace("⑿", "")
                .replace("⒀", "")
                .replace("⒁", "")
                .replace("⒂", "")
                .replace("⒃", "")
                .replace("⒄", "")
                .replace("⑹", "")
                .replace("⑺", "");
        return replace.trim();
    }
}