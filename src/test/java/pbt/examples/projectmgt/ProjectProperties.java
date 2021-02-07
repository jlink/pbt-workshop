package pbt.examples.projectmgt;

import java.util.*;
import java.util.stream.*;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import net.jqwik.web.api.*;

class ProjectProperties {

	@Property
	void can_add_up_to_10_team_members_to_a_project(
			@ForAll @NotEmpty @AlphaChars @NumericChars String projectName,
			@ForAll @Size(max = 10) @UniqueElements List<@Email String> emails
	) {
		Project project = new Project(projectName);

		List<User> users = emails.stream().map(User::new).collect(Collectors.toList());

		for (User user : users) {
			project.addMember(user);
		}

		for (User user : users) {
			project.isMember(user);
		}
	}

	@Property(tries = 100)
	void can_add_team_members_up_to_specified_limit(@ForAll("members") List<User> users) {
		Project project = new Project("projectName", users.size());

		for (User user : users) {
			project.addMember(user);
		}

		for (User user : users) {
			project.isMember(user);
		}
	}

	@Provide
	Arbitrary<List<User>> members() {
		return Web.emails().map(User::new).list().ofMaxSize(50).uniqueElements();
	}
}
