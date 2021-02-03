package pbt.examples.projectmgt;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.*;

class ProjectTests {

	@Example
	void can_add_many_team_members_to_a_project() {
		Project project = new Project("Mein großes Projekt");

		var alex = new User("alex@example.com");
		var kim = new User("kim@example.com");
		var pat = new User("pat@example.com");
		project.addMember(alex);
		project.addMember(kim);
		project.addMember(pat);

		assertThat(project.isMember(alex)).isTrue();
		assertThat(project.isMember(kim)).isTrue();
		assertThat(project.isMember(pat)).isTrue();
	}
}
