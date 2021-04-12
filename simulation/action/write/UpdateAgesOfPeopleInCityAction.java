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

package grakn.benchmark.simulation.action.write;

import grakn.benchmark.simulation.action.Action;
import grakn.benchmark.simulation.common.GeoData;
import grakn.benchmark.simulation.driver.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class UpdateAgesOfPeopleInCityAction<TX extends Transaction> extends Action<TX, Void> {
    protected final LocalDateTime today;
    protected final GeoData.City city;

    public UpdateAgesOfPeopleInCityAction(TX tx, LocalDateTime today, GeoData.City city) {
        super(tx);
        this.today = today;
        this.city = city;
    }

    @Override
    protected ArrayList<Object> inputForReport() {
        return argsList(today, city);
    }

    @Override
    public HashMap<ComparableField, Object> outputForReport(Void answer) {
        return new HashMap<>(); // Nothing to report for this action
    }
}