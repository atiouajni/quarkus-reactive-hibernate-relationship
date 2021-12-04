package io.project.quarkus_reactive_hibernate_relationship;

import static org.hibernate.reactive.mutiny.Mutiny.fetch;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.hibernate.reactive.mutiny.Mutiny;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.smallrye.mutiny.Uni;

@Path("tests")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class TestResource {
	private static final Logger LOGGER = Logger.getLogger(TestResource.class);

	
	@Inject
	Mutiny.Session session;
	
	@Inject
	Mutiny.SessionFactory sessionFactory;

	@GET
	@Path("/persist")
	public Uni<Response> persist() {
		LOGGER.debug("Persist new Artist + Painting");
		Artist artemisia = new Artist( "Artemisia Gentileschi" );
		Painting p = new Painting("Mona Lisa");
		p.setAuthor(artemisia);
		artemisia.getPaintings().add(p);
		
		return toResponse(session.withTransaction(
				// persist the Artist with their Painting in a transaction
				tx -> session.persist( artemisia )
		).chain(ignore -> session.find( Artist.class, Long.valueOf(1) )) ) ;
				
	}
	
	@GET
	public Uni<Response> getArtist() {
		LOGGER.debug("Receiving request to retrieve Artist");
		return toResponse(
					session.withTransaction(
							tx -> sessionFactory.openSession().chain( session -> session
									  .createQuery( "from Artist", Artist.class )
									  .getSingleResult() )
									  // We are checking `.getPaintings()` but not doing anything with it and therefore it should work.
									  .onItem().invoke( Artist::getPaintings )
					));
				
	}
	
	@GET
	@Path("/{id}/paintings")
	public Uni<Response> getPaintings(@PathParam("id") String id) {
		LOGGER.debug("Receiving request to retrieve all painting for  "+id);
		return toResponse(
					session.withTransaction(
							tx -> sessionFactory.openSession()
								.chain( session -> session.find(Artist.class, Long.valueOf(id)))
								// lazily fetch their painting
								.chain( a -> fetch(a.getPaintings()))
					));
	}

	private Uni<Response> toResponse(Uni<?> uni) {
		return uni.onItem().ifNotNull().transform(item -> Response.ok(item).build())
				.onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).build())
				.onFailure(IllegalArgumentException.class)
					.recoverWithItem(faillure -> Response.status(Response.Status.BAD_REQUEST).entity(new ObjectMapper().createObjectNode().put("message", faillure.getMessage())).build());
	}
}
