package telran.net;

import java.io.*;

public interface NetworkClient extends Closeable {
	<T> T send(String type, Serializable requestData);
}
