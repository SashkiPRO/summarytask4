package ua.nure.botsula.st4.db.bean;

import ua.nure.botsula.st4.db.entity.Entity;

public class TeacherRequestBean extends Entity{

	/**
	 * Provide records for virtual table:
	 * 
	 *
	 * 
	 * @author A.Botsula
	 * 
	 */
public TeacherRequestBean(){
	super();
}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	private static final long serialVersionUID = 1L;

	private String firstName;
	private String lastName;
	private int courseId;
	private String courseName;
	private String email;
	
}
