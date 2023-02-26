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

public class FileSystemCalatog implements Serializable, Comparable<FileSystemCalatog> {

  public enum EnumPathItemType {
    DIRECTORY, FILE, OTHER, OTHER_LEAF, OTHER_NODE;
  }

  public enum EnumResultState {
    FAILED, SUCCEED;
  }

  public class ExtendedPathItem extends PathItem implements Serializable {

    private static final long serialVersionUID = -154288367308892581L;
    public final long computedSize;
    public final BigInteger contentSha256;
    public final Date creationDate;
    public final Date modificationDate;

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

  public class PathItem implements Serializable, Comparable<PathItem> {

    private static final long serialVersionUID = 484943314649154887L;
    public final EnumPathItemType itemType;
    public final BigInteger parentSha256;
    public final String pathBeyondParent;

    public final BigInteger sha256;

    public PathItem(BigInteger sha256, BigInteger parentSha256, String pathBeyondParent,
        EnumPathItemType itemType) {
      super();
      this.sha256 = sha256;
      this.parentSha256 = parentSha256;
      this.pathBeyondParent = pathBeyondParent;
      this.itemType = itemType;
    }

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

    @Override
    public int compareTo(PathItem o) {
      if (o == null) {
        return Integer.MIN_VALUE;
      }
      return this.sha256.compareTo(o.sha256);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null && !PathItem.class.isAssignableFrom(obj.getClass())) {
        return false;
      }
      PathItem o = (PathItem) obj;
      return this.itemType == o.itemType && this.sha256.equals(o.sha256);
    }

  }

  public static final int CATALOG_EXECUTOR_THREADS = 3;
  public static MessageDigest md = null;

  public static Semaphore mdSemaphore = null;

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

  /**
   * Executor for async ops
   */
  protected ScheduledThreadPoolExecutor executor;
  /**
   * File for extendedPathItem storage
   */
  protected File extendedpathItemDataFile;
  /**
   * TreeSet for ExtendedPathItems
   */
  protected TreeSet<ExtendedPathItem> extendedPathItems;

  /**
   * File for mainFileData
   */
  protected File mainFile;

  /**
   * Info about this catalog
   */
  protected Properties mainFileData;

  /**
   * File for PathItem storage
   */
  protected File pathItemDataFile;
  /**
   * TreeSet for PathItem
   */
  protected TreeSet<PathItem> pathItems;

  /**
   * Sub catalogs when size of data exceed capacity of a single catalog.
   */
  protected TreeSet<FileSystemCalatog> subCatalogs;

  /**
   * Main files of sub catalogs relative to main file of this catalog
   */
  protected TreeSet<File> subCatalogsMainFiles;

  public Future<EnumResultState> addPathItem(PathItem pathItem) {
    return null;

  }

  @Override
  public int compareTo(FileSystemCalatog o) {
    if (o == null) {
      return Integer.MIN_VALUE;
    }
    return this.getMainFile().compareTo(o.getMainFile());
  }

  public File getMainFile() {
    return mainFile;
  }

  public TreeSet<FileSystemCalatog> getSubCatalogs() {
    return subCatalogs;
  }

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

  public boolean isReady() {
    return false;
  }

  public Future<EnumResultState> loadCatalog(File mainFile) {
    return null;

  }

  public Future<EnumResultState> saveCatalog() {
    return null;

  }

}
