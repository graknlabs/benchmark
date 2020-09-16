package grakn.simulation.db.neo4j.initialise;

import grakn.simulation.db.common.yaml_tool.YAMLException;
import grakn.simulation.db.common.yaml_tool.YAMLLoader;
import grakn.simulation.db.neo4j.yaml_tool.Neo4jYAMLLoader;
import org.neo4j.driver.Session;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Map;

public class Neo4jInitialiser {

    private final Session session;
    private Map<String, Path> files;

    public Neo4jInitialiser(Session session, Map<String, Path> files) {
        this.session = session;
        this.files = files;
    }

    public void initialise() {
        YAMLLoader loader = new Neo4jYAMLLoader(session, files);
        try {
            loader.loadFile(files.get("neo4j_data.yml").toFile());
        } catch (YAMLException | FileNotFoundException e) {
            e.printStackTrace();
        }
        // TODO Add key constraints
    }
}
