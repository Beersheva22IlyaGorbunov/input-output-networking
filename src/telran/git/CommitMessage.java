package telran.git;

import java.io.Serializable;
import java.time.Instant;

public record CommitMessage(String commitName, String commitMessage, Instant commitTime) implements Serializable {}
