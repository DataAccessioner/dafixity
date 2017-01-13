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

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sprater on 1/12/17.
 */
public class DAReportParser {

    private File report;

    public DAReportParser(File report) {
        this.report = report;
    }

    public List<DAFile> parse() throws ParserConfigurationException, SAXException, IOException {
        List<DAFile> dafiles = new ArrayList<>();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        DAReportHandler rptHandler = new DAReportHandler();
        saxParser.parse(report, rptHandler);
        return rptHandler.getDafiles();
    }
}
