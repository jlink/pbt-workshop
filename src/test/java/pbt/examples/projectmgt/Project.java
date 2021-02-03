package pbt.examples.projectmgt;

import java.awt.*;
import java.util.*;

public class Project {

	private final String name;
	private final int maxMembers;
	private Set<User> members = new HashSet<>();

	public Project(String name) {
		this(name, 10);
	}

	public Project(String name, int maxMembers) {
		if (name.isBlank()) {
			throw new IllegalArgumentException("Project name must not be blank");
		}
		this.name = name;
		this.maxMembers = maxMembers;
	}

	public String getName() {
		return name;
	}

	public void addMember(User newMember) {
		if (emailKnown(newMember.getEmail())) {
			throw new IllegalArgumentException(String.format("Member with email [%s] already exists.", newMember.getEmail()));
		}
		if (members.size() >= maxMembers) {
			throw new IllegalArgumentException(String.format("Maximum number of %s members already reached", maxMembers));
		}
		members.add(newMember);
	}

	public boolean isMember(User user) {
		return members.contains(user);
	}

	private boolean emailKnown(String email) {
		return members.stream().anyMatch(m -> m.getEmail().equals(email));
	}
}
