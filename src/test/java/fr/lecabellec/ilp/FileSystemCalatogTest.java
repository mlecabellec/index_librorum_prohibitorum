package fr.lecabellec.ilp;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;


class FileSystemCalatogTest {

  @Test
  void testGetSha256FromPath() {
    
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

  @Test
  void testAddPathItem() {
    fail("Not yet implemented");
  }

  @Test
  void testCompareTo() {
    fail("Not yet implemented");
  }

  @Test
  void testGetMainFile() {
    fail("Not yet implemented");
  }

  @Test
  void testGetSubCatalogs() {
    fail("Not yet implemented");
  }

  @Test
  void testInit() {
    fail("Not yet implemented");
  }

  @Test
  void testIsReady() {
    fail("Not yet implemented");
  }

  @Test
  void testLoadCatalog() {
    fail("Not yet implemented");
  }

  @Test
  void testSaveCatalog() {
    fail("Not yet implemented");
  }

}
