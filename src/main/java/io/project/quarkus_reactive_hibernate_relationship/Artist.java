package io.project.quarkus_reactive_hibernate_relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "Artist")
	@Table(name = "artist")
	public  class Artist {

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long id;

		private String name;

		@OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST)
		@JsonIgnore
		private List<Painting> paintings = new ArrayList<Painting>();

		public Artist() {
		}

		public Artist(String name) {
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

		public List<Painting> getPaintings() {
			return paintings;
		}

		public void setPaintings(List<Painting> paintings) {
			this.paintings = paintings;
		}

		@Override
		public boolean equals(Object o) {
			if ( this == o ) {
				return true;
			}
			if ( o == null || getClass() != o.getClass() ) {
				return false;
			}
			Artist person = (Artist) o;
			return Objects.equals( name, person.name );
		}

		@Override
		public int hashCode() {
			return Objects.hash( name );
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append( id );
			builder.append( ':' );
			builder.append( name );
			return builder.toString();
		}
	}