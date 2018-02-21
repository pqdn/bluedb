package edu.babanin.bluedb.core;

import java.io.IOException;
import java.nio.file.Path;

public interface DataBase {
    void start() throws IOException;
    void stop() throws IOException;
    void save() throws IOException;
    BlueDBAnswer doSqlRequest(String sql);
    boolean isWorking();
    Path getDirectory();
}
