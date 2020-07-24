package grakn.simulation.db.grakn.driver;

import grakn.client.GraknClient;
import grakn.simulation.db.common.driver.DriverWrapper;

public class GraknClientWrapper implements DriverWrapper {

    private GraknClient client = null;

    public GraknClient getClient() {
        return client;
    }

    @Override
    public GraknClientWrapper open(String uri) {
        client = new GraknClient(uri);
        return this;
    }

    @Override
    public void close() {
        client.close();
    }

    @Override
    public Session session(String database) {
        return new Session(client.session(database));
    }

    static class Session extends DriverWrapper.Session {

        private GraknClient.Session session;

        Session(GraknClient.Session session) {
            this.session = session;
        }

        @Override
        public void close() {
            this.session.close();
        }

        @Override
        public Transaction transaction() {
            return new Transaction(session.transaction(GraknClient.Transaction.Type.WRITE));
        }

        class Transaction extends DriverWrapper.Session.Transaction {

            private GraknClient.Transaction transaction;

            Transaction(GraknClient.Transaction transaction) {
                this.transaction = transaction;
            }

            @Override
            public void close() {
                transaction.close();
            }

            @Override
            public void commit() {
                transaction.commit();
            }

            @Override
            public GraknClient.Transaction forGrakn() {
                return transaction;
            }
        }
    }
}
