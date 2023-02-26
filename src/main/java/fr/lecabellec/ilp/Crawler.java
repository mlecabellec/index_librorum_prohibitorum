package fr.lecabellec.ilp;

import java.nio.file.Path;
import java.util.concurrent.ScheduledThreadPoolExecutor;

// TODO: Auto-generated Javadoc
/**
 * The Class Crawler.
 */
public class Crawler {

  /**
   * Start path of this crawler.
   */
  protected final Path startPath;
  /**
   * Parent crawler. Shall reverse results to it at end.
   */
  protected final Crawler parentCrawler;

  /**
   * Used for multithread operations. May be shared with parent.
   */
  protected final ScheduledThreadPoolExecutor executor;

  /**
   * The Enum EnumCrawlerStatus.
   *
   * @author vortigern
   */
  public enum EnumCrawlerStatus {
    /**
     * Default.
     */
    CRAWLER_STATUS_UNKNOWN,
    /**
     * OK, can start.
     */
    CRAWLER_STATUS_READY,
    /**
     * Working hard.
     */
    CRAWLER_STATUS_RUNNING,

    /** The crawler status done. */
    CRAWLER_STATUS_DONE,

    /** The crawler status done with warnings. */
    CRAWLER_STATUS_DONE_WITH_WARNINGS,

    /** The crawler status done with errors. */
    CRAWLER_STATUS_DONE_WITH_ERRORS,

    /** The crawler status failed. */
    CRAWLER_STATUS_FAILED;

  }

  /**
   * Status of this crawler.
   */
  protected EnumCrawlerStatus status;

  /**
   * Instantiates a new crawler.
   *
   * @param startPath the start path
   * @param parentCrawler the parent crawler
   * @param executor the executor
   */
  public Crawler(Path startPath, Crawler parentCrawler, ScheduledThreadPoolExecutor executor) {
    super();
    this.startPath = startPath;
    this.parentCrawler = parentCrawler;
    this.status = EnumCrawlerStatus.CRAWLER_STATUS_UNKNOWN;
    this.executor = executor;
  }

}
