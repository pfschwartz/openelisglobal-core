package oe.analyzerfilebroker.actor;

import oe.analyzerfilebroker.StringLocalization;
import oe.analyzerfilebroker.config.GeneralConfig;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.*;

/**
 * Created by paulsc on 9/23/2015.
 */
public class FileShipper {
    private final String url;
    private final String name;
    private final String password;
    private static final String CHARSET = "UTF-8";
    private static final String CRLF = "\r\n";
    private static final String PARAM = "value";
    private Path queue;
    private static final String QUEUE = "." + File.separator + "transmissionQueue";

    public FileShipper(GeneralConfig config){
        url = config.getUrl();
        name = config.getName();
        password = config.getPassword();

        ensureQueueDirectory();
    }

    public Path getTransmissionQueuePath(){
        return queue;
    }
    private void ensureQueueDirectory() {
        queue = Paths.get(QUEUE);

        boolean create = Files.notExists(queue, LinkOption.NOFOLLOW_LINKS);

        if( create){
            try {
                Files.createDirectory(queue);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean sendFile(File textFile) {
        synchronized (FileShipper.class){
            String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
            URLConnection connection = null;
            try {
                connection = new URL(url).openConnection();
                connection.setRequestProperty("user", name);
                connection.setRequestProperty("password", password);
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            try {
                    OutputStream output = connection.getOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, CHARSET), true);

                // Send normal PARAM.
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"PARAM\"").append(CRLF);
                writer.append("Content-Type: text/plain; CHARSET=" + CHARSET).append(CRLF);
                writer.append(CRLF).append(PARAM).append(CRLF).flush();

                // Send text file.
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"textFile\"; filename=\"" + textFile.getName() + "\"").append(CRLF);
                writer.append("Content-Type: text/plain; CHARSET=" + CHARSET).append(CRLF); // Text file itself must be saved in this CHARSET!
                writer.append(CRLF).flush();
                Files.copy(textFile.toPath(), output);
                output.flush(); // Important before continuing with writer!
                writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.


                // End of multipart/form-data.
                writer.append("--" + boundary + "--").append(CRLF).flush();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

// Request is lazily fired whenever you need to obtain information about response.
            int responseCode = 0;
            try {
                responseCode = ((HttpURLConnection) connection).getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if( responseCode == HttpURLConnection.HTTP_PARTIAL){
                System.out.println(responseCode); // Should be 200
                System.out.println(StringLocalization.instance().getStringForKey("error.http.partial"));
                return false;
            }

            System.out.println("yikes: " + responseCode); // Should be 200

        }

        return false;
    }

    private void sendFile(String fileName) {

    }
}
