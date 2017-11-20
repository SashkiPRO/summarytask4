package ua.nure.botsula.st4.db.entity;


/**
 * Course state entity.
 * 
 * @author A.Botsula
 * 
 */
public enum CourseState {

 RECRUITED, DURING, FINISHED;

	public String getName() {
		return name().toLowerCase();
	}

	public static CourseState getCourseState(int requestState) {
		return CourseState.values()[requestState];
	}
	
}
