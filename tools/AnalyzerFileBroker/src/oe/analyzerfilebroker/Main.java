package oe.analyzerfilebroker;

import oe.analyzerfilebroker.actor.ConnectionValidator;
import oe.analyzerfilebroker.actor.FileShipper;
import oe.analyzerfilebroker.config.CommandLineParser;
import oe.analyzerfilebroker.config.GeneralConfig;
import oe.analyzerfilebroker.config.XMLParser;

public class Main {

    public static void main(String[] args) {
        StringLocalization.instance().setLocale(StringLocalization.supportedLocale.FR);

        CommandLineParser commandLineParser = new CommandLineParser();
        GeneralConfig generalConfig = commandLineParser.parse(args);
        if( generalConfig == null){
            return;
        }

        boolean valid = new XMLParser().parse( generalConfig);

        if( valid){
            FileShipper shipper = new FileShipper(generalConfig);
            boolean connectionValid = new ConnectionValidator().validateConnection(shipper);
        }

        new QuitHandler();

        Object mon = new String();
        try {
            synchronized (mon) {
                mon.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
