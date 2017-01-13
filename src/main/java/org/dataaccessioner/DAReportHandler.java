/*
 * Copyright (c) 2017 Scott Prater
 *
 *      This file is part of DAFixity.
 *
 *      DAFixity is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      DAFixity is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dataaccessioner;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prater on 1/12/2017.
 */
public class DAReportHandler extends DefaultHandler {

    private static String accessionID = "unknown";
    private static StringBuilder currpath;
    private static List<DAFile> dafiles;

    @Override
    public void startElement(String uri,
                             String localName, String qName, Attributes attributes)
            throws SAXException {
        if (currpath == null)
            currpath = new StringBuilder("");
        if (dafiles == null)
            dafiles = new ArrayList<>();

        if (qName.equalsIgnoreCase("accession")) {
            accessionID = attributes.getValue("number");
        } else if (qName.equalsIgnoreCase("folder")) {
            String dirname = attributes.getValue("name");
            currpath.append(File.separator).append(dirname);
        } else if (qName.equalsIgnoreCase("file")) {
            String filename = attributes.getValue("name");
            String checksum = attributes.getValue("MD5");
            currpath.append(File.separator).append(filename);
            DAFile dafile = new DAFile(new File(currpath.toString()), checksum, accessionID);
            dafiles.add(dafile);
        }
    }

    @Override
    public void endElement(String uri,
                           String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("folder") ||
                qName.equalsIgnoreCase("file") ) {
            currpath.delete(currpath.lastIndexOf(File.separator), currpath.length());
        }
    }

    public List<DAFile> getDafiles() {
        return this.dafiles;
    }
}
