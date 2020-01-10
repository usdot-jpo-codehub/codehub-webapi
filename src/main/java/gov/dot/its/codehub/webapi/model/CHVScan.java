package gov.dot.its.codehub.webapi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"squid:S00116","squid:S00100","squid:S00117"})
public class CHVScan {
	private String data_scanned;
	private long infected_files;
	private Date lastscan;
	private long scanned_directories;
	private long scanned_files;
	private String time;
	private List<CHReportedFiles> reported_files;

	public CHVScan() {
		this.reported_files = new ArrayList<>();
	}

	public String getData_scanned() {
		return data_scanned;
	}
	public void setData_scanned(String data_scanned) {
		this.data_scanned = data_scanned;
	}
	public long getInfected_files() {
		return infected_files;
	}
	public void setInfected_files(long infected_files) {
		this.infected_files = infected_files;
	}
	public Date getLastscan() {
		return lastscan;
	}
	public void setLastscan(Date lastscan) {
		this.lastscan = lastscan;
	}
	public long getScanned_directories() {
		return scanned_directories;
	}
	public void setScanned_directories(long scanned_directories) {
		this.scanned_directories = scanned_directories;
	}
	public long getScanned_files() {
		return scanned_files;
	}
	public void setScanned_files(long scanned_files) {
		this.scanned_files = scanned_files;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<CHReportedFiles> getReported_files() {
		return reported_files;
	}
	public void setReported_files(List<CHReportedFiles> reported_files) {
		this.reported_files = reported_files;
	}


}
