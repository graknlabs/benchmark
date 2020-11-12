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

package grakn.simulation.common.driver;

import org.slf4j.Logger;

public class LogWrapper {
    private final Logger logger;

    public LogWrapper(Logger logger) {
        this.logger = logger;
    }

    public void query(String tracker, String action, Object query) {
        query(tracker, action, query.toString());
    }

    public void query(String tracker, String action, String query) {
        logger.info("({}):{}:\n{}", tracker, action, query);
    }

    public void query(String tracker, Object query) {
        query(tracker, query.toString());
    }

    public void query(String tracker, String query) {
        logger.info("{}:\n{}", tracker, query);
    }
}
