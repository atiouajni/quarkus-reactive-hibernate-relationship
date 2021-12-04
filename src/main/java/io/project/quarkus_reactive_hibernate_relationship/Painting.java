package io.project.quarkus_reactive_hibernate_relationship;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "Painting")
@Table(name = "painting")
public class Painting {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id", nullable = false, referencedColumnName = "id")
	@JsonIgnore
	Artist author;

	public Painting() {
	}

	public Painting(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Artist getAuthor() {
		return author;
	}

	public void setAuthor(Artist author) {
		this.author = author;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}
		Painting city = (Painting) o;
		return Objects.equals( this.name, city.name );
	}

	@Override
	public int hashCode() {
		return Objects.hash( name, name );
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append( id );
		builder.append( ':' );
		builder.append( name );
		builder.append( ':' );
		return builder.toString();
	}
}
