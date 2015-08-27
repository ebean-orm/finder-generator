package org.avaje.ebean.typequery.generator.write;

import org.avaje.ebean.typequery.generator.GeneratorConfig;
import org.avaje.ebean.typequery.generator.read.EntityBeanPropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Writes 'Finder' field into existing entity bean source code.
 */
public class SimpleFinderLinkWriter {

  protected static final Logger logger = LoggerFactory.getLogger(SimpleFinderLinkWriter.class);

  protected final GeneratorConfig config;

  protected final EntityBeanPropertyReader classMeta;

  protected final String entityBeanPackage;

  protected String finderPackage;

  protected String shortName;

  /**
   * Low tech, read all the source into this buffer and write it all out again.
   */
  protected List<String> existingSource = new ArrayList<>(300);

  /**
   * linux/unix new line but that is ok for now.
   */
  protected String newLine = "\n";

  public SimpleFinderLinkWriter(GeneratorConfig config, EntityBeanPropertyReader classMeta) {
    this.config = config;
    this.classMeta = classMeta;
    this.entityBeanPackage = config.getEntityBeanPackage();
    this.finderPackage = config.getDestFinderPackage();
    this.shortName = deriveShortName(classMeta.name);
  }

  /**
   * Write the find field into the entity bean if we don't think it is there.
   */
  public void write() throws IOException {

    File file = getEntitySourceFile();
    if (!file.exists()) {
      logger.warn("Could not find entity bean source java file {}", file.getAbsoluteFile());
      return;
    }

    String finderDefn = shortName + "Finder find = new " + shortName + "Finder();";

    if (checkForExistingFinder(file, finderDefn)) {
      logger.warn("Existing find field on entity {}", shortName);
      return;
    }

    String classDefn = "public class " + shortName + " ";

    FileWriter writer = new FileWriter(file, false);

    boolean addedImport = false;
    boolean addedField = false;

    // loop the existing source and write it again including the
    // import and the finder field
    for (String sourceLine : existingSource) {

      if (!addedImport && sourceLine.startsWith("import ")) {
        writer.append("import ").append(finderPackage).append(".").append(shortName).append("Finder;");
        writer.append(newLine);
        addedImport = true;
      }
      writer.append(sourceLine).append(newLine);
      if (!addedField && sourceLine.startsWith(classDefn)) {
        writer.append(newLine);
        writer.append("  public static final ").append(finderDefn).append(newLine);
        addedField = true;
      }
    }

    writer.flush();
    writer.close();
  }

  /**
   * Return true if the finder is already in the entity bean source file.
   */
  protected boolean checkForExistingFinder(File file, String finderDefn) throws IOException {
    FileReader reader = new FileReader(file);
    try {
      if (hasExistingFinder(reader, finderDefn)) {
        return true;
      }
    } finally {
      reader.close();
    }
    return false;
  }

  /**
   * Return true if the finder is already in the entity bean source reader.
   */
  protected boolean hasExistingFinder(Reader reader, String finderDefn) throws IOException {

    LineNumberReader lineReader = new LineNumberReader(reader);
    String line;
    while((line = lineReader.readLine()) != null) {
      if (line.contains(finderDefn)) {
        return true;
      }
      existingSource.add(line);
    }
    return false;
  }

  /**
   * Return the file for the entity bean source.
   */
  protected File getEntitySourceFile() throws IOException {

    String destDirectory = config.getDestDirectory();
    File destDir = new File(destDirectory);

    String packageAsDir = asSlashNotation(entityBeanPackage);

    File packageDir = new File(destDir, packageAsDir);

    String fileName = shortName + ".java";
    return new File(packageDir, fileName);
  }

  protected String asSlashNotation(String path) {
    return path.replace('.', '/');
  }

  protected String deriveShortName(String name) {
    int startPos = name.lastIndexOf('/');
    if (startPos == -1) {
      return name;
    }
    return name.substring(startPos + 1);
  }

}
