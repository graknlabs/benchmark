/*
 * Copyright (C) 2021 Grakn Labs
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

package grakn.benchmark.simulation.agent;

import grakn.benchmark.simulation.action.Action;
import grakn.benchmark.simulation.action.ActionFactory;
import grakn.benchmark.simulation.action.read.CompaniesInCountryAction;
import grakn.benchmark.simulation.action.read.ResidentsInCityAction;
import grakn.benchmark.simulation.common.RandomValueGenerator;
import grakn.benchmark.simulation.common.SimulationContext;
import grakn.benchmark.simulation.driver.Session;
import grakn.benchmark.simulation.driver.Transaction;
import grakn.benchmark.simulation.driver.Client;
import grakn.benchmark.simulation.common.GeoData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static grakn.benchmark.simulation.common.Allocation.allocate;

public class EmploymentAgent<TX extends Transaction> extends Agent<GeoData.City, TX> {

    private static final double MIN_ANNUAL_WAGE = 18000.00;
    private static final double MAX_ANNUAL_WAGE = 80000.00;
    private static final double MIN_CONTRACTED_HOURS = 30.0;
    private static final double MAX_CONTRACTED_HOURS = 70.0;
    private static final int MIN_CONTRACT_CHARACTER_LENGTH = 200;
    private static final int MAX_CONTRACT_CHARACTER_LENGTH = 600;

    public EmploymentAgent(Client<?, TX> client, ActionFactory<TX, ?> actionFactory, SimulationContext context) {
        super(client, actionFactory, context);
    }

    @Override
    protected List<GeoData.City> getRegions() {
        return context.geoData().cities();
    }

    @Override
    protected List<Action<?, ?>.Report> run(Session<TX> session, GeoData.City region, Random random) {
        List<Action<?, ?>.Report> reports = new ArrayList<>();
        LocalDateTime employmentDate = context.today().minusYears(0);
        List<String> employeeEmails;
        List<Long> companyNumbers;

        try (TX tx = session.transaction(region.tracker(), context.iterationNumber(), isTracing())) {
            ResidentsInCityAction<TX> employeeEmailsAction = actionFactory().residentsInCityAction(tx, region, context.scaleFactor(), employmentDate);
            employeeEmails = runAction(employeeEmailsAction, reports);
        }

        try (TX tx = session.transaction(region.tracker(), context.iterationNumber(), isTracing())) {
            CompaniesInCountryAction<TX> companyNumbersAction = actionFactory().companiesInCountryAction(tx, region.country(), context.scaleFactor());
            companyNumbers = runAction(companyNumbersAction, reports);
        }

        try (TX tx = session.transaction(region.tracker(), context.iterationNumber(), isTracing())) {
            // A second transaction is being used to circumvent graknlabs/grakn issue #5585
            boolean allocated = allocate(employeeEmails, companyNumbers, (employeeEmail, companyNumber) -> {
                double wageValue = RandomValueGenerator.of(random).boundRandomDouble(MIN_ANNUAL_WAGE, MAX_ANNUAL_WAGE);
                String contractContent = RandomValueGenerator.of(random).boundRandomLengthRandomString(MIN_CONTRACT_CHARACTER_LENGTH, MAX_CONTRACT_CHARACTER_LENGTH);
                double contractedHours = RandomValueGenerator.of(random).boundRandomDouble(MIN_CONTRACTED_HOURS, MAX_CONTRACTED_HOURS);
                runAction(actionFactory().insertEmploymentAction(tx, region, employeeEmail, companyNumber, employmentDate, wageValue, contractContent, contractedHours), reports);
            });
            if (allocated) {
                tx.commit();
            }
        }

        return reports;
    }
}