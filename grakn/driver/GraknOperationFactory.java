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

package grakn.simulation.grakn.driver;

import grakn.client.GraknClient;
import grakn.simulation.common.driver.TransactionalDbOperationFactory;
import org.slf4j.Logger;

import static com.google.common.collect.Iterables.getOnlyElement;

public class GraknOperationFactory extends TransactionalDbOperationFactory<GraknOperation> {

    private final GraknClient.Session session;

    public GraknOperationFactory(GraknClient.Session session, Logger logger) {
        super(logger);
        this.session = session;
    }

    @Override
    public GraknOperation newDbOperation(String tracker, boolean trace) {
        return new GraknOperation(session, logger(), tracker, trace);
    }

}
