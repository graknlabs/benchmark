package grakn.simulation.db.common.agent.write;

import grakn.simulation.db.common.agent.base.SimulationContext;
import grakn.simulation.db.common.action.ActionFactory;
import grakn.simulation.db.common.action.read.ResidentsInCityAction;
import grakn.simulation.db.common.agent.region.CityAgent;
import grakn.simulation.db.common.driver.DbDriver;
import grakn.simulation.db.common.driver.DbOperation;
import grakn.simulation.db.common.driver.DbOperationFactory;
import grakn.simulation.db.common.world.World;

import java.util.List;
import java.util.Random;

public class FriendshipAgent<DB_DRIVER extends DbDriver<DB_OPERATION>, DB_OPERATION extends DbOperation> extends CityAgent<DB_DRIVER, DB_OPERATION> {

    public FriendshipAgent(DB_DRIVER dbDriver, ActionFactory<DB_OPERATION, ?> actionFactory) {
        super(dbDriver, actionFactory);
    }

    @Override
    protected RegionalFriendshipAgent getRegionalAgent(int simulationStep, String tracker, Random random, boolean test) {
        return new RegionalFriendshipAgent(simulationStep, tracker, random, test);
    }

    public class RegionalFriendshipAgent extends RegionalAgent {
        public RegionalFriendshipAgent(int simulationStep, String tracker, Random random, boolean test) {
            super(simulationStep, tracker, random, test);
        }

        @Override
        protected void run(DbOperationFactory<DB_OPERATION> dbOperationFactory, World.City city, SimulationContext simulationContext) {
            List<String> residentEmails;
            try (DB_OPERATION dbOperation = dbOperationFactory.newDbOperation(tracker(), trace())) {
                ResidentsInCityAction<?> residentEmailsAction = actionFactory().residentsInCityAction(dbOperation, city, simulationContext.world().getScaleFactor(), simulationContext.today());
                residentEmails = runAction(residentEmailsAction);
            } // TODO Closing and reopening the transaction here is a workaround for https://github.com/graknlabs/grakn/issues/5585

            try (DB_OPERATION dbOperation = dbOperationFactory.newDbOperation(tracker(), trace())) {
                if (residentEmails.size() > 0) {
                    shuffle(residentEmails);
                    int numFriendships = simulationContext.world().getScaleFactor();
                    for (int i = 0; i < numFriendships; i++) {
                        runAction(actionFactory().insertFriendshipAction(dbOperation, simulationContext.today(), pickOne(residentEmails), pickOne(residentEmails)));
                    }
                    dbOperation.save();
                }
            }
        }
    }
}
