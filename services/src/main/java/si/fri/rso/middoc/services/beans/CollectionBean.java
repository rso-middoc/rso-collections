package src.main.java.si.fri.rso.middoc.services.beans;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import src.main.java.si.fri.rso.middoc.models.converters.CollectionsConverter;
import src.main.java.si.fri.rso.middoc.models.entities.CollectionEntity;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@RequestScoped
public class CollectionBean {

    private Logger log = Logger.getLogger(CollectionBean.class.getName());

    @Inject
    private EntityManager em;

    private Client httpClient;

    private String baseUrl;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
        baseUrl = "http://localhost:8081"; // only for demonstration
    }


    public List<src.main.java.si.fri.rso.middoc.lib.Collection> getCollections() {

        TypedQuery<CollectionEntity> query = em.createNamedQuery("CollectionEntity.getAll",
                CollectionEntity.class);

        return query.getResultList().stream().map(CollectionsConverter::toDto).collect(Collectors.toList());

    }

    public List<src.main.java.si.fri.rso.middoc.lib.Collection> getCollectionsFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, CollectionEntity.class, queryParameters).stream()
                .map(CollectionsConverter::toDto).collect(Collectors.toList());
    }

    public src.main.java.si.fri.rso.middoc.lib.Collection getCollection(Integer id) {

        CollectionEntity collectionEntity = em.find(CollectionEntity.class, id);

        if (collectionEntity == null) {
            throw new NotFoundException();
        }

        return CollectionsConverter.toDto(collectionEntity);
    }

    public src.main.java.si.fri.rso.middoc.lib.Collection createCollection(src.main.java.si.fri.rso.middoc.lib.Collection collection) {

        CollectionEntity collectionEntity = CollectionsConverter.toEntity(collection);

        try {
            beginTx();
            em.persist(collectionEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        if (collectionEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return CollectionsConverter.toDto(collectionEntity);
    }

    public src.main.java.si.fri.rso.middoc.lib.Collection putCollection(Integer id, src.main.java.si.fri.rso.middoc.lib.Collection collection) {

        CollectionEntity c = em.find(CollectionEntity.class, id);

        if (c == null) {
            return null;
        }

        CollectionEntity updatedCollectionEntity = CollectionsConverter.toEntity(collection);

        try {
            beginTx();
            updatedCollectionEntity.setId(c.getId());
            updatedCollectionEntity = em.merge(updatedCollectionEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return CollectionsConverter.toDto(updatedCollectionEntity);
    }

    public boolean deleteCollection(Integer id) {

        CollectionEntity collection = em.find(CollectionEntity.class, id);

        if (collection != null) {
            try {
                beginTx();
                em.remove(collection);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }

}
