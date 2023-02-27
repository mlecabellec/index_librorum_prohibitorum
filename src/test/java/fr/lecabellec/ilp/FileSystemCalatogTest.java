package fr.lecabellec.ilp;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.Properties;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import fr.lecabellec.ilp.FileSystemCalatog.EnumPathItemType;
import fr.lecabellec.ilp.FileSystemCalatog.EnumResultState;

/**
 * The Class FileSystemCalatogTest.
 */
class FileSystemCalatogTest {

  /** The Constant LOG. */
  public static final Logger LOG = Logger.getLogger(FileSystemCalatogTest.class.getCanonicalName());
  
  @Test
  void testSomeIdeasAboutSerialization() {
    LOG.info("Entering testSomeIdeasAboutSerialization...");
    TreeSet<PathItem> t1 = new TreeSet<>();
    PathItem p1 = new PathItem(BigInteger.ONE, BigInteger.TEN, "/", EnumPathItemType.DIRECTORY);
    t1.add(p1);

    File f1;
    try {
      f1 = File.createTempFile("testSomeIdeas", "test1");
    } catch (IOException e) {
      fail("Error when creating temp file", e);
      e.printStackTrace();
      return;
    }

    try (FileOutputStream fos = new FileOutputStream(f1);
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
      oos.writeObject(t1);
      oos.flush();
    } catch (IOException e) {
      fail("Error when writng to file", e);
      e.printStackTrace();
      return;
    }

    assertTrue(f1.exists() && f1.isFile() && f1.length() > 0, "Having a file");

    try (FileInputStream fis = new FileInputStream(f1);
        ObjectInputStream ois = new ObjectInputStream(fis)) {
      TreeSet<PathItem> t2;
      Object o1 = ois.readObject();
      assertTrue(TreeSet.class.isAssignableFrom(o1.getClass()), "Having a TreeSet");
      t2 = (TreeSet<PathItem>) o1;

      assertTrue(t2.size() == 1, "t2 has a PathItem");
      assertTrue(t2.first().sha256.equals(BigInteger.ONE), "PathItem matches");
      PathItem p2 = t2.first();
      Logger.getAnonymousLogger().log(Level.INFO, "p1 = {0}", p1);
      Logger.getAnonymousLogger().log(Level.INFO, "p2 = {0}", p2);

    } catch (ClassNotFoundException | IOException e) {
      fail("Error when reading to file", e);
      e.printStackTrace();
      return;
    }

  }

  @Test
  void testGetSha256FromPath() {
    LOG.info("Entering testGetSha256FromPath...");
    BigInteger sha1 = FileSystemCalatog.getSha256FromPath("aaaaa");
    BigInteger sha2 = FileSystemCalatog.getSha256FromPath("aaaaa");
    BigInteger sha3 = FileSystemCalatog.getSha256FromPath("aaaab");
    assertTrue(sha1 != null, "sha1 is not null");
    assertTrue(sha2 != null, "sha2 is not null");
    assertTrue(sha3 != null, "sha3 is not null");
    assertTrue(sha1 != BigInteger.ZERO, "sha1 is not zero");
    assertTrue(sha2 != BigInteger.ZERO, "sha2 is not zero");
    Logger.getAnonymousLogger().log(Level.INFO, "sha1: {0}", sha1.toString(16));
    Logger.getAnonymousLogger().log(Level.INFO, "sha2: {0}", sha2.toString(16));
    Logger.getAnonymousLogger().log(Level.INFO, "sha3: {0}", sha3.toString(16));
    assertTrue(sha1.equals(sha2), "sha1 == sha2");
    assertTrue(!sha2.equals(sha3), "sha2 != sha3");

  }

  /**
   * Test add path item.
   */
  @Disabled
  @Test
  void testAddPathItem() {
    fail("Not yet implemented");
  }

  @Disabled
  @Test
  void testCompareTo() {
    fail("Not yet implemented");
  }

  @Disabled
  @Test
  void testGetMainFile() {
    fail("Not yet implemented");
  }

  @Disabled
  @Test
  void testGetSubCatalogs() {
    fail("Not yet implemented");
  }

  @Test
  void testInit() {
    LOG.info("Entering testInit...");
    FileSystemCalatog fsc = new FileSystemCalatog();
    assertTrue(fsc.init(new Properties()) == EnumResultState.SUCCEED, "Init with empty props");
    assertTrue(fsc.getMainFile().exists(), "Main file exists");
    assertTrue(fsc.getPathItemDataFile().exists(), "PathItem file exists");
    assertTrue(fsc.getExtendedPathItemDataFile().exists(), "ExtendedPathItem file exists");
    
    fsc.getMainFile().delete();
    fsc.getPathItemDataFile().delete();
    fsc.getExtendedPathItemDataFile().delete();
    
  }

  @Disabled
  @Test
  void testIsReady() {
    fail("Not yet implemented");
  }
  
  @Disabled
  @Test
  void testLoadCatalog() {
    fail("Not yet implemented");
  }

  @Disabled
  @Test
  void testSaveCatalog() {
    fail("Not yet implemented");
  }

}
