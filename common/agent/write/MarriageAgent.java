package grakn.simulation.common.agent.write;

import grakn.simulation.common.agent.base.SimulationContext;
import grakn.simulation.common.action.ActionFactory;
import grakn.simulation.common.agent.region.CityAgent;
import grakn.simulation.common.driver.DbDriver;
import grakn.simulation.common.driver.DbOperation;
import grakn.simulation.common.driver.DbOperationFactory;
import grakn.simulation.common.world.World;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class MarriageAgent<DB_OPERATION extends DbOperation> extends CityAgent<DB_OPERATION> {

    public MarriageAgent(DbDriver<DB_OPERATION> dbDriver, ActionFactory<DB_OPERATION, ?> actionFactory) {
        super(dbDriver, actionFactory);
    }

    @Override
    protected RegionalMarriageAgent getRegionalAgent(int simulationStep, String tracker, Random random, boolean test) {
        return new RegionalMarriageAgent(simulationStep, tracker, random, test);
    }

    public class RegionalMarriageAgent extends RegionalAgent {
        public RegionalMarriageAgent(int simulationStep, String tracker, Random random, boolean test) {
            super(simulationStep, tracker, random, test);
        }

        @Override
        protected void run(DbOperationFactory<DB_OPERATION> dbOperationFactory, World.City city, SimulationContext simulationContext) {

            // Find bachelors and bachelorettes who are considered adults and who are not in a marriage and pair them off randomly
            LocalDateTime dobOfAdults = simulationContext.today().minusYears(simulationContext.world().AGE_OF_ADULTHOOD);
            List<String> womenEmails;
            try (DB_OPERATION dbOperation = dbOperationFactory.newDbOperation(tracker(), trace())) {
                womenEmails = runAction(actionFactory().unmarriedPeopleInCityAction(dbOperation, city, "female", dobOfAdults));
                shuffle(womenEmails);
            }

            List<String> menEmails;
            try (DB_OPERATION dbOperation = dbOperationFactory.newDbOperation(tracker(), trace())) {
                menEmails = runAction(actionFactory().unmarriedPeopleInCityAction(dbOperation, city, "male", dobOfAdults));
                shuffle(menEmails);
            }

            int numMarriagesPossible = Math.min(simulationContext.world().getScaleFactor(), Math.min(womenEmails.size(), menEmails.size()));
            try (DB_OPERATION dbOperation = dbOperationFactory.newDbOperation(tracker(), trace())) {
                if (numMarriagesPossible > 0) {
                    for (int i = 0; i < numMarriagesPossible; i++) {
                        String wifeEmail = womenEmails.get(i);
                        String husbandEmail = menEmails.get(i);
                        int marriageIdentifier = uniqueId(simulationContext, i).hashCode();
                        runAction(actionFactory().insertMarriageAction(dbOperation, city, marriageIdentifier, wifeEmail, husbandEmail));
                    }
                    dbOperation.save();
                }
            }
        }
    }
}