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

package grakn.simulation.common.action.write;

import grakn.simulation.common.action.Action;
import grakn.simulation.common.driver.DbOperation;

import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class InsertFriendshipAction<DB_OPERATION extends DbOperation, ACTION_RETURN_TYPE> extends Action<DB_OPERATION, ACTION_RETURN_TYPE> {
    protected final LocalDateTime today;
    protected final String friend1Email;
    protected final String friend2Email;

    public InsertFriendshipAction(DB_OPERATION dbOperation, LocalDateTime today, String friend1Email, String friend2Email) {
        super(dbOperation);
        this.today = today;
        this.friend1Email = friend1Email;
        this.friend2Email = friend2Email;
    }

    @Override
    protected ArrayList<Object> inputForReport() {
        return argsList(today, friend1Email, friend2Email);
    }

    public enum InsertFriendshipActionField implements ComparableField {
        FRIEND1_EMAIL, FRIEND2_EMAIL, START_DATE
    }
}
