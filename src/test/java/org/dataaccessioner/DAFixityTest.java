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

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Unit test for simple App.
 */
public class DAFixityTest {

    @Test
    public void testParseReport() {
        List<DAFile> expected_dafiles = getMockDAFiles();
        List<DAFile> dafiles = new ArrayList<>();
        File reportFile = new File("src/test/resources/testreport.xml");
        DAReportParser rptParser = new DAReportParser(reportFile);
        try {
            dafiles = rptParser.parse();
        } catch (Exception e) {
            Assert.fail("failed: ", e);
        }

        Assert.assertEquals(expected_dafiles.size(), dafiles.size());
        for (int i = 0;  i < expected_dafiles.size(); i++) {
            Assert.assertEquals(expected_dafiles.get(i).getFilePath().toString(), dafiles.get(i).getFilePath().toString());
            Assert.assertEquals(expected_dafiles.get(i).getChecksum(), dafiles.get(i).getChecksum());
            Assert.assertEquals(expected_dafiles.get(i).getAccessionID(), dafiles.get(i).getAccessionID());
        }
    }

    @Test
    public void testFileNotFound() {
        List<DAFile> dafiles = getMockDAFiles();

        DAFile missing_dafile = dafiles.get(6);

        String fullpath = DAFixity.getCompletePath(new File("src/test/resources"), missing_dafile);

        Assert.assertEquals(false, DAFixity.checkReadFile(new File(fullpath)));
    }

    @Test
    public void testFileFound() {
        List<DAFile> dafiles = getMockDAFiles();

        DAFile dafile = dafiles.get(5);
        String fullpath = DAFixity.getCompletePath(new File("src/test/resources"), dafile);

        Assert.assertEquals(true, DAFixity.checkReadFile(new File(fullpath)));
    }

    @Test
    public void testMismatchedChecksums() {
        List<DAFile> dafiles = getMockDAFiles();

        DAFile dafile = dafiles.get(7);
        String fullpath = DAFixity.getCompletePath(new File("src/test/resources"), dafile);

        Assert.assertEquals(false, DAFixity.checkFileChecksum(new File(fullpath), dafile.getChecksum()));
    }

    @Test
    public void testMatchedChecksums() {
        List<DAFile> dafiles = getMockDAFiles();

        DAFile dafile = dafiles.get(0);
        String fullpath = DAFixity.getCompletePath(new File("src/test/resources"), dafile);

        Assert.assertEquals(true, DAFixity.checkFileChecksum(new File(fullpath), dafile.getChecksum()));
    }

    private List<DAFile> getMockDAFiles() {
        List<DAFile> dafiles = new ArrayList<>();

        DAFile dafile1 = new DAFile(new File("/test-collection/folder1/folder1a/sample1a.jpg"), "329d2ee68ee708bd4421b36025bb166f", "dafixity-sample" );
        dafiles.add(dafile1);
        DAFile dafile2 = new DAFile(new File("/test-collection/folder1/folder1a/sample1a.pdf"), "aa5e1ec3f6cbe32c95982b6e3d511af2", "dafixity-sample" );
        dafiles.add(dafile2);
        DAFile dafile3 = new DAFile(new File("/test-collection/folder1/folder1b/sample1b.odt"), "3e23fb8d0968e6b37bb63278d3e27f70", "dafixity-sample" );
        dafiles.add(dafile3);
        DAFile dafile4 = new DAFile(new File("/test-collection/folder2/sample2.wpd"), "09f827d28541d6d845447432fdbaf9da", "dafixity-sample" );
        dafiles.add(dafile4);
        DAFile dafile5 = new DAFile(new File("/test-collection/folder2/folder2a/sample2a"), "fa11d8719f82d99f034a9ab2e52ac446", "dafixity-sample" );
        dafiles.add(dafile5);
        DAFile dafile6 = new DAFile(new File("/test-collection/folder3/folder3a/folder3ai/random3ai"), "8111830d5c398028e675b45187af10f6", "dafixity-sample" );
        dafiles.add(dafile6);
        DAFile dafile7 = new DAFile(new File("/test-collection/folder4-deleted/deleted-sample4.m4v"), "a6f6fd76fed0770862b9ff0049b357d0", "dafixity-sample" );
        dafiles.add(dafile7);
        DAFile dafile8 = new DAFile(new File("/test-collection/folder5-changed/folder5a-changed/changed-sample5a.txt"), "d61e884cdf73257617e7947defe8eb09", "dafixity-sample" );
        dafiles.add(dafile8);

        return dafiles;
    }
}
