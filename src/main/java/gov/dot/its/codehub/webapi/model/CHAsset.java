package gov.dot.its.codehub.webapi.model;

public class CHAsset {
	private String id;
	private String name;
	private String label;
	private long size;
	private long downloadCount;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getDownloadCount() {
		return downloadCount;
	}
	public void setDownloadCount(long downloadCount) {
		this.downloadCount = downloadCount;
	}


}
