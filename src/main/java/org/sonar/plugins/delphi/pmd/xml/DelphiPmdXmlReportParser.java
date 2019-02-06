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

 /**
 * Sonar MSBuild Plugin, open source software quality management tool.
 * Author(s) : Jorge Costa @ jmecsoftware.com
 *
 * Sonar MSBuild Plugin is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar MSBuild Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.sonar.plugins.delphi.pmd.xml;

import com.sonar.sslr.api.Grammar;
import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.issue.Issue;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.delphi.core.helpers.DelphiProjectHelper;
import org.sonar.plugins.delphi.pmd.DelphiPmdConstants;
import org.sonar.plugins.delphi.utils.DelphiUtils;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.plugins.delphi.utils.StaxParser;
import org.sonar.squidbridge.SquidAstVisitor;

/**
 * Parses PMD xml report
 */
public class DelphiPmdXmlReportParser {

  private final DelphiProjectHelper delphiProjectHelper;
  private List<Issue> toReturn = new ArrayList<>();

  public DelphiPmdXmlReportParser(DelphiProjectHelper delphiProjectHelper) {
    this.delphiProjectHelper = delphiProjectHelper;
  }

  /**
   * Parses XML file
   *
   * @param context
   * @param xmlFile PMD xml file
   * @return returns a list of issues which can be used for further usage
   */
  public List<Issue> parse(final SensorContext context, File xmlFile) {
    StaxParser parser = new StaxParser(new StaxParser.XmlStreamHandler() {

      @Override
      public void stream(SMHierarchicCursor rootCursor) throws XMLStreamException {
        rootCursor.advance();

        SMInputCursor fileCursor = rootCursor.descendantElementCursor("file");
        while (fileCursor.getNext() != null) {
          String fileName = fileCursor.getAttrValue("name");
          SMInputCursor violationCursor = fileCursor.descendantElementCursor("violation");
          while (violationCursor.getNext() != null) {
            String beginLine = violationCursor.getAttrValue("beginline");
            //String endLine = violationCursor.getAttrValue("beginline");
            String ruleKey = violationCursor.getAttrValue("rule");
            String message = StringUtils.trim(violationCursor.collectDescendantText());
            addIssue(context, ruleKey, fileName, Integer.parseInt(beginLine), message);
          }
        }
      }
    });

    try {
      parser.parse(xmlFile);
    } catch (XMLStreamException e) {
      DelphiUtils.LOG.error("Error parsing file : {}", xmlFile);
    }
    return toReturn;
  }

  private void addIssue(final SensorContext context, String ruleKey, String fileName, Integer beginLine, String message) {

    DelphiUtils.LOG.debug("PMD Violation - rule: " + ruleKey + " file: " + fileName);
    InputFile inputFile = delphiProjectHelper.getFile(fileName);

      //This line has been added to debug since it'soften a problem
      DelphiUtils.LOG.debug("Line of the issue is:" + beginLine + "     And the File has, amount lines:" + inputFile.lines());
      //note this has been added to get compatibility with sonar 5.2
      if (inputFile.lines() >= beginLine && inputFile.lines() != -1 && beginLine != -1) {
        final NewIssue newIssue = context.newIssue().forRule(RuleKey.of(DelphiPmdConstants.REPOSITORY_KEY, ruleKey));
        final NewIssueLocation newIssueLocation = newIssue.newLocation().on(inputFile).at(inputFile.selectLine(beginLine)).message(message);        
        newIssue.at(newIssueLocation);
        newIssue.save();        

      } else {
        final NewIssue newIssue = context.newIssue().forRule(RuleKey.of(DelphiPmdConstants.REPOSITORY_KEY, ruleKey));
        final NewIssueLocation newIssueLocation = newIssue.newLocation().on(inputFile).at(inputFile.selectLine(beginLine)).message(message);        
        newIssue.at(newIssueLocation);
        newIssue.save();
      }
  }
}
