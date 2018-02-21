package edu.babanin.bluedb.core;

import com.sun.tools.javac.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;

import static com.sun.tools.javac.util.List.*;
import static org.junit.jupiter.api.Assertions.*;

class DataBaseTest {
    private final String path = "db-storage-test";
    private DataBase db;

    @BeforeEach
    void setUp() throws IOException{
        String sPath = Paths.get(".")
                .toAbsolutePath()
                .normalize()
                .toString() + FileSystems.getDefault().getSeparator() + path;
        db = new BlueDataBase(Paths.get(sPath));
        db.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        db.stop();
        deleteDirectory(db.getDirectory());
    }

    void deleteDirectory(Path dbPath) throws IOException{

        if (Files.isExecutable(dbPath)) {
            Files.walkFileTree(dbPath,new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    @Test
    void save() {
    }

    @Test
    void doSqlRequest() {
        BlueDBAnswer test;

        // Создаём таблицу
        test = db.doSqlRequest("CREATE TABLE months (id int, name varchar(10), days int)");
        assertTrue(test.isSuccsecc());

        test = db.doSqlRequest("SELECT * FROM INFORMATION_SCHEMA.TABLES");
        assertTrue(test.isSuccsecc());
        assertEquals(test.getAnswer().toString(), List.of("months").toString());


        // Ввод данных
        test = db.doSqlRequest("INSERT INTO months VALUES (1,'January',31)");
        assertTrue(test.isSuccsecc());

        test = db.doSqlRequest("SELECT * FROM months WHERE id = 1");
        assertTrue(test.isSuccsecc());
        assertEquals(test.getAnswer().toString(), List.of(1,"January", 31).toString());

        test = db.doSqlRequest("INSERT INTO months (id,name,days) VALUES (2,'February',29)");
        assertTrue(test.isSuccsecc());

        test = db.doSqlRequest("SELECT * FROM months WHERE id = 2");
        assertTrue(test.isSuccsecc());
        assertEquals(test.getAnswer().toString(), List.of(2,"February", 29).toString());

        // Вывод данных из базы данных
        test = db.doSqlRequest("SELECT * FROM characters");
        test = db.doSqlRequest("SELECT name, weapon FROM characters");
        test = db.doSqlRequest("SELECT name, weapon FROM characters ORDER BY name DESC");

        // Where
        test = db.doSqlRequest("SELECT * FROM characters WHERE weapon = 'pistol'");

        // И/или
        test = db.doSqlRequest("SELECT * FROM albums \n" +
                "WHERE genre = 'rock' AND sales_in_millions <= 50 \n" +
                "ORDER BY released");

        // In/Between/Like
        test = db.doSqlRequest("SELECT * FROM albums WHERE genre IN ('pop','soul')");
        test = db.doSqlRequest("SELECT * FROM albums WHERE released BETWEEN 1975 AND 1985");
        test = db.doSqlRequest("SELECT * FROM albums WHERE album LIKE '%R%'");
        test = db.doSqlRequest("SELECT * FROM albums WHERE album LIKE 'R%'");


        // Функции
        test = db.doSqlRequest("SELECT MAX(released) FROM albums");
        test = db.doSqlRequest("SELECT name, avg(age) FROM students GROUP BY name");

        //Вложенные Select
        test = db.doSqlRequest("SELECT artist,album,released \n" +
                "FROM albums \n" +
                "WHERE released = (\n" +
                " SELECT MIN(released) FROM albums\n" +
                ")");

        // Присоединение таблиц
        test = db.doSqlRequest("SELECT video_games.name, video_games.genre, game_developers.name, game_developers.country \n" +
                "FROM video_games \n" +
                "INNER JOIN game_developers \n" +
                "ON video_games.developer_id = game_developers.id");


        // Псевдонимы
        test = db.doSqlRequest("SELECT games.name, games.genre, devs.name AS developer, devs.country \n" +
                "FROM video_games AS games \n" +
                "INNER JOIN game_developers AS devs \n" +
                "ON games.developer_id = devs.id");

        // Update
        test = db.doSqlRequest("UPDATE tv_series \n" +
                "SET genre = 'drama' \n" +
                "WHERE name = 'Game of Thrones'");

        // Удаление записей из таблицы
        test = db.doSqlRequest("DELETE FROM tv_series \n" +
                "WHERE id = 4");

        // Удаление всего содержимого таблицы
        test = db.doSqlRequest("TRUNCATE TABLE table_name");

        // Удаление таблиц
        test = db.doSqlRequest("DROP TABLE table_name");
    }
}