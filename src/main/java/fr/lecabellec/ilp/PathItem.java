package fr.lecabellec.ilp;

import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;

import fr.lecabellec.ilp.FileSystemCalatog.EnumPathItemType;

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
      this.sha256 = FileSystemCalatog.getSha256FromPath(parent.pathBeyondParent + pathBeyondParent);
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

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("PathItem [itemType=").append(this.itemType).append(", parentSha256=")
        .append(this.parentSha256).append(", pathBeyondParent=").append(this.pathBeyondParent)
        .append(", sha256=").append(this.sha256).append("]");
    return builder.toString();
  }

}