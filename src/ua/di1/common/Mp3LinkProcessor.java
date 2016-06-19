package ua.di1.common;


import java.io.*;
import java.net.URL;

public class Mp3LinkProcessor implements IHtmlLinkProcessor {
    private final String destinationPath;


    public Mp3LinkProcessor(String destination) {
        if(destination == null) throw new NullPointerException("Link processor must obtain non null destinationPath String.");
        this.destinationPath = destination;
    }

    private String getFilenameFromURL(URL url){
        String urlFile = url.getFile();
        System.out.println(urlFile);
        int fileNameStartIndex = urlFile.lastIndexOf('/') + 1;
        int fileNameEndIndex = urlFile.lastIndexOf('_');
        int fileNameExtensionIndex = urlFile.lastIndexOf('.');
        return ( (fileNameEndIndex >= 0 )
                    && (fileNameExtensionIndex >= 0)
                        && (fileNameStartIndex < fileNameEndIndex))
                ? urlFile.substring(fileNameStartIndex, fileNameEndIndex)
                        .concat(urlFile.substring(fileNameExtensionIndex))
                : null;
    }

    @Override
    public int process(URL url) {
        String fileName = null;
        //url = new URL("http://cdndl.zaycev.net/73481/1021227/mylene_farmer_-_pourvu_qu_elles_soient_douces_(zaycev.net).mp3");
        fileName = getFilenameFromURL(url);
        if(fileName != null){
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                String destination = destinationPath.concat(fileName);
                System.out.println("destination = " + destination);
                if(!(new File(destination).exists())) {
                    bis = new BufferedInputStream(url.openStream());
                    int bufSize = 65535; // bis.available();
                    byte[] buffer = new byte[bufSize];

                    bos = new BufferedOutputStream(new FileOutputStream(destination));

                    int readCount = 0;
                    while ((readCount = bis.read(buffer)) > 0) {
                        bos.write(buffer, 0, readCount);
                    }
                    bos.flush();
                    System.out.println("Done.");
                    return 0;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(bis != null) try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(bos != null){
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("Skipped. " + String.valueOf((Object) fileName));
        return -1;
    }
}
