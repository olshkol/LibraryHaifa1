package telran.library.service.mappers;

public interface Mapper<E, D> {
    D toDto(E entity);
    E toEntity(D dto);
}
