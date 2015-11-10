package oe.analyzerfilebroker.config;

import oe.analyzerfilebroker.StringLocalization;
import org.kohsuke.args4j.*;
import org.kohsuke.args4j.spi.OptionHandler;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulsc on 9/23/2015.
 */
public class CommandLineParser {


    @Option(name="-configPath",usage="usage.configFile")
    private File confFile = new File("./BrokerConfiguration.xml");

    @Option(name="-lang", aliases={"-l"},usage="usage.language")
    private StringLocalization.supportedLocale language= null;

    @Option(name="-period", aliases = {"-p"}, usage="usage.period" )
    private int period = 5;

    @Option(name="-url", aliases = {"-u"}, usage = "usage.url")
    private String url;

    @Option(name="-acountName", aliases = {"-n"}, usage = "usage.account.name")
    private String name;

    @Option(name="-password", aliases = {"-pwd"}, usage = "usage.account.password")
    private String password;

    @Option(name="-log", usage="usage.log")
    private String log;

    @Option(name="-logBacklog", aliases = {"-b"}, usage = "usage.log.backlog")
    private int logBacklog = 30;

    @Option(name="-console", aliases = {"-c"}, usage = "usage.console")
    private boolean printToConsole = true;

    @Option(name="-help", aliases = {"-h"}, usage = "usage.help", forbids = {"-test", "-configSample"})
    private boolean help = false;

    @Option(name="-test", aliases = {"-t"}, usage = "usage.test", forbids = {"-help", "-configSample"})
    private boolean test = false;

    @Option(name="-configSample", aliases = {"-s"}, usage = "usage.sample", forbids = {"-help", "-test"})
    private boolean sample = false;

    @Argument
    private List<String> arguments = new ArrayList<String>();


    public GeneralConfig parse(String[] args){
        CmdLineParser parser = new CmdLineParser(this);


        try {
            parser.parseArgument(args);
        } catch( CmdLineException e ) {
            language = StringLocalization.supportedLocale.EN;
            System.err.println(e.getMessage());
            printUsage(parser);
            return null;

        }

        if( language != null){
            StringLocalization.instance().setLocale(language);
        }

        if( help ){
            printUsage(parser);
            return null;
        }

        if( !confFile.exists() ){
            System.out.println("Configuration file " + confFile.getAbsolutePath() + " does not exist");
            return null;
        }

        return buildConfig();
    }

    private void printUsage(CmdLineParser parser){
        System.err.println("java -jar AnalyzerFileBroker [options...] arguments...");
        System.err.println();
        parser.printUsage(new PrintWriter(System.out, true), StringLocalization.instance().getResourceBundle());
        System.err.println("  Example: java -jar AnalyzerFileBroker" + parser.printExample(OptionHandlerFilter.ALL, StringLocalization.instance().getResourceBundle()));

    }
    private GeneralConfig buildConfig() {
        GeneralConfig config = new GeneralConfig();

        config.setFile(confFile);
        config.setLanguage(language);
        config.setLog(log);
        config.setLogBacklog(logBacklog);
        config.setName(name);
        config.setPassword(password);
        config.setPeriod( period);
        config.setUrl(url);
        config.setTest(test);
        config.setSample(sample);

        return config;
    }
}
