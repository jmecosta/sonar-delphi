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

package org.sonar.plugins.delphi.surefire;

import org.junit.Before;
import org.junit.Test;
import org.sonar.plugins.delphi.DelphiTestUtils;
import org.sonar.plugins.delphi.core.helpers.DelphiProjectHelper;
import org.sonar.plugins.delphi.utils.DelphiUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import org.sonar.api.batch.sensor.internal.SensorContextTester;

public class SurefireSensorTest {

    private static final String PROJECT_DIR = "/org/sonar/plugins/delphi/UnitTest";
    private static final String PROJECT_TEST_DIR = "/org/sonar/plugins/delphi/UnitTest/tests";
    private static final String SUREFIRE_REPORT_DIR = "./reports";

    SensorContextTester context;
    private DelphiProjectHelper delphiProjectHelper;

    @Before
    public void setup() throws FileNotFoundException {
        List<File> testDirs = new ArrayList<>();
        testDirs.add(DelphiUtils.getResource(PROJECT_TEST_DIR));


        delphiProjectHelper = DelphiTestUtils.mockProjectHelper();
        when(delphiProjectHelper.baseDir()).thenReturn(new File(getClass().getResource(PROJECT_DIR).getFile()));
    }


    @Test
    public void analyzeTest() {
        //Test not working, Due too deprecated methods TODO: Fix this test
        SurefireSensor sensor = new SurefireSensor(delphiProjectHelper);
        //sensor.analyse(project, context);

        //assertEquals(18, context.getMeasuresKeys().size());
    }

    @Test
    public void analyzeTestUsingDefaultSurefireReportsPath() {
        //Test not working, Due too deprecated methods TODO: Fix this test
        SurefireSensor sensor = new SurefireSensor(delphiProjectHelper);
        //sensor.analyse(project, context);

        //assertEquals(24, context.getMeasuresKeys().size());
    }

}
