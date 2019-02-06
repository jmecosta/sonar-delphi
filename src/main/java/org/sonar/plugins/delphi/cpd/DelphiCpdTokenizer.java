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
package org.sonar.plugins.delphi.cpd;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.sonar.plugins.delphi.antlr.DelphiLexer;
import org.sonar.plugins.delphi.antlr.sanitizer.DelphiSourceSanitizer;
import org.sonar.plugins.delphi.utils.DelphiUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.plugins.delphi.core.DelphiLanguage;

/**
 * DelphiLanguage tokenizer class. It creates tokens based on antlr Lexer class.
 */
public class DelphiCpdTokenizer implements Sensor {

  public DelphiCpdTokenizer() {
  }

  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor.onlyOnLanguage(DelphiLanguage.KEY).name("DelphiCpdTokenizer");
  }

  @Override
  public void execute(SensorContext context) {
    Set<String> keywords = DelphiKeywords.get();
    Iterable<InputFile> inputFiles = context.fileSystem().inputFiles(context.fileSystem().predicates()
            .and(context.fileSystem().predicates()
                    .hasLanguage(DelphiLanguage.KEY), context.fileSystem().predicates()
                    .hasType(InputFile.Type.MAIN)));

    for (InputFile inputFile : inputFiles) {
      NewHighlighting newHighlighting = context.newHighlighting().onFile(inputFile);
      NewCpdTokens cpdTokens = context.newCpdTokens().onFile(inputFile);

      try {
        DelphiLexer lexer = new DelphiLexer(new DelphiSourceSanitizer(inputFile.filename()));
        Token token = lexer.nextToken();
        while (token.getType() != Token.EOF) {
          TextRange range = inputFile.newRange(token.getLine(), token.getCharPositionInLine(),
                  token.getLine(), token.getCharPositionInLine() + token.getText().length());

          cpdTokens.addToken(range, token.getText());
          
          if(keywords.contains(token.getText()))
          {
            newHighlighting.highlight(token.getLine(), token.getCharPositionInLine(),
                  token.getLine(), token.getCharPositionInLine() + token.getText().length(), TypeOfText.KEYWORD);            
          }
          
          token = lexer.nextToken();
        }

      } catch (IllegalArgumentException | IllegalStateException | IOException e) {
        // ignore range errors: parsing errors could lead to wrong location data
        DelphiUtils.LOG.error("CPD error in file '{}'", inputFile.filename());
      }

      cpdTokens.save();
    }
  }

  /**
   * Create tokens from text.
   *
   * @param source The source code to parse for tokens
   * @return List of found tokens
   */
  public static final List<Token> tokenize(String[] source) {
    List<Token> tokens = new ArrayList<>();

    for (String string : source) {
      DelphiLexer lexer = new DelphiLexer(new ANTLRStringStream(string));
      Token token = lexer.nextToken();
      token.setText(token.getText().toLowerCase());
      while (token.getType() != Token.EOF) {
        tokens.add(token);
        token = lexer.nextToken();
      }
    }
    //has been changed to add compatibility for sonarqube 5.2
    tokens.add(new CommonToken((-1)));
    return tokens;
  }

}
