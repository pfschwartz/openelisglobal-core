package oe.analyzerfilebroker.actor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by paulsc on 10/6/2015.
 */
public class ConnectionValidator {
    private static final String PING_FILE = "ping.txt";
    public boolean validateConnection(FileShipper shipper) {
        Path queue = shipper.getTransmissionQueuePath();

        File ping = new File( queue.toString(), PING_FILE);
        try {
            ping.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        ping.deleteOnExit();

        boolean success = shipper.sendFile(ping);


        return false;
    }
}
