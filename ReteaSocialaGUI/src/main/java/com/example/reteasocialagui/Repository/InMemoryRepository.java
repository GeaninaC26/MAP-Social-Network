/*package Repository;

import Domain.Entity;
import Domain.Validation.ValidationException;
import Domain.Validation.Validator;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E>{

    private Validator<E> validator;
    Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    @Override
    public E findOne(ID id) {
        if (id == null)
            throw new IllegalArgumentException("Id-ul nu poate fi nul! ");
        return entities.get(id);
    }

    public boolean inRepository(E entity){
        return entities.containsValue(entity);
    }

    @Override
    public Iterable<E> findAll()  {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entitatea nu poate sa fie nula! ");
        validator.validate(entity);
        if(entities.get(entity.getId()) != null) {
            throw new ValidationException("Id deja existent in retea! ");
        }
        else
            return entities.put(entity.getId(),entity);
    }

    @Override
    public E delete(ID id) {
        if(findOne(id) == null )
            throw new ValidationException("Id inexistent.");
        return entities.remove(id);
    }

    @Override
    public E update(E entity) {

        if(entity == null)
            throw new IllegalArgumentException("Entitatea nu poate sa fie nula! ");
        validator.validate(entity);

        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return null;
        }
        return entity;

    }
}
*/