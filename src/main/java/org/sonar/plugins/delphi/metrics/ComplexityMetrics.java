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

package org.sonar.plugins.delphi.metrics;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.ActiveRule;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.delphi.antlr.DelphiParser;
import org.sonar.plugins.delphi.core.language.ClassInterface;
import org.sonar.plugins.delphi.core.language.FunctionInterface;
import org.sonar.plugins.delphi.core.language.UnitInterface;
import org.sonar.plugins.delphi.pmd.DelphiPmdConstants;
import org.sonar.plugins.delphi.utils.DelphiUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;

/**
 * Class counting function cyclomatic complexity.
 */
public class ComplexityMetrics extends DefaultMetrics implements MetricsInterface {

  public static final RuleKey RULE_KEY_METHOD_CYCLOMATIC_COMPLEXITY = RuleKey.of(DelphiPmdConstants.REPOSITORY_KEY, "MethodCyclomaticComplexityRule");
  private static final Number[] FUNCTIONS_DISTRIB_BOTTOM_LIMITS = {1, 2, 4, 6, 8, 10, 12, 20, 30};
  private static final Number[] FILES_DISTRIB_BOTTOM_LIMITS = {1, 5, 10, 20, 30, 60, 90};
  private ActiveRule methodCyclomaticComplexityRule;

  /**
   * The Cyclomatic Complexity Number.
   */
  private double fileComplexity = 0;
  /**
   * Average cyclomatic complexity number by method.
   */
  private double functionComplexity = 0;
  /**
   * Number of classes including nested classes, interfaces, enums and
   * annotations.
   */
  private double classCount = 0;
  /**
   * Average complexity by class.
   */
  private double classComplexity = 0;
  /**
   * Number of Methods without including accessors. A constructor is considered
   * to be a method.
   */
  private double methodsCount = 0;
  /**
   * Number of getter and setter methods used to get(reading) or set(writing) a
   * class' property.
   */
  private double accessorsCount = 0;
  /**
   * Number of statements as defined in the DelphiLanguage Language
   * Specification but without block definitions.
   */
  private double statementsCount = 0;
  /**
   * Number of public classes, public methods (without accessors) and public
   * properties (without public final static ones).
   */
  private double publicApi = 0;

  private Integer threshold;

  /**
   * {@inheritDoc}
   */
  public ComplexityMetrics(ActiveRules activeRules) {
    super();
    methodCyclomaticComplexityRule = activeRules.find(RULE_KEY_METHOD_CYCLOMATIC_COMPLEXITY);
    threshold = Integer.valueOf(methodCyclomaticComplexityRule.param("Threshold"));

  }

  /**
   * Analyses DelphiLanguage source file
   *
   * @param resource DelphiLanguage source file (.pas) to analyse
   * @param sensorContext Sensor context, given by Sonar
   * @param classes Classes that were found in that file
   * @param functions Functions that were found in that file
   */
  @Override
  public void analyse(InputFile resource, SensorContext sensorContext, List<ClassInterface> classes,
          List<FunctionInterface> functions,
          Set<UnitInterface> units) {
    reset();
    Set<String> processedFunc = new HashSet<>();
    if (classes != null) {
      for (ClassInterface cl : classes) {
        if (cl == null) {
          continue;
        }

        ++classCount;
        fileComplexity += cl.getComplexity();
        classComplexity += cl.getComplexity();
        accessorsCount += cl.getAccessorCount();
        publicApi += cl.getPublicApiCount();

        for (FunctionInterface func : cl.getFunctions()) {
          processFunction(resource, func, sensorContext);
          processedFunc.add(func.getName());
        }
      }
    }

    // procesing stand-alone (global) functions, not merged with any class
    if (functions != null) {
      for (FunctionInterface func : functions) {
        if (func == null) {
          continue;
        }
        if (processedFunc.contains(func.getName())) {
          continue;
        }
        methodsCount += 1 + func.getOverloadsCount();
        fileComplexity += func.getComplexity();
        functionComplexity += func.getComplexity();
        statementsCount += func.getStatements().size();
        if (func.getVisibility() == DelphiParser.PUBLIC) {
          ++publicApi;
        }
        processedFunc.add(func.getName());

        addIssue(resource, func, sensorContext);
      }
    }

    if (methodsCount != 0.0) {
      functionComplexity /= methodsCount;
    }
    if (classCount != 0.0) {
      classComplexity /= classCount;
    }

    saveAllMetrics();
  }

  private void processFunction(InputFile resource, FunctionInterface func, SensorContext sensor) {
    if (!func.isAccessor()) {
      methodsCount++;
      functionComplexity += func.getComplexity();

      addIssue(resource, func, sensor);

      for (FunctionInterface over : func.getOverloadedFunctions()) {
        processFunction(resource, over, sensor);
      }
    }
    statementsCount += func.getStatements().size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(InputFile resource, SensorContext sensorContext) {
    if (resource == null || sensorContext == null) {
      return;
    }
    try {
      // Number of statements as defined in the DelphiLanguage Language Specification but without block definitions.         
      sensorContext.<Integer>newMeasure()
              .forMetric(CoreMetrics.STATEMENTS)
              .on(sensorContext.module())
              .withValue((int) (getMetric("STATEMENTS")))
              .save();

      // The Cyclomatic Complexity Number
      sensorContext.<Integer>newMeasure()
              .forMetric(CoreMetrics.COMPLEXITY)
              .on(sensorContext.module())
              .withValue((int) (getMetric("COMPLEXITY")))
              .save();

      // Number of classes including nested classes, interfaces, enums and annotations
      sensorContext.<Integer>newMeasure()
              .forMetric(CoreMetrics.CLASSES)
              .on(sensorContext.module())
              .withValue((int) (getMetric("CLASSES")))
              .save();

      // Number of Methods without including accessors. A constructor is considered to be a method.
      sensorContext.<Integer>newMeasure()
              .forMetric(CoreMetrics.FUNCTIONS)
              .on(sensorContext.module())
              .withValue((int) (getMetric("FUNCTIONS")))
              .save();

      // Number of public classes, public methods (without accessors) and public properties (without public static final ones)
      sensorContext.<Integer>newMeasure()
              .forMetric(CoreMetrics.PUBLIC_API)
              .on(sensorContext.module())
              .withValue((int) (getMetric("PUBLIC_API")))
              .save();
    } catch (IllegalStateException ise) {
      DelphiUtils.LOG.error(ise.getMessage());
    }
  }

  private void saveAllMetrics() {
    setMetric("STATEMENTS", statementsCount);
    setMetric("COMPLEXITY", fileComplexity);
    setMetric("CLASS_COMPLEXITY", classComplexity);
    setMetric("FUNCTION_COMPLEXITY", functionComplexity);
    setMetric("CLASSES", classCount);
    setMetric("FUNCTIONS", methodsCount);
    setMetric("ACCESSORS", accessorsCount);
    setMetric("PUBLIC_API", publicApi);
  }

  private void reset() {
    fileComplexity = 0;
    functionComplexity = 0;
    classCount = 0;
    classComplexity = 0;
    methodsCount = 0;
    accessorsCount = 0;
    statementsCount = 0;
    publicApi = 0;
    clearMetrics();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean executeOnResource(InputFile resource) {
    return true;
  }

  private void addIssue(InputFile resource, FunctionInterface func, SensorContext sensorContext) {
    if (func.getComplexity() > threshold) {

      //This line has been added to debug since it'soften a problem
      DelphiUtils.LOG.debug("Line of the issue is:" + methodCyclomaticComplexityRule.ruleKey() + "     And the File has, amount lines:" + resource.lines());
      //note this has been added to get compatibility with sonar 5.2
      if (resource.lines() >= func.getBodyLine() && resource.lines() != -1 && func.getBodyLine() != -1) {
        NewIssue newIssue = sensorContext.newIssue().forRule(RULE_KEY_METHOD_CYCLOMATIC_COMPLEXITY);
        newIssue.newLocation()
                .on(resource).at(resource.selectLine(func.getBodyLine()))
                .message(String.format("The Cyclomatic Complexity of this method \"%s\" is %d which is greater than %d authorized."));
        
        newIssue.save();        
      } else {
        NewIssue newIssue = sensorContext.newIssue().forRule(RULE_KEY_METHOD_CYCLOMATIC_COMPLEXITY);
        newIssue.newLocation()
                .on(resource).at(resource.selectLine(1))
                .message(String.format("The Cyclomatic Complexity of this method \"%s\" is %d which is greater than %d authorized."));
        
        newIssue.save();  
      }
    }
  }
}
