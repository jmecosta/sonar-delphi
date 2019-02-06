/**
 * Sonar Delphi Plugin
 * Copyright (C) 2011 Sabre Airline Solutions and Fabricio Colombo
 * Author(s):
 * Przemyslaw Kociolek (przemyslaw.kociolek@sabre.com)
 * Michal Wojcik (michal.wojcik@sabre.com)
 * Fabricio Colombo (fabricio.colombo.mva@gmail.com)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.plugins.delphi.codecoverage.delphicodecoveragetool;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.plugins.delphi.DelphiTestUtils;
import org.sonar.plugins.delphi.core.helpers.DelphiProjectHelper;
import org.sonar.plugins.delphi.utils.DelphiUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.plugins.delphi.utils.TestUtils;

public class DelphiCoverageToolParserTest {
    private static final String ROOT_NAME = "/org/sonar/plugins/delphi/SimpleDelphiProject";
    private static final String REPORT_FILE = "/org/sonar/plugins/delphi/SimpleDelphiProject/reports/Coverage.xml";
    private final File reportFile = DelphiUtils.getResource(REPORT_FILE);
    private DelphiProjectHelper delphiProjectHelper;

    @Before
    public void init() throws FileNotFoundException, IOException {

        

        File baseDir = DelphiUtils.getResource(ROOT_NAME);

        List<File> sourceDirs = new ArrayList<>();

        sourceDirs.add(baseDir); // include baseDir

        delphiProjectHelper = DelphiTestUtils.mockProjectHelper();    
        File target = new File(baseDir, "cpd.cc");
        String content = new String(Files.readAllBytes(target.toPath()), "UTF-8");
        InputFile inputFile = TestInputFileBuilder.create("moduleKey", baseDir, target).setType(InputFile.Type.MAIN).setContents(content).setCharset(Charset.forName("UTF-8")).build();
        when(delphiProjectHelper.findFileInDirectories(REPORT_FILE)).thenReturn(inputFile);

    }

    @Test
    public void parseTest() {
        DelphiCodeCoverageToolParser parser = new DelphiCodeCoverageToolParser(reportFile, delphiProjectHelper);
        DefaultFileSystem fs = TestUtils.mockFileSystem("");
        SensorContext debugContext = SensorContextTester.create(fs.baseDir());
        parser.parse(debugContext);

        String coverage_names[] = {"Globals.pas:coverage", "MainWindow.pas:coverage"};
        double coverage_values[] = {100.00, 50.00};
        String lineHits_names[] = {"Globals.pas:coverage_line_hits_data", "MainWindow.pas:coverage_line_hits_data"};
        String lineHits_values[] = {"19=1;20=1", "36=1;37=0;38=1;39=0"};

    }

}
