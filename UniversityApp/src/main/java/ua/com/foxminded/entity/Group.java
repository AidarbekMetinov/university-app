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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ugroup")
public class Group {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	@NotNull(message = "is required")
	@Size(min = 2, message = "there must be at least 2 characters")
	private String name;

	@Column(name = "faculty_id")
	@NotNull(message = "is required")
	@Min(value = 1, message = "Choose faculty")
	private Integer facultyId;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
	@JoinColumn(name = "group_id")
	private List<Student> students;

	public void add(Student student) {

		if (students == null) {
			students = new ArrayList<>();
		}
		students.add(student);

		student.setGroupId(id);
	}

	@Override
	public String toString() {
		return "Group: " + name;
	}
}