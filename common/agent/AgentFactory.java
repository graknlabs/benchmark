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

package grakn.simulation.common.agent;

import grakn.simulation.common.action.ActionFactory;
import grakn.simulation.common.agent.base.Agent;
import grakn.simulation.common.agent.insight.ArbitraryOneHopAgent;
import grakn.simulation.common.agent.insight.FindCurrentResidentsAgent;
import grakn.simulation.common.agent.insight.FindLivedInAgent;
import grakn.simulation.common.agent.insight.FindSpecificMarriageAgent;
import grakn.simulation.common.agent.insight.FindSpecificPersonAgent;
import grakn.simulation.common.agent.insight.FindTransactionCurrencyAgent;
import grakn.simulation.common.agent.insight.FourHopAgent;
import grakn.simulation.common.agent.insight.MeanWageAgent;
import grakn.simulation.common.agent.insight.ThreeHopAgent;
import grakn.simulation.common.agent.insight.TwoHopAgent;
import grakn.simulation.common.agent.write.AgeUpdateAgent;
import grakn.simulation.common.agent.write.CompanyAgent;
import grakn.simulation.common.agent.write.EmploymentAgent;
import grakn.simulation.common.agent.write.FriendshipAgent;
import grakn.simulation.common.agent.write.MarriageAgent;
import grakn.simulation.common.agent.write.ParentshipAgent;
import grakn.simulation.common.agent.write.PersonBirthAgent;
import grakn.simulation.common.agent.write.ProductAgent;
import grakn.simulation.common.agent.write.RelocationAgent;
import grakn.simulation.common.agent.write.TransactionAgent;
import grakn.simulation.common.driver.DbDriver;
import grakn.simulation.common.driver.DbOperation;

public class AgentFactory<DB_OPERATION extends DbOperation, ACTION_FACTORY extends ActionFactory<DB_OPERATION, ?>> {

    private final DbDriver<DB_OPERATION> dbDriver;
    private final ACTION_FACTORY actionFactory;

    public AgentFactory(DbDriver<DB_OPERATION> dbDriver, ACTION_FACTORY actionFactory) {
        this.dbDriver = dbDriver;
        this.actionFactory = actionFactory;
    }

    public MarriageAgent<DB_OPERATION> marriage() {
        return new MarriageAgent<>(dbDriver, actionFactory);
    }

    public PersonBirthAgent<DB_OPERATION> personBirth() {
        return new PersonBirthAgent<>(dbDriver, actionFactory);
    }

    public AgeUpdateAgent<DB_OPERATION> ageUpdate() {
        return new AgeUpdateAgent<>(dbDriver, actionFactory);
    }

    public ParentshipAgent<DB_OPERATION> parentship() {
        return new ParentshipAgent<>(dbDriver, actionFactory);
    }

    public RelocationAgent<DB_OPERATION> relocation() {
        return new RelocationAgent<>(dbDriver, actionFactory);
    }

    public CompanyAgent<DB_OPERATION> company() {
        return new CompanyAgent<>(dbDriver, actionFactory);
    }

    public EmploymentAgent<DB_OPERATION> employment() {
        return new EmploymentAgent<>(dbDriver, actionFactory);
    }

    public ProductAgent<DB_OPERATION> product() {
        return new ProductAgent<>(dbDriver, actionFactory);
    }

    public TransactionAgent<DB_OPERATION> transaction() {
        return new TransactionAgent<>(dbDriver, actionFactory);
    }

    public FriendshipAgent<DB_OPERATION> friendship() {
        return new FriendshipAgent<>(dbDriver, actionFactory);
    }

    public MeanWageAgent<DB_OPERATION> meanWage() {
        return new MeanWageAgent<>(dbDriver, actionFactory);
    }

    public FindLivedInAgent<DB_OPERATION> findLivedIn() {
        return new FindLivedInAgent<>(dbDriver, actionFactory);
    }

    public FindCurrentResidentsAgent<DB_OPERATION> findCurrentResidents() {
        return new FindCurrentResidentsAgent<>(dbDriver, actionFactory);
    }

    public FindTransactionCurrencyAgent<DB_OPERATION> findTransactionCurrency() {
        return new FindTransactionCurrencyAgent<>(dbDriver, actionFactory);
    }

    public ArbitraryOneHopAgent<DB_OPERATION> arbitraryOneHop() {
        return new ArbitraryOneHopAgent<>(dbDriver, actionFactory);
    }

    public TwoHopAgent<DB_OPERATION> twoHop() {
        return new TwoHopAgent<>(dbDriver, actionFactory);
    }

    public ThreeHopAgent<DB_OPERATION> threeHop() {
        return new ThreeHopAgent<>(dbDriver, actionFactory);
    }

    public FourHopAgent<DB_OPERATION> fourHop() {
        return new FourHopAgent<>(dbDriver, actionFactory);
    }

    public FindSpecificMarriageAgent<DB_OPERATION> findSpecificMarriage() {
        return new FindSpecificMarriageAgent<>(dbDriver, actionFactory);
    }

    public FindSpecificPersonAgent<DB_OPERATION> findSpecificPerson() {
        return new FindSpecificPersonAgent<>(dbDriver, actionFactory);
    }

    public Agent<?, DB_OPERATION> get(String agentName) {
        switch (agentName) {
            case "marriage":
                return marriage();
            case "personBirth":
                return personBirth();
            case "ageUpdate":
                return ageUpdate();
            case "parentship":
                return parentship();
            case "relocation":
                return relocation();
            case "company":
                return company();
            case "employment":
                return employment();
            case "product":
                return product();
            case "transaction":
                return transaction();
            case "friendship":
                return friendship();
            case "meanWage":
                return meanWage();
            case "findLivedIn":
                return findLivedIn();
            case "findCurrentResidents":
                return findCurrentResidents();
            case "findTransactionCurrency":
                return findTransactionCurrency();
            case "arbitraryOneHop":
                return arbitraryOneHop();
            case "twoHop":
                return twoHop();
            case "threeHop":
                return threeHop();
            case "fourHop":
                return fourHop();
            case "findSpecificMarriage":
                return findSpecificMarriage();
            case "findSpecificPerson":
                return findSpecificPerson();
            default:
                throw new IllegalArgumentException("Unrecognised agent name: " + agentName);
        }
    }
}
