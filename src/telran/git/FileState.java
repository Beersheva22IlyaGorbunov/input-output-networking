package telran.git;

import java.io.Serializable;

public record FileState(String filePath, Status status) implements Serializable {

}
