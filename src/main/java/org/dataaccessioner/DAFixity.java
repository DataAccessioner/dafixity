/*
 * Copyright (c) 2017 Scott Prater
 *
 *     This file is part of DAFixity.
 *
 *     DAFixity is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     DAFixity is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dataaccessioner;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class DAFixity
{

    private static final String NAME = "dafixity";
    private static String version;
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS zzz");
    private static final Logger logger = LoggerFactory.getLogger(DAFixity.class);

    public static void main( String[] args ) {

        String report_path = "";
        String directory_path = "";
        List<DAFile> dafiles = new ArrayList<>();

        version = getVersion();

        // Get the report and directory path
        Options options = new Options();
        options.addOption(Option.builder("r").hasArg().longOpt("report").desc("Path to Data Accessioner report file").argName("REPORT").required().build());
        options.addOption(Option.builder("d").hasArg().longOpt("directory").desc("Base directory for accession tree in the report").argName("DIRECTORY").required().build());
        options.addOption(Option.builder("h").longOpt("help").desc("This help message").build());

        CommandLineParser optParser = new DefaultParser();

        try {
            CommandLine cmd = optParser.parse(options, args);

            if (cmd != null && cmd.hasOption("h")) {
                printHelp(options);
                System.exit(0);
            }

            if (cmd != null && cmd.hasOption("r")) {
                report_path = cmd.getOptionValue("r");
            }

            if (cmd != null && cmd.hasOption("d")) {
                directory_path = cmd.getOptionValue("d");
            }

        } catch (ParseException pe) {
            System.err.println("Unable to read command line options: " + pe.getMessage());
            printHelp(options);
            System.exit(1);
        }

        if (report_path == null || report_path.isEmpty()) {
            System.err.println("Invalid report path");
            printHelp(options);
            System.exit(1);
        }

        if (directory_path == null || directory_path.isEmpty()) {
            System.err.println("Invalid path to accession directory");
            printHelp(options);
            System.exit(1);
        }

        File reportFile = new File(report_path);
        if (! reportFile.isFile() || ! reportFile.canRead()) {
            System.err.println("Report file '" + report_path + "' cannot be found or is not readable.");
            printHelp(options);
            System.exit(1);
        }

        File baseDirectory = new File(directory_path);
        if (! baseDirectory.isDirectory() || ! baseDirectory.canRead()) {
            System.err.println("Accession directory '" + directory_path + "' cannot be found or is not readable.");
            printHelp(options);
            System.exit(1);
        }

        // Initialize the log
        logger.info( "Running " + NAME + ", version " + version);
        logger.info("Report file is '" + reportFile.getAbsolutePath() + "'");
        logger.info("Accession directory path is '" + baseDirectory.getAbsolutePath() + "'");

        // Parse the report, get our list of files
        logger.info("Parsing report to get the list of files and their checksums");
        DAReportParser rptParser = new DAReportParser(reportFile);
        try {
            dafiles = rptParser.parse();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        // Run the check
        Date startDate = new Date();
        logger.info("Starting fixity check at " + DATE_FORMAT.format(startDate));

        long startTime = System.currentTimeMillis();

        for (DAFile dafile : dafiles) {
            System.out.println("File: '" + dafile.getFilePath().toString() +"'");
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        Date endDate = new Date();
        logger.info("Ending fixity check at " + DATE_FORMAT.format(endDate));
        logger.info("Fixity check run time: " + getDuration(elapsedTime));
    }

    private static void printHelp(Options opts) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(null);
        formatter.printHelp(NAME + " [options]", opts);
    }


    private static String getVersion() {
        String propfile = "/version.properties";
        InputStream stream = DAFixity.class.getResourceAsStream(propfile);
        if (stream == null) return "UNKNOWN";
        Properties props = new Properties();
        try {
	        props.load(stream);
	        stream.close();
	        return (String)props.get("version");
	    } catch (IOException e) {
	        return "UNKNOWN";
	    }
    }

    private static String getDuration(long elapsedTime) {
        String hhmmssms = String.format("%02d:%02d:%02d.%03d",
                TimeUnit.MILLISECONDS.toHours(elapsedTime),
                TimeUnit.MILLISECONDS.toMinutes(elapsedTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsedTime)),
                TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime)),
                elapsedTime - TimeUnit.MILLISECONDS.toSeconds(elapsedTime));
        return hhmmssms;
    }
}
