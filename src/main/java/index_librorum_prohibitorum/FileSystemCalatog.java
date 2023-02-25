package index_librorum_prohibitorum;

import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

public class FileSystemCalatog implements Serializable, Comparable<FileSystemCalatog>{
	
	/**
	 * 
	 */
	protected File mainFile;
	
	protected File pathIndexFile;

	public enum EnumPathItemType {
		FILE,
		DIRECTORY,
		OTHER_LEAF,
		OTHER_NODE,
		OTHER;
	}	
	
	
	public static MessageDigest md = null;
	public static Semaphore mdSemaphore = null;	
	
	
	public static BigInteger getSha256FromPath(String path)
	{
		if(mdSemaphore == null)
		{
			mdSemaphore = new Semaphore(1);
			try {
				mdSemaphore.acquire();
			} catch (InterruptedException e) {
				Logger.getAnonymousLogger().severe("Ignoring InterruptedException");
			}
		}else
		{
			try {
				mdSemaphore.acquire();
			} catch (InterruptedException e) {
				Logger.getAnonymousLogger().severe("Ignoring InterruptedException");
			}
		}
		
		if(md == null)
		{
			try {
				md = MessageDigest.getInstance("SHA-256");
			} catch (NoSuchAlgorithmException e) {
				Logger.getAnonymousLogger().severe("MessageDigest.getInstance(\"SHA-256\") got a NoSuchAlgorithmException !!!");
				Runtime.getRuntime().exit(666);
			}
		}
		
		byte[] digest = md.digest(path.getBytes());
		md.reset();
		mdSemaphore.release();
		return new BigInteger(digest);
		
	}
	
	public class PathItem implements Serializable,Comparable<PathItem>
	{

		
		public final BigInteger sha256 ;
		public final BigInteger parentSha256; 
		public final String pathBeyondParent;
		
		public final EnumPathItemType itemType ;
	

		public PathItem(BigInteger sha256, BigInteger parentSha256, String pathBeyondParent, EnumPathItemType itemType) {
			super();
			this.sha256 = sha256;
			this.parentSha256 = parentSha256;
			this.pathBeyondParent = pathBeyondParent;
			this.itemType = itemType;
		}
		
		public PathItem(PathItem parent,String pathBeyondParent, EnumPathItemType itemType)
		{
			this.parentSha256 = parent.sha256 ;
			this.pathBeyondParent = pathBeyondParent ;
			this.itemType = itemType;
			if(parent.pathBeyondParent.endsWith(File.pathSeparator) )
			{
				this.sha256 = FileSystemCalatog.getSha256FromPath(parent.pathBeyondParent + pathBeyondParent);
			}
			else
			{
				this.sha256 = FileSystemCalatog.getSha256FromPath(parent.pathBeyondParent + File.pathSeparator + pathBeyondParent);
			}
				
		}

		@Override
		public int compareTo(PathItem o) {
			if(o == null)
			{
				return Integer.MIN_VALUE;
			}
			return this.sha256.compareTo(o.sha256);
		}

		@Override
		public boolean equals(Object obj) {
			if(obj == null && !PathItem.class.isAssignableFrom(obj.getClass()))
			{
				return false ;
			}
			PathItem o = (PathItem) obj ;
			return this.itemType == o.itemType &&  this.sha256.equals(o.sha256) ;
		}
		
		
		
		
		
	}
	
	protected TreeSet<PathItem> pathItems;
	
	
	protected TreeSet<FileSystemCalatog> subCatalogs;

	@Override
	public int compareTo(FileSystemCalatog o) {
		if(o == null)
		{
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
	



}
