package grakn.simulation.db.common.agents.interaction;

import grakn.simulation.db.common.agents.base.Agent;
import grakn.simulation.db.common.agents.base.AgentResult;
import grakn.simulation.db.common.agents.base.AgentResultSet;
import grakn.simulation.db.common.agents.base.SimulationContext;
import grakn.simulation.db.common.world.World;

import java.time.LocalDateTime;
import java.util.List;

import static grabl.tracing.client.GrablTracingThreadStatic.ThreadTrace;
import static grabl.tracing.client.GrablTracingThreadStatic.traceOnThread;
import static grakn.simulation.db.common.agents.utils.Allocation.allocate;

public interface EmploymentAgentBase extends InteractionAgent<World.City> {

    double MIN_ANNUAL_WAGE = 18000.00;
    double MAX_ANNUAL_WAGE = 80000.00;
    double MIN_CONTRACTED_HOURS = 30.0;
    double MAX_CONTRACTED_HOURS = 70.0;
    int MIN_CONTRACT_CHARACTER_LENGTH = 200;
    int MAX_CONTRACT_CHARACTER_LENGTH = 600;

    enum EmploymentAgentField implements Agent.ComparableField {
        CITY_NAME, PERSON_EMAIL, COMPANY_NUMBER, START_DATE, WAGE, CURRENCY, CONTRACT_CONTENT, CONTRACTED_HOURS
    }

    @Override
    default AgentResultSet iterate(Agent<World.City, ?> agent, World.City city, SimulationContext simulationContext) {
        LocalDateTime employmentDate = simulationContext.today().minusYears(0);
        int numEmployments = simulationContext.world().getScaleFactor();
        List<String> employeeEmails;
        List<Long> companyNumbers;
        agent.newAction("getEmployeeEmails");
        try (ThreadTrace trace = traceOnThread(agent.action())) {
            employeeEmails = getEmployeeEmails(city, numEmployments, employmentDate);
        }

        int numCompanies = simulationContext.world().getScaleFactor();
        agent.newAction("getCompanyNumbers");
        try (ThreadTrace trace = traceOnThread(agent.action())) {
            companyNumbers = getCompanyNumbers(city.country(), numCompanies);
        }
        agent.commitAction();  //TODO Should be close not commit?
        AgentResultSet agentResultSet = new AgentResultSet();
        // A second transaction is being used to circumvent graknlabs/grakn issue #5585
        boolean allocated = allocate(employeeEmails, companyNumbers, (employeeEmail, companyNumber) -> {
            double wageValue = agent.randomAttributeGenerator().boundRandomDouble(MIN_ANNUAL_WAGE, MAX_ANNUAL_WAGE);
            String contractContent = agent.randomAttributeGenerator().boundRandomLengthRandomString(MIN_CONTRACT_CHARACTER_LENGTH, MAX_CONTRACT_CHARACTER_LENGTH);
            double contractedHours = agent.randomAttributeGenerator().boundRandomDouble(MIN_CONTRACTED_HOURS, MAX_CONTRACTED_HOURS);
            agent.newAction("insertEmployment");
            try (ThreadTrace trace = traceOnThread(agent.action())) {
                agentResultSet.add(insertEmployment(city, employeeEmail, companyNumber, employmentDate, wageValue, contractContent, contractedHours));
            }
        });
        if (allocated) {
            agent.commitAction();
        }
        return agentResultSet;
    }

    List<Long> getCompanyNumbers(World.Country country, int numCompanies);

    List<String> getEmployeeEmails(World.City city, int numEmployments, LocalDateTime earliestDate);

    AgentResult insertEmployment(World.City city, String employeeEmail, long companyNumber, LocalDateTime employmentDate, double wageValue, String contractContent, double contractedHours);
}
