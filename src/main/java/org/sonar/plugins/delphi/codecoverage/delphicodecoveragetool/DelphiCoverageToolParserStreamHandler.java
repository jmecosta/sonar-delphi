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

import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.delphi.core.helpers.DelphiProjectHelper;

import javax.xml.stream.XMLStreamException;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.coverage.NewCoverage;
import org.sonar.plugins.delphi.utils.StaxParser.XmlStreamHandler;

public class DelphiCoverageToolParserStreamHandler implements XmlStreamHandler {
    private final SensorContext context;
    private final DelphiProjectHelper delphiProjectHelper;

    public DelphiCoverageToolParserStreamHandler(SensorContext context, DelphiProjectHelper delphiProjectHelper) {
        this.context = context;
        this.delphiProjectHelper = delphiProjectHelper;
    }

    @Override
    public void stream(SMHierarchicCursor rootCursor) throws XMLStreamException {
        rootCursor.advance();
        SMInputCursor fileCursor = rootCursor.descendantElementCursor("srcfile");

        while (fileCursor.getNext() != null) {
          collectCoverageData(fileCursor);
        }
    }


    private void collectCoverageData(SMInputCursor fileCursor) {
        try {
            String fileName = fileCursor.getAttrValue("name");

            InputFile sourceFile = delphiProjectHelper.findFileInDirectories(fileName);
            NewCoverage newCoverage = context.newCoverage().onFile(sourceFile);
            
            SMInputCursor lineCursor = fileCursor.descendantElementCursor("line");
            while (lineCursor.getNext() != null) {
                if (!lineCursor.asEvent().isStartElement()) {
                    continue;
                }
                String lineNumber = lineCursor.getAttrValue("number");
                boolean isCovered = Boolean.valueOf(lineCursor.getAttrValue("covered"));               
                newCoverage.lineHits(Integer.parseInt(lineNumber), isCovered ? 1 : 0);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failure trying collect coverage data.", e);
        }
    }
}
