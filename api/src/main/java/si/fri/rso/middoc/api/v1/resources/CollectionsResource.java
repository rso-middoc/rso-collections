package src.main.java.si.fri.rso.middoc.api.v1.resources;

import src.main.java.si.fri.rso.middoc.services.beans.CollectionBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@ApplicationScoped
@Path("/collections")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CollectionsResource {

    @Inject
    private CollectionBean collectionBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getCollections() {

        List<src.main.java.si.fri.rso.middoc.lib.Collection> collections = collectionBean.getCollectionsFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(collections).build();
    }

    @GET
    @Path("/{collectionId}")
    public Response getCollection(@PathParam("collectionId") Integer collectionId) {

        src.main.java.si.fri.rso.middoc.lib.Collection collection = collectionBean.getCollection(collectionId);

        if (collection == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(collection).build();
    }

    @POST
    public Response createCollection(src.main.java.si.fri.rso.middoc.lib.Collection collection) {

        if ((collection.getTitle() == null || collection.getDescription() == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            collection = collectionBean.createCollection(collection);
        }

        return Response.status(Response.Status.OK).entity(collection).build();

    }

    @PUT
    @Path("{collectionId}")
    public Response putCollection(@PathParam("collectionId") Integer collectionId, src.main.java.si.fri.rso.middoc.lib.Collection collection) {

        collection = collectionBean.putCollection(collectionId, collection);

        if (collection == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

    @DELETE
    @Path("{collectionId}")
    public Response deleteCollection(@PathParam("collectionId") Integer collectionId) {

        boolean deleted = collectionBean.deleteCollection(collectionId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
