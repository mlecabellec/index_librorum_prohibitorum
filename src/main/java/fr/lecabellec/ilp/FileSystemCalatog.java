package fr.lecabellec.ilp;

import java.io.File;
import java.io.IOException;
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
import java.util.logging.Level;
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

  /** The Constant CONFIG_KEY_BASE_CATALOG_DIR. */
  public static final String CONFIG_KEY_BASE_CATALOG_DIR = "baseCatalogDir";

  /** The Constant CONFIG_KEY_CATALOG_ID. */
  public static final String CONFIG_KEY_CATALOG_ID = "catalogId";

  /** The Constant CONSTANT_ID. */
  public static final String CONSTANT_ID = "id";

  /** The Constant LOG. */
  public static final Logger LOG = Logger.getLogger(FileSystemCalatog.class.getCanonicalName());

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
        LOG.severe("Ignoring InterruptedException");
      }
    } else {
      try {
        mdSemaphore.acquire();
      } catch (InterruptedException e) {
        LOG.severe("Ignoring InterruptedException");
      }
    }

    if (md == null) {
      try {
        md = MessageDigest.getInstance("SHA-256");
      } catch (NoSuchAlgorithmException e) {
        LOG
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

    Properties defaults = new Properties();
    defaults.putAll(Map.<String, String>of(CONFIG_KEY_CATALOG_ID,
        CONSTANT_ID + Math.abs(new Random().nextLong()), CONFIG_KEY_BASE_CATALOG_DIR,
        Paths.get("").toAbsolutePath().toString()));

    if (this.executor == null) {
      this.executor = new ScheduledThreadPoolExecutor(CATALOG_EXECUTOR_THREADS);
    }

    if (this.mainFileData == null) {
      this.mainFileData = new Properties();
      this.mainFileData.putAll(defaults);
      this.mainFileData.putAll(config);

    }

    if (!defaults.entrySet().stream().allMatch(e -> this.mainFileData.containsKey(e.getKey()))) {
      LOG.log(Level.SEVERE, "Failed to ensure correct configuration");
      return EnumResultState.FAILED;
    }

    if (this.mainFile == null) {
      File baseCatalogDirFile = new File(
          this.mainFileData.getProperty(CONFIG_KEY_BASE_CATALOG_DIR));
      if (!baseCatalogDirFile.isDirectory() || !baseCatalogDirFile.canWrite()) {
        LOG.log(Level.SEVERE,
            "CONFIG_KEY_BASE_CATALOG_DIR config is not good");
        return EnumResultState.FAILED;

      }

      try {
        this.mainFile = new File(this.mainFileData.getProperty(CONFIG_KEY_CATALOG_ID));
        if (!this.mainFile.exists() && !this.mainFile.createNewFile()) {
          LOG.log(Level.SEVERE, "Failed to create catalog main file.");
          return EnumResultState.FAILED;

        }
      } catch (IOException e1) {
        LOG.log(Level.SEVERE, "Failed to create file", e1);
        return EnumResultState.FAILED;

      }

    }

    return EnumResultState.SUCCEED;
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
