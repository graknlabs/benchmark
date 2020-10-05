package grakn.simulation.db.grakn.agents.action;

import grakn.simulation.db.common.agents.action.CompanyNumbersAction;
import grakn.simulation.db.common.agents.base.TransactionDbOperationController;
import grakn.simulation.db.common.world.World;
import grakn.simulation.db.grakn.driver.GraknTransaction;
import graql.lang.Graql;
import graql.lang.query.GraqlGet;

import java.util.List;

import static grakn.simulation.db.grakn.schema.Schema.COMPANY;
import static grakn.simulation.db.grakn.schema.Schema.COMPANY_NUMBER;
import static grakn.simulation.db.grakn.schema.Schema.COUNTRY;
import static grakn.simulation.db.grakn.schema.Schema.INCORPORATION;
import static grakn.simulation.db.grakn.schema.Schema.INCORPORATION_INCORPORATED;
import static grakn.simulation.db.grakn.schema.Schema.INCORPORATION_INCORPORATING;
import static grakn.simulation.db.grakn.schema.Schema.LOCATION_NAME;

public class GraknCompanyNumbersAction extends CompanyNumbersAction<TransactionDbOperationController<GraknTransaction>.TransactionalDbOperation> {
    public GraknCompanyNumbersAction(TransactionDbOperationController<GraknTransaction>.TransactionalDbOperation dbOperation, World.Country country, int numCompanies) {
        super(dbOperation, country, numCompanies);
    }

    @Override
    public List<Long> run() {
        GraqlGet.Unfiltered companyNumbersQuery = Graql.match(
                Graql.var(COUNTRY).isa(COUNTRY)
                        .has(LOCATION_NAME, country.name()),
                Graql.var(COMPANY).isa(COMPANY)
                        .has(COMPANY_NUMBER, Graql.var(COMPANY_NUMBER)),
                Graql.var(INCORPORATION).isa(INCORPORATION)
                        .rel(INCORPORATION_INCORPORATED, Graql.var(COMPANY))
                        .rel(INCORPORATION_INCORPORATING, Graql.var(COUNTRY))
        ).get();
        return dbOperation.tx().getOrderedAttribute(companyNumbersQuery, COMPANY_NUMBER, numCompanies);
    }
}
