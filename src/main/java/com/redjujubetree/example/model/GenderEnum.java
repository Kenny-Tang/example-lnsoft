package com.redjujubetree.example.model;

public enum GenderEnum {
	MAN(1, "男"), WOMAN(0, "女");
	private int gender;
	private String description;
	GenderEnum(int gender, String description) {
		this.gender = gender;
		this.description = description;
	}
	public static GenderEnum fromGender(int gender) {
		for (GenderEnum value : GenderEnum.values()) {
			if (value.gender == gender) {
				return value;
			}
		}
		return null;
	}
	public int getGender() {
		return gender;
	}
	public String getDescription() {
		return description;
	}
}
