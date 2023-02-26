package fr.lecabellec.ilp;

import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
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

  /**
   * The Class ExtendedPathItem.
   */
  public class ExtendedPathItem extends PathItem implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -154288367308892581L;

    /** The computed size. */
    public final long computedSize;

    /** The content sha 256. */
    public final BigInteger contentSha256;

    /** The creation date. */
    public final Date creationDate;

    /** The modification date. */
    public final Date modificationDate;

    /**
     * Instantiates a new extended path item.
     *
     * @param sha256 the sha 256
     * @param parentSha256 the parent sha 256
     * @param pathBeyondParent the path beyond parent
     * @param itemType the item type
     * @param creationDate the creation date
     * @param modificationDate the modification date
     * @param computedSize the computed size
     * @param contentSha256 the content sha 256
     */
    public ExtendedPathItem(BigInteger sha256, BigInteger parentSha256, String pathBeyondParent,
        EnumPathItemType itemType, Date creationDate, Date modificationDate, long computedSize,
        BigInteger contentSha256) {
      super(sha256, parentSha256, pathBeyondParent, itemType);

      this.creationDate = creationDate;
      this.modificationDate = modificationDate;
      this.computedSize = computedSize;
      this.contentSha256 = contentSha256;
      // TODO Auto-generated constructor stub
    }

  }

  /**
   * The Class PathItem.
   */
  public class PathItem implements Serializable, Comparable<PathItem> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 484943314649154887L;

    /** The item type. */
    public final EnumPathItemType itemType;

    /** The parent sha 256. */
    public final BigInteger parentSha256;

    /** The path beyond parent. */
    public final String pathBeyondParent;

    /** The sha 256. */
    public final BigInteger sha256;

    /**
     * Instantiates a new path item.
     *
     * @param sha256 the sha 256
     * @param parentSha256 the parent sha 256
     * @param pathBeyondParent the path beyond parent
     * @param itemType the item type
     */
    public PathItem(BigInteger sha256, BigInteger parentSha256, String pathBeyondParent,
        EnumPathItemType itemType) {
      super();
      this.sha256 = sha256;
      this.parentSha256 = parentSha256;
      this.pathBeyondParent = pathBeyondParent;
      this.itemType = itemType;
    }

    /**
     * Instantiates a new path item.
     *
     * @param parent the parent
     * @param pathBeyondParent the path beyond parent
     * @param itemType the item type
     */
    public PathItem(PathItem parent, String pathBeyondParent, EnumPathItemType itemType) {
      this.parentSha256 = parent.sha256;
      this.pathBeyondParent = pathBeyondParent;
      this.itemType = itemType;
      if (parent.pathBeyondParent.endsWith(File.pathSeparator)) {
        this.sha256 = FileSystemCalatog
            .getSha256FromPath(parent.pathBeyondParent + pathBeyondParent);
      } else {
        this.sha256 = FileSystemCalatog
            .getSha256FromPath(parent.pathBeyondParent + File.pathSeparator + pathBeyondParent);
      }

    }

    /**
     * Compare to.
     *
     * @param o the o
     * @return the int
     */
    @Override
    public int compareTo(PathItem o) {
      if (o == null) {
        return Integer.MIN_VALUE;
      }
      return this.sha256.compareTo(o.sha256);
    }

    /**
     * Equals.
     *
     * @param obj the obj
     * @return true, if successful
     */
    @Override
    public boolean equals(Object obj) {
      if (obj == null && !PathItem.class.isAssignableFrom(obj.getClass())) {
        return false;
      }
      PathItem o = (PathItem) obj;
      return this.itemType == o.itemType && this.sha256.equals(o.sha256);
    }

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
