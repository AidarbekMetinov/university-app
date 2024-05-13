package ua.com.foxminded.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.foxminded.validation.ValidEmail;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "first_name")
	@NotNull(message = "is required")
	@Size(min = 2, message = "Write at least 2 characters")
	private String firstName;

	@Column(name = "last_name")
	@NotNull(message = "is required")
	@Size(min = 2, message = "Write at least 2 characters")
	private String lastName;

	@Column(name = "email")
	@ValidEmail
	@NotNull(message = "is required")
	private String email;

	@Column(name = "group_id")
	@NotNull(message = "is required")
	@Size(min = 1, message = "is required")
	@JsonIgnore
	private Integer groupId;

	@Override
	public String toString() {
		return "Student: " + firstName + " " + lastName;
	}
}
