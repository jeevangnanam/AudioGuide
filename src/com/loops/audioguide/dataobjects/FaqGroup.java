package com.loops.audioguide.dataobjects;

import java.io.Serializable;

public class FaqGroup {

	private String catID;
	private String CatName;
	
	
	public FaqGroup(){	}
	
	public String getCatID() {
		return catID;
	}
	public String getCatName() {
		return CatName;
	}
	public void setCatID(String catID) {
		this.catID = catID;
	}
	public void setCatName(String catName) {
		CatName = catName;
	}

}
