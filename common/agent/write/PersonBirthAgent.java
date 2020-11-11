package grakn.simulation.common.agent.write;

import grakn.simulation.common.agent.base.SimulationContext;
import grakn.simulation.common.action.ActionFactory;
import grakn.simulation.common.agent.region.CityAgent;
import grakn.simulation.common.driver.DbDriver;
import grakn.simulation.common.driver.DbOperation;
import grakn.simulation.common.driver.DbOperationFactory;
import grakn.simulation.common.world.World;

import java.util.Random;

public class PersonBirthAgent<DB_OPERATION extends DbOperation> extends CityAgent<DB_OPERATION> {

    public PersonBirthAgent(DbDriver<DB_OPERATION> dbDriver, ActionFactory<DB_OPERATION, ?> actionFactory) {
        super(dbDriver, actionFactory);
    }

    @Override
    protected RegionalPersonBirthAgent getRegionalAgent(int simulationStep, String tracker, Random random, boolean test) {
        return new RegionalPersonBirthAgent(simulationStep, tracker, random, test);
    }

    public class RegionalPersonBirthAgent extends RegionalAgent {
        public RegionalPersonBirthAgent(int simulationStep, String tracker, Random random, boolean test) {
            super(simulationStep, tracker, random, test);
        }

        @Override
        protected void run(DbOperationFactory<DB_OPERATION> dbOperationFactory, World.City city, SimulationContext simulationContext) {
            // Find bachelors and bachelorettes who are considered adults and who are not in a marriage and pair them off randomly
            int numBirths = simulationContext.world().getScaleFactor();
            try (DB_OPERATION dbOperation = dbOperationFactory.newDbOperation(tracker(), trace())) {
                for (int i = 0; i < numBirths; i++) {
                    String gender;
                    String forename;
                    String surname = pickOne(simulationContext.world().getSurnames());

                    boolean genderBool = random().nextBoolean();
                    if (genderBool) {
                        gender = "male";
                        forename = pickOne(simulationContext.world().getMaleForenames());
                    } else {
                        gender = "female";
                        forename = pickOne(simulationContext.world().getFemaleForenames());
                    }
                    String email = "email/" + uniqueId(simulationContext, i);
                    runAction(actionFactory().insertPersonAction(dbOperation, city, simulationContext.today(), email, gender, forename, surname));
                }
                dbOperation.save();
            }
        }
    }
}