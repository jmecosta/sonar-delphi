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
package org.sonar.plugins.delphi;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.rule.ActiveRule;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.delphi.core.helpers.DelphiProjectHelper;
import org.sonar.plugins.delphi.debug.ProjectMetricsXMLParser;
import org.sonar.plugins.delphi.project.DelphiProject;
import org.sonar.plugins.delphi.utils.DelphiUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.plugins.delphi.utils.TestUtils;

public class DelphiSensorTest {

    private static final String ROOT_NAME = "/org/sonar/plugins/delphi/SimpleDelphiProject";
    private final DelphiProject delphiProject = new DelphiProject("Default Project");
    private DelphiSensor sensor = null;
    private File baseDir = null;
    private Map<String, Integer> keyMetricIndex = null;

    @Before
    public void init() {

        baseDir = DelphiUtils.getResource(ROOT_NAME);
        File reportDir = new File(baseDir.getAbsolutePath() + "/reports");

        File[] dirs = baseDir.listFiles(DelphiUtils.getDirectoryFilter()); // get
        // all
        // directories

        List<File> sourceDirs = new ArrayList<>(dirs.length);
        List<InputFile> sourceFiles = new ArrayList<>();

        sourceDirs.add(baseDir); // include baseDir
        for (File source : baseDir.listFiles(DelphiUtils.getFileFilter())) {
          
          InputFile inputFile = TestInputFileBuilder.create("moduleKey", new File(""), source).setType(InputFile.Type.MAIN)
            .setContents("").setCharset(Charset.forName("UTF-8")).build();   
    
            sourceFiles.add(inputFile);
        }

        for (File directory : dirs) { // get all source files from all
            // directories
            File[] files = directory.listFiles(DelphiUtils.getFileFilter());
            for (File sourceFile : files) {
          InputFile inputFile = TestInputFileBuilder.create("moduleKey", new File(""), sourceFile).setType(InputFile.Type.MAIN)
            .setContents("").setCharset(Charset.forName("UTF-8")).build();                 
                sourceFiles.add(inputFile);
            }
            sourceDirs.add(directory); // put all directories to list
        }

        DelphiProjectHelper delphiProjectHelper = DelphiTestUtils.mockProjectHelper();
        DelphiTestUtils.mockGetFileFromString(delphiProjectHelper);

        delphiProject.setSourceFiles(sourceFiles);

        ActiveRules activeRules = mock(ActiveRules.class);
        ActiveRule activeRule = mock(ActiveRule.class);
        when(activeRules.find(Matchers.any(RuleKey.class))).thenReturn(activeRule);
        when(activeRule.param("Threshold")).thenReturn("3");

        sensor = new DelphiSensor(delphiProjectHelper, activeRules);
    }

    @Test
    public void analyseTest() {
        createKeyMetricIndexMap();

        // xml file for expected metrics for files
        ProjectMetricsXMLParser xmlParser = new ProjectMetricsXMLParser(new File(baseDir.getAbsolutePath() + File.separator + "values.xml"));

        DefaultFileSystem fs = TestUtils.mockFileSystem("");
        SensorContext context = SensorContextTester.create(fs.baseDir());        

//    sensor.analyse(project, context); // analysing project

        // create a map of expected values for each file
        Map<String, Double[]> expectedValues = new HashMap<>();
        for (String fileName : xmlParser.getFileNames()) {
            expectedValues.put(fileName.toLowerCase(), xmlParser.getFileValues(fileName));
        }
    }

    private void createKeyMetricIndexMap() {
        keyMetricIndex = new HashMap<>();
        keyMetricIndex.put("complexity", 0);
        keyMetricIndex.put("functions", 1);
        keyMetricIndex.put("function_complexity", 2);
        keyMetricIndex.put("classes", 3);
        keyMetricIndex.put("lines", 4);
        keyMetricIndex.put("comment_lines", 5);
        keyMetricIndex.put("accessors", 6);
        keyMetricIndex.put("public_undocumented_api", 7);
        keyMetricIndex.put("ncloc", 8);
        keyMetricIndex.put("files", 9);
        keyMetricIndex.put("package.files", 10);
        keyMetricIndex.put("package.packages", 11);
        keyMetricIndex.put("class_complexity", 12);
        keyMetricIndex.put("noc", 13);
        keyMetricIndex.put("statements", 14);
        keyMetricIndex.put("public_api", 15);
        keyMetricIndex.put("comment_blank_lines", 16);
    }

    @Test
    public void analyseFileOnRootDir() {
        createKeyMetricIndexMap();

        ProjectMetricsXMLParser xmlParser = new ProjectMetricsXMLParser(new File(baseDir.getAbsolutePath()
                + File.separator + "values.xml")); // xml file for
        // expected
        // metrics for
        // files
        DefaultFileSystem fs = TestUtils.mockFileSystem("");
        SensorContext context = SensorContextTester.create(fs.baseDir());      
        // context for
        // debug
        // information
//    sensor.analyse(project, context); // analysing project

        Map<String, Double[]> expectedValues = new HashMap<>(); // create
        // a
        // map
        // of
        // expected
        // values
        // for
        // each
        // file
        for (String fileName : xmlParser.getFileNames()) {
            expectedValues.put(fileName, xmlParser.getFileValues(fileName));
        }
    }

    @Test
    public void analyseWithEmptySourceFiles() {
        delphiProject.getSourceFiles().clear();
        DefaultFileSystem fs = TestUtils.mockFileSystem("");
        SensorContext context = SensorContextTester.create(fs.baseDir());      
        sensor.execute(context);
    }

    @Test
    public void analyseWithBadSourceFileSintax() {
        delphiProject.getSourceFiles().clear();
        delphiProject.getSourceFiles().add(new File(baseDir + "/Globals.pas"));
        delphiProject.getSourceFiles().add(new File(baseDir + "/../BadSyntax.pas"));
        DefaultFileSystem fs = TestUtils.mockFileSystem("");
        SensorContext context = SensorContextTester.create(fs.baseDir());      
        sensor.execute(context);

        assertThat("processed files", sensor.getProcessedFilesCount(), is(1));
        assertThat("units", sensor.getUnits(), hasSize(1));
        assertThat("file classes", sensor.getFileClasses().size(), is(1));
        assertThat("file functions", sensor.getFileFunctions().size(), is(1));
    }

}
