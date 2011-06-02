package net.sf.jasperreports.jsf.context;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentType implements Serializable, Comparable<ContentType> {

	private static final Pattern PATTERN = Pattern.compile("(.+)/(.+)");
	
	public static boolean isContentType(String value) {
		return PATTERN.matcher(value).matches();
	}
	
	private String type;
	
	private String subtype;
	
	public ContentType(String value) {
		Matcher matcher = PATTERN.matcher(value);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Value '" + value + 
					"' is not a valid MIME type.");
		}
		this.type    = matcher.group(0);
		this.subtype = matcher.group(1);
	}
	
	public int compareTo(ContentType o) {
		if (this.equals(o)) return 0;
		if (this.implies(o)) return 1;
		if (o.implies(this)) return -1;
		return 0;
	}

	public boolean implies(ContentType other) {
		if (null == other) return false;
		if (this == other) return true;
		if (equals(other)) return true;
		
		if (!type.equals("*") && (!type.equals(other.type))) {
			return false;
		}
		if (!subtype.equals("*") && (!subtype.equals(other.subtype))) {
			return false;
		}
		return true;
	}
	
	public int hashCode() {
		return (7 * type.hashCode()) + (11 * subtype.hashCode());
	}
	
	public boolean equals(Object o) {
		if (null == o) return false;
		if (this == o) return true;
		if (!(o instanceof ContentType)) return false;
		
		final ContentType other = (ContentType) o;
		return type.equals(other.type) 
				&& subtype.equals(other.subtype);
	}
	
	public String toString() {
		return (type + "/" + subtype);
	}
	
}
