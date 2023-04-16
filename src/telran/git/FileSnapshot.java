package telran.git;

import java.io.Serializable;
import java.time.Instant;

public record FileSnapshot(String path, Instant modifiedTime, String[] content) implements Serializable {}
