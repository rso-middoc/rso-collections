package src.main.java.si.fri.rso.middoc.models.converters;

import src.main.java.si.fri.rso.middoc.models.entities.CollectionEntity;

public class CollectionsConverter {

    public static src.main.java.si.fri.rso.middoc.lib.Collection toDto(CollectionEntity entity) {

        src.main.java.si.fri.rso.middoc.lib.Collection dto = new src.main.java.si.fri.rso.middoc.lib.Collection();
        dto.setCollectionId(entity.getId());
        dto.setCreated(entity.getCreated());
        dto.setDescription(entity.getDescription());
        dto.setTitle(entity.getTitle());
        dto.setType(entity.getType());
        dto.setIsFree(entity.getIsFree());

        return dto;

    }

    public static CollectionEntity toEntity(src.main.java.si.fri.rso.middoc.lib.Collection dto) {

        CollectionEntity entity = new CollectionEntity();
        entity.setCreated(dto.getCreated());
        entity.setDescription(dto.getDescription());
        entity.setTitle(dto.getTitle());
        entity.setType(dto.getType());
        entity.setIsFree(dto.getIsFree());

        return entity;

    }

}
