package ua.com.foxminded.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "course")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	@NotNull(message = "is required")
	@Size(min = 3, message = "Course name must be at least 5 characters")
	private String name;

	@Column(name = "description")
	@NotNull(message = "is required")
	@Size(min = 10, message = "Course description must be at least 10 characters")
	private String description;

	@Override
	public String toString() {
		return "Course: " + name;
	}
}