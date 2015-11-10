package oe.analyzerfilebroker;



/**
 * Created by paulsc on 9/21/2015.
 */
public class QuitHandler {
    public QuitHandler(){
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println(StringLocalization.instance().getStringForKey("shutdown.start"));

                System.out.println(StringLocalization.instance().getStringForKey("shutdown.finished"));
            }
        });
    }
}
