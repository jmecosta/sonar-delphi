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
package org.sonar.plugins.delphi.core.helpers;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;

import org.sonar.plugins.delphi.DelphiPlugin;
import org.sonar.plugins.delphi.core.DelphiLanguage;
import org.sonar.plugins.delphi.project.DelphiProject;
import org.sonar.plugins.delphi.project.DelphiWorkgroup;
import org.sonar.plugins.delphi.utils.DelphiUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.sonar.api.batch.ScannerSide;
import org.sonar.api.batch.sensor.SensorContext;

/**
 * Class that helps get the maven/ant configuration from .xml file
 */
@ScannerSide
public class DelphiProjectHelper {

  public static final String DEFAULT_PACKAGE_NAME = "[default]";

  private final FileSystem fs;

  /**
   * ctor used by Sonar
   *
   * @param settings Project settings
   * @param fs Sonar FileSystem
   */
  public DelphiProjectHelper(FileSystem fs) {
    this.fs = fs;
    DelphiUtils.LOG.info("Delphi Project Helper creation!!!");
  }

  /**
   * Should includes be copy-pasted to a file which tries to include them
   *
   * @param context
   * @return True if so, false otherwise
   */
  public boolean shouldExtendIncludes(SensorContext context) {
    String str = context.config().get(DelphiPlugin.INCLUDE_EXTEND_KEY).orElse("true");
    return "true".equals(str);
  }

  /**
   * Gets the include directories (directories that are looked for include
   * files)
   *
   * @param context
   * @return List of include directories
   */
  public List<File> getIncludeDirectories(SensorContext context) {
    List<File> result = new ArrayList<>();

    String[] includedDirs = context.config().getStringArray(DelphiPlugin.INCLUDED_DIRECTORIES_KEY);
    if (includedDirs != null && includedDirs.length > 0) {
      for (String path : includedDirs) {
        if (StringUtils.isEmpty(path)) {
          continue;
        }
        File included = DelphiUtils.resolveAbsolutePath(fs.baseDir().getAbsolutePath(), path.trim());
        if (!included.exists()) {
          DelphiUtils.LOG.warn("Include directory does not exist: " + included.getAbsolutePath());
        } else if (!included.isDirectory()) {
          DelphiUtils.LOG.warn("Include path is not a directory: " + included.getAbsolutePath());
        } else {
          result.add(included);
        }
      }
    } else {
      DelphiUtils.LOG.info("No include directories found in project configuration.");
    }
    return result;
  }

  /**
   * Gets the list of excluded source files and directories
   *
   * @param context
   * @return List of excluded source files and directories
   */
  public List<File> getExcludedSources(SensorContext context) {
    return detectExcludedSources(context);
  }

  private List<File> detectExcludedSources(SensorContext context) {
    List<File> result = new ArrayList<>();

    String[] excludedNames = context.config().getStringArray(DelphiPlugin.EXCLUDED_DIRECTORIES_KEY);
    if (excludedNames != null && excludedNames.length > 0) {
      for (String path : excludedNames) {
        if (StringUtils.isEmpty(path)) {
          continue;
        }
        File excluded = DelphiUtils.resolveAbsolutePath(fs.baseDir().getAbsolutePath(), path.trim());
        result.add(excluded);
        if (!excluded.exists()) {
          DelphiUtils.LOG.warn("Exclude directory does not exist: " + excluded.getAbsolutePath());
        }
      }
    } else {
      DelphiUtils.LOG.info("No exclude directories found in project configuration.");
    }
    return result;
  }

  /**
   * Gets the project file (.dproj)
   *
   * @param ctx
   * @return Path to project file
   */
  public String getProjectFile(SensorContext ctx) {
    return ctx.config().get(DelphiPlugin.PROJECT_FILE_KEY).orElse("");
  }

  /**
   * Gets the workgroup (.groupproj) file
   *
   * @param ctx
   * @return Path to workgroup file
   */
  public String getWorkgroupFile(SensorContext ctx) {
    return ctx.config().get(DelphiPlugin.WORKGROUP_FILE_KEY).orElse("");
  }

  /**
   * Create list of DelphiLanguage projects in a current workspace
   *
   * @param ctx
   * @return List of DelphiLanguage projects
   */
  public List<DelphiProject> getWorkgroupProjects(SensorContext ctx) {
    List<DelphiProject> list = new ArrayList<>();

    String dprojPath = getProjectFile(ctx);
    String gprojPath = getWorkgroupFile(ctx);

    // Single workgroup file, containing list of .dproj files
    if (!StringUtils.isEmpty(gprojPath)) {
      try {
        DelphiUtils.LOG.debug(".groupproj file found: " + gprojPath);
        DelphiWorkgroup workGroup = new DelphiWorkgroup(new File(gprojPath));
        for (DelphiProject newProject : workGroup.getProjects()) {
          list.add(newProject);
        }
      } catch (IOException e) {
        DelphiUtils.LOG.error(e.getMessage());
        DelphiUtils.LOG.error("Skipping .groupproj reading, default configuration assumed.");
        DelphiProject newProject = new DelphiProject("Default Project");
        newProject.setIncludeDirectories(getIncludeDirectories(ctx));
        newProject.setSourceFiles(mainFiles());
        list.clear();
        list.add(newProject);
      }
    } // Single .dproj file
    else if (!StringUtils.isEmpty(dprojPath)) {
      File dprojFile = DelphiUtils.resolveAbsolutePath(fs.baseDir().getAbsolutePath(), dprojPath);
      DelphiUtils.LOG.info(".dproj file found: " + gprojPath);
      DelphiProject newProject = new DelphiProject(dprojFile);
      list.add(newProject);
    } else {
      // No .dproj files, create default project
      DelphiProject newProject = new DelphiProject("Default Project");
      newProject.setIncludeDirectories(getIncludeDirectories(ctx));
      newProject.setSourceFiles(mainFiles());
      list.add(newProject);
    }

    return list;
  }

  public List<InputFile> mainFiles() {
    FilePredicates p = fs.predicates();
    Iterable<InputFile> inputFiles = fs.inputFiles(p.and(p.hasLanguage(DelphiLanguage.KEY),
            p.hasType(InputFile.Type.MAIN)));
    return Lists.newArrayList(inputFiles);
  }

  public List<InputFile> testFiles() {
    FilePredicates p = fs.predicates();
    Iterable<InputFile> inputFiles = fs.inputFiles(p.and(p.hasLanguage(DelphiLanguage.KEY), p.hasType(InputFile.Type.TEST)));
    return Lists.newArrayList(inputFiles);
  }

  public boolean shouldExecuteOnProject() {
    return fs.hasFiles(fs.predicates().hasLanguage(DelphiLanguage.KEY));
  }

  public InputFile getFile(String path) {
    return getFile(new File(path));
  }

  public InputFile getFile(File file) {
    return fs.inputFile(fs.predicates().is(file));
  }

  public InputFile findFileInDirectories(String fileName) throws FileNotFoundException {
    for (InputFile inputFile : mainFiles()) {
      if (inputFile.filename().equalsIgnoreCase(fileName)) {
        return inputFile;
      }
    }

    throw new FileNotFoundException(fileName);
  }

  public InputFile findTestFileInDirectories(String fileName) throws FileNotFoundException {
    String unitFileName = normalize(fileName);
    for (InputFile inputFile : testFiles()) {
      if (inputFile.filename().equalsIgnoreCase(unitFileName)) {
        return inputFile;
      }
    }

    throw new FileNotFoundException(fileName);
  }

  private String normalize(String fileName) {
    if (!fileName.contains(".")) {
      return fileName + "." + DelphiLanguage.FILE_SOURCE_CODE_SUFFIX;
    }
    return fileName;
  }

  public File baseDir() {
    return this.fs.baseDir();
  }

  public File workDir() {
    return fs.workDir();
  }

  public String encoding() {
    return fs != null ? fs.encoding().name() : Charset.defaultCharset().name();
  }

  /**
   * Is file in excluded list?
   *
   * @param fileName File to check
   * @param excludedSources Excluded paths
   * @return True if file is excluded, false otherwise
   */
  public boolean isExcluded(String fileName, List<File> excludedSources) {
    if (excludedSources == null) {
      return false;
    }
    for (File excludedDir : excludedSources) {
      String normalizedFileName = DelphiUtils.normalizeFileName(fileName.toLowerCase());
      String excludedDirNormalizedPath = DelphiUtils.normalizeFileName(excludedDir.getAbsolutePath()
              .toLowerCase());
      if (normalizedFileName.startsWith(excludedDirNormalizedPath)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Is file excluded?
   *
   * @param delphiFile File to check
   * @param excludedSources List of excluded sources
   * @return True if file is excluded, false otherwise
   */
  public boolean isExcluded(File delphiFile, List<File> excludedSources) {
    return isExcluded(delphiFile.getAbsolutePath(), excludedSources);
  }

  /**
   * Gets code coverage excluded directories
   *
   * @return List of excluded directories, empty list if none
   */
  public List<File> getCodeCoverageExcludedDirectories(SensorContext ctx) {
    List<File> list = new ArrayList<>();

    String[] sources = ctx.config().getStringArray(DelphiPlugin.CC_EXCLUDED_KEY);
    if (sources == null || sources.length == 0) {
      return list;
    }
    for (String path : sources) {
      if (StringUtils.isEmpty(path)) {
        continue;
      }
      File excluded = DelphiUtils.resolveAbsolutePath(fs.baseDir().getAbsolutePath(), path.trim());
      if (!excluded.exists()) {
        DelphiUtils.LOG.warn("Excluded code coverage path does not exist: " + excluded.getAbsolutePath());
      } else if (!excluded.isDirectory()) {
        DelphiUtils.LOG.warn("Excluded code coverage path is not a directory: " + excluded.getAbsolutePath());
      } else {
        list.add(excluded);
      }
    }
    return list;
  }

}
