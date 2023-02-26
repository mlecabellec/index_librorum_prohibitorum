package fr.lecabellec.ilp;

import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class FileSystemCalatog.
 */
public class FileSystemCalatog implements Serializable, Comparable<FileSystemCalatog> {

  /**
   * The Enum EnumPathItemType.
   */
  public enum EnumPathItemType {

    /** The directory. */
    DIRECTORY,
    /** The file. */
    FILE,
    /** The other. */
    OTHER,
    /** The other leaf. */
    OTHER_LEAF,
    /** The other node. */
    OTHER_NODE;
  }

  /**
   * The Enum EnumResultState.
   */
  public enum EnumResultState {

    /** The failed. */
    FAILED,
    /** The succeed. */
    SUCCEED;
  }

  /** The Constant CATALOG_EXECUTOR_THREADS. */
  public static final int CATALOG_EXECUTOR_THREADS = 3;

  /** The md. */
  public static MessageDigest md = null;

  /** The md semaphore. */
  public static Semaphore mdSemaphore = null;

  /**
   * Gets the sha 256 from path.
   *
   * @param path the path
   * @return the sha 256 from path
   */
  public static BigInteger getSha256FromPath(String path) {
    if (mdSemaphore == null) {
      mdSemaphore = new Semaphore(1);
      try {
        mdSemaphore.acquire();
      } catch (InterruptedException e) {
        Logger.getAnonymousLogger().severe("Ignoring InterruptedException");
      }
    } else {
      try {
        mdSemaphore.acquire();
      } catch (InterruptedException e) {
        Logger.getAnonymousLogger().severe("Ignoring InterruptedException");
      }
    }

    if (md == null) {
      try {
        md = MessageDigest.getInstance("SHA-256");
      } catch (NoSuchAlgorithmException e) {
        Logger.getAnonymousLogger()
            .severe("MessageDigest.getInstance(\"SHA-256\") got a NoSuchAlgorithmException !!!");
        Runtime.getRuntime().exit(666);
      }
    }

    byte[] digest = md.digest(path.getBytes());
    md.reset();
    mdSemaphore.release();
    return new BigInteger(digest);

  }

  /** Executor for async ops. */
  protected ScheduledThreadPoolExecutor executor;

  /** File for extendedPathItem storage. */
  protected File extendedpathItemDataFile;

  /** TreeSet for ExtendedPathItems. */
  protected TreeSet<ExtendedPathItem> extendedPathItems;

  /** File for mainFileData. */
  protected File mainFile;

  /** Info about this catalog. */
  protected Properties mainFileData;

  /** File for PathItem storage. */
  protected File pathItemDataFile;

  /** TreeSet for PathItem. */
  protected TreeSet<PathItem> pathItems;

  /**
   * Sub catalogs when size of data exceed capacity of a single catalog.
   */
  protected TreeSet<FileSystemCalatog> subCatalogs;

  /** Main files of sub catalogs relative to main file of this catalog. */
  protected TreeSet<File> subCatalogsMainFiles;

  /**
   * Adds the path item.
   *
   * @param pathItem the path item
   * @return the future
   */
  public Future<EnumResultState> addPathItem(PathItem pathItem) {
    return null;

  }

  /**
   * Compare to.
   *
   * @param o the o
   * @return the int
   */
  @Override
  public int compareTo(FileSystemCalatog o) {
    if (o == null) {
      return Integer.MIN_VALUE;
    }
    return this.getMainFile().compareTo(o.getMainFile());
  }

  /**
   * Gets the main file.
   *
   * @return the main file
   */
  public File getMainFile() {
    return mainFile;
  }

  /**
   * Gets the sub catalogs.
   *
   * @return the sub catalogs
   */
  public TreeSet<FileSystemCalatog> getSubCatalogs() {
    return subCatalogs;
  }

  /**
   * Inits the.
   *
   * @param config the config
   * @return the enum result state
   */
  public EnumResultState init(Properties config) {
    if (this.executor == null) {
      this.executor = new ScheduledThreadPoolExecutor(CATALOG_EXECUTOR_THREADS);
    }

    if (this.mainFileData == null) {
      Properties defaults = new Properties();
      defaults.putAll(Map.<String, String>of("ID", "id" + Math.abs(new Random().nextLong()),
          "basedir", Paths.get("").toAbsolutePath().toString()));
      this.mainFileData = new Properties();
      this.mainFileData.putAll(defaults);
      this.mainFileData.putAll(config);

    }

    if (this.mainFile == null) {
    }

    return EnumResultState.FAILED;
  }

  /**
   * Checks if is ready.
   *
   * @return true, if is ready
   */
  public boolean isReady() {
    return false;
  }

  /**
   * Load catalog.
   *
   * @param mainFile the main file
   * @return the future
   */
  public Future<EnumResultState> loadCatalog(File mainFile) {
    return null;

  }

  /**
   * Save catalog.
   *
   * @return the future
   */
  public Future<EnumResultState> saveCatalog() {
    return null;

  }

}
