/*
 * Copyright (C) 2020 Grakn Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package grakn.simulation.common.agent.write;

import grakn.simulation.common.agent.base.SimulationContext;
import grakn.simulation.common.action.ActionFactory;
import grakn.simulation.common.agent.region.CountryAgent;
import grakn.simulation.common.driver.DbDriver;
import grakn.simulation.common.driver.DbOperation;
import grakn.simulation.common.driver.DbOperationFactory;
import grakn.simulation.common.world.World;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;

public class CompanyAgent<DB_OPERATION extends DbOperation> extends CountryAgent<DB_OPERATION> {

    public CompanyAgent(DbDriver<DB_OPERATION> dbDriver, ActionFactory<DB_OPERATION, ?> actionFactory) {
        super(dbDriver, actionFactory);
    }

    @Override
    protected Country getRegionalAgent(int simulationStep, String tracker, Random random, boolean test) {
        return new Country(simulationStep, tracker, random, test);
    }

    public class Country extends CountryRegion {
        public Country(int simulationStep, String tracker, Random random, boolean test) {
            super(simulationStep, tracker, random, test);
        }

        @Override
        protected void run(DbOperationFactory<DB_OPERATION> dbOperationFactory, World.Country country, SimulationContext simulationContext) {
            int numCompanies = simulationContext.world().getScaleFactor();

            try (DB_OPERATION dbOperation = dbOperationFactory.newDbOperation(tracker(), trace())) {

                for (int i = 0; i < numCompanies; i++) {
                    String adjective = pickOne(simulationContext.world().getAdjectives());
                    String noun = pickOne(simulationContext.world().getNouns());

                    int companyNumber = uniqueId(simulationContext, i).hashCode();
                    String companyName = StringUtils.capitalize(adjective) + StringUtils.capitalize(noun) + "-" + companyNumber;
                    runAction(actionFactory().insertCompanyAction(dbOperation, country, simulationContext.today(), companyNumber, companyName));
                }
                dbOperation.save();
            }
        }
    }
}