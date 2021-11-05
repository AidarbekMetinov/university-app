package ua.com.foxminded.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "audience")
public class Audience {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "audience_n")
	@NotNull(message = "is required")
	@Min(value = 1, message = "N1 - N999")
	@Max(value = 999, message = "N1 - N999")
	private Integer audienceNumber;

	@Column(name = "seat")
	@NotNull(message = "is required")
	@Min(value = 10, message = "required at least 10 seats")
	@Max(value = 200, message = "max 200 seats")
	private Integer seats;

	@Column(name = "has_projector")
	private boolean hasProjector;

	@Override
	public String toString() {
		return "Audience N" + audienceNumber;
	}
}