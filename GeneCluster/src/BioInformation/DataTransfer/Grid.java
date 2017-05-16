package BioInformation.DataTransfer;

import java.util.HashSet;
import java.util.Set;

public class Grid {
    private Set<Integer> ids = new HashSet<Integer>();
	public Set<Integer> getIds() {
		return ids;
	}
	public void setIds(Set<Integer> ids) {
		this.ids = ids;
	}
	public void add(int id){
		ids.add(id);
	}
}
