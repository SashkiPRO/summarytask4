package ua.nure.botsula.st4.db.entity;

/**
 * Role entity.
 * 
 * @author A.Botsula
 * 
 */
public enum Role {
	ADMIN, TEACHER, STUDENT, BUNNED;

	public static Role getRole(User user) {
		int roleId = user.getRoleid();
		return Role.values()[roleId];
	}

	public String getName() {
		return name().toLowerCase();
	}
}
