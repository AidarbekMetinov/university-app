package ua.com.foxminded.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "faculty")
public class Faculty {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	@NotNull(message = "is required")
	@Size(min = 3, message = "Faculty name must be at least 3 characters")
	private String name;

	@Column(name = "description")
	@NotNull(message = "is required")
	@Size(min = 10, message = "Faculty description must be at least 10 characters")
	private String description;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
			CascadeType.REFRESH })
	@JoinColumn(name = "faculty_id")
	@JsonIgnore
	private List<Group> groups;

	public void add(Group group) {

		if (groups == null) {
			groups = new ArrayList<>();
		}
		groups.add(group);

		group.setFacultyId(id);
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
			CascadeType.REFRESH })
	@JoinTable(name = "faculty_teacher", joinColumns = @JoinColumn(name = "faculty_id"), inverseJoinColumns = @JoinColumn(name = "teacher_id"))
	@JsonIgnore
	private List<Teacher> teachers;

	public void add(Teacher teacher) {

		if (teachers == null) {
			teachers = new ArrayList<>();
		}
		teachers.add(teacher);
	}

	@Override
	public String toString() {
		return "Faculty: " + name;
	}
}