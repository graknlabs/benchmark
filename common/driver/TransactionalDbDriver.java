package grakn.simulation.common.driver;

public abstract class TransactionalDbDriver<TRANSACTION, SESSION, DB_OPERATION extends TransactionalDbOperation> extends DbDriver<DB_OPERATION> {
    public enum TracingLabel {
        OPEN_CLIENT("openClient"),
        CLOSE_CLIENT("closeClient"),
        OPEN_SESSION("openSession"),
        CLOSE_SESSION("closeSession"),
        OPEN_TRANSACTION("openTx"),
        CLOSE_TRANSACTION("closeTx"),
        COMMIT_TRANSACTION("commitTx"),
        EXECUTE("execute"),
        SORTED_EXECUTE("sortedExecute");

        private String name;

        TracingLabel(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public abstract SESSION session(String sessionKey);

    public abstract void closeSessions();

    public abstract class Session {
        public abstract TRANSACTION tx();
    }
}