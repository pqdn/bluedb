package edu.babanin.bluedb.core;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

public class BlueDataBase implements DataBase {
    final public Path dbDirectory;
    public boolean isRun = false;

    public BlueDataBase(Path dbDirectory) {
        this.dbDirectory = dbDirectory;
        if(dbDirectory == null){
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void start() throws IOException {
        isRun = true;
        if(!Files.exists(dbDirectory)){
           Files.createDirectory(dbDirectory);
        }else if(!Files.isDirectory(dbDirectory)){
            throw new IOException("На месте папки найден одноименный файл");
        }
    }

    @Override
    public void stop() throws IOException {
        isRun = false;
    }

    @Override
    public void save() throws IOException {

    }

    @Override
    public BlueDBAnswer doSqlRequest(String sql) {
        try {
            return null;
        }catch (Exception e){
            return new BlueDBAnswer(e);
        }
    }

    @Override
    public boolean isWorking() {
        return isRun;
    }

    @Override
    public Path getDirectory() {
        return dbDirectory;
    }
}
