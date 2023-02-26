package fr.lecabellec.ilp;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import fr.lecabellec.ilp.FileSystemCalatog.EnumPathItemType;

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

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("ExtendedPathItem [computedSize=").append(this.computedSize)
        .append(", contentSha256=").append(this.contentSha256).append(", creationDate=")
        .append(this.creationDate).append(", modificationDate=").append(this.modificationDate)
        .append(", itemType=").append(this.itemType).append(", parentSha256=")
        .append(this.parentSha256).append(", pathBeyondParent=").append(this.pathBeyondParent)
        .append(", sha256=").append(this.sha256).append("]");
    return builder.toString();
  }

}