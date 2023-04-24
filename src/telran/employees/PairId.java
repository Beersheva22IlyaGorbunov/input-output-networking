package telran.employees;

import java.io.Serializable;

public record PairId<T> (long id, T value) implements Serializable {

}
