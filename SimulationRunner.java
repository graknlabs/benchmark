package grakn.simulation;

import grabl.tracing.client.GrablTracing;
import grabl.tracing.client.GrablTracingThreadStatic;
import grakn.simulation.config.Config;
import grakn.simulation.config.ConfigLoader;
import grakn.simulation.db.common.Simulation;
import grakn.simulation.db.common.agents.base.AgentRunner;
import grakn.simulation.db.common.agents.base.ResultHandler;
import grakn.simulation.db.common.initialise.AgentPicker;
import grakn.simulation.db.common.world.World;
import grakn.simulation.db.grakn.GraknSimulation;
import grakn.simulation.db.neo4j.Neo4jSimulation;
import grakn.simulation.utils.RandomSource;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static grabl.tracing.client.GrablTracing.tracing;
import static grabl.tracing.client.GrablTracing.tracingNoOp;
import static grabl.tracing.client.GrablTracing.withLogging;
import static grakn.simulation.db.common.world.World.initialise;

public class SimulationRunner {

    final static Logger LOG = LoggerFactory.getLogger(SimulationRunner.class);
    private final static String DEFAULT_CONFIG_YAML = "config/config.yaml";

    public static void main(String[] args) {

        ///////////////////
        // CONFIGURATION //
        ///////////////////

        Options options = cliOptions();
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            System.exit(1);
            return;
        }

        String dbName = getOption(commandLine, "d").orElse("grakn");
        String hostUri = getOption(commandLine, "s").orElse(null);
        String grablTracingUri = getOption(commandLine, "t").orElse("localhost:7979");
        String grablTracingOrganisation = commandLine.getOptionValue("o");
        String grablTracingRepository = commandLine.getOptionValue("r");
        String grablTracingCommit = commandLine.getOptionValue("c");
        String grablTracingUsername = commandLine.getOptionValue("u");
        String grablTracingToken = commandLine.getOptionValue("a");

        boolean disableTracing = commandLine.hasOption("n");

        Map<String, Path> initialisationDataFiles = new HashMap<>();
        for (String filepath : commandLine.getArgList()) {
            Path path = Paths.get(filepath);
            String filename = path.getFileName().toString();
            initialisationDataFiles.put(filename, path);
        }

        Path configPath = Paths.get(getOption(commandLine, "b").orElse(DEFAULT_CONFIG_YAML));
        Config config = ConfigLoader.loadConfigFromYaml(configPath.toFile());

        ////////////////////
        // INITIALIZATION //
        ////////////////////

        // Components customised based on the DB
        String defaultUri;
        AgentPicker agentPicker;
        List<AgentRunner<?, ?>> agentRunnerList;

        LOG.info("Welcome to the Simulation!");
        LOG.info("Parsing world data...");
        World world = initialise(config.getScaleFactor(), initialisationDataFiles);
        if (world == null) return;

        LOG.info(String.format("Connecting to %s...", dbName));

        try {
            try (GrablTracing tracingIgnored = grablTracing(grablTracingUri, grablTracingOrganisation, grablTracingRepository, grablTracingCommit, grablTracingUsername, grablTracingToken, disableTracing)) {

                Simulation simulation;
                switch (dbName) {
                    case "grakn":
                        defaultUri = "localhost:48555";
                        if (hostUri == null) hostUri = defaultUri;

                        simulation = new GraknSimulation(
                                hostUri,
                                config.getDatabaseName(),
                                initialisationDataFiles,
                                new RandomSource(config.getRandomSeed()),
                                world,
                                config.getAgents(),
                                config.getTraceSampling().getSamplingFunction(),
                                new ResultHandler()
                        );
                        break;

                    case "neo4j":
                        defaultUri = "bolt://localhost:7687";
                        if (hostUri == null) hostUri = defaultUri;

                        simulation = new Neo4jSimulation(
                                hostUri,
                                initialisationDataFiles,
                                new RandomSource(config.getRandomSeed()),
                                world,
                                config.getAgents(),
                                config.getTraceSampling().getSamplingFunction(),
                                new ResultHandler()
                        );
                        break;

                    default:
                        throw new IllegalArgumentException("Unexpected value: " + dbName);
                }

                ///////////////
                // MAIN LOOP //
                ///////////////
                for (int i = 0; i < config.getIterations(); ++i) {
                    simulation.iterate();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        LOG.info("Simulation complete");
    }

    private static Optional<String> getOption(CommandLine commandLine, String option) {
        if (commandLine.hasOption(option)) {
            return Optional.of(commandLine.getOptionValue(option));
        } else {
            return Optional.empty();
        }
    }

    private static Options cliOptions() {
        Options options = new Options();
        options.addOption(Option.builder("d")
                .longOpt("database").desc("Database under test").hasArg().required().argName("database")
                .build());
        options.addOption(Option.builder("s")
                .longOpt("database-uri").desc("Database server URI").hasArg().argName("uri")
                .build());
        options.addOption(Option.builder("t")
                .longOpt("tracing-uri").desc("Grabl tracing server URI").hasArg().argName("uri")
                .build());
        options.addOption(Option.builder("o")
                .longOpt("org").desc("Repository organisation").hasArg().argName("name")
                .build());
        options.addOption(Option.builder("r")
                .longOpt("repo").desc("Grabl tracing repository").hasArg().argName("name")
                .build());
        options.addOption(Option.builder("c")
                .longOpt("commit").desc("Grabl tracing commit").hasArg().argName("sha")
                .build());
        options.addOption(Option.builder("u")
                .longOpt("username").desc("Grabl tracing username").hasArg().argName("username")
                .build());
        options.addOption(Option.builder("a")
                .longOpt("api-token").desc("Grabl tracing API token").hasArg().argName("token")
                .build());
        options.addOption(Option.builder("b")
                .longOpt("config-file").desc("Configuration file").hasArg().argName("config-file-path")
                .build());
        options.addOption(Option.builder("n")
                .longOpt("disable-tracing").desc("Disable grabl tracing")
                .build());
        return options;
    }

    public static GrablTracing grablTracing(String grablTracingUri, String grablTracingOrganisation, String grablTracingRepository, String grablTracingCommit, String grablTracingUsername, String grablTracingToken, boolean disableTracing) {
        GrablTracing tracing;
        if (disableTracing) {
            tracing = withLogging(tracingNoOp());
        } else if (grablTracingUsername == null) {
            tracing = withLogging(tracing(grablTracingUri));
        } else {
            tracing = withLogging(tracing(grablTracingUri, grablTracingUsername, grablTracingToken));
        }
        GrablTracingThreadStatic.setGlobalTracingClient(tracing);
        GrablTracingThreadStatic.openGlobalAnalysis(grablTracingOrganisation, grablTracingRepository, grablTracingCommit);
        return tracing;
    }
}