package gov.dot.its.codehub.webapi.model;

import java.util.ArrayList;
import java.util.List;

public class CHForks {
	private List<CHFork> forkedRepos;

	public CHForks() {
		this.forkedRepos = new ArrayList<>();
	}

	public List<CHFork> getForkedRepos() {
		return forkedRepos;
	}

	public void setForkedRepos(List<CHFork> forkedRepos) {
		this.forkedRepos = forkedRepos;
	}


}
