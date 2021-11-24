package pbt.examples.projectmgt;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.*;

@Label("In a project")
class ProjectTests {

	@Example
	void you_can_add_many_team_members() {
		Project project = new Project("My big project");

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
