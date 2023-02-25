package index_librorum_prohibitorum;

import java.nio.file.Path;
import java.util.concurrent.ScheduledThreadPoolExecutor;

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
	 * 
	 * @author vortigern
	 *
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
		/**
		 * 
		 */
		CRAWLER_STATUS_DONE,
		/**
		 * 
		 */
		CRAWLER_STATUS_DONE_WITH_WARNINGS,
		/**
		 * 
		 */
		CRAWLER_STATUS_DONE_WITH_ERRORS,
		/**
		 * 
		 */
		CRAWLER_STATUS_FAILED;

	}

	/**
	 * Status of this crawler.
	 */
	protected EnumCrawlerStatus status;

	
	
	
	/**
	 * 
	 * @param startPath
	 * @param parentCrawler
	 */
	public Crawler(Path startPath, Crawler parentCrawler, ScheduledThreadPoolExecutor executor) {
		super();
		this.startPath = startPath;
		this.parentCrawler = parentCrawler;
		this.status = EnumCrawlerStatus.CRAWLER_STATUS_UNKNOWN;
		this.executor = executor;
	}

}
