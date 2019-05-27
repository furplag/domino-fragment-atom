/**
 * Copyright (C) 2019+ furplag (https://github.com/furplag)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.furplag.sandbox.domino.misc.origin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import jp.furplag.sandbox.domino.misc.generic.EntityInspector;
import jp.furplag.sandbox.domino.misc.vars.ColumnDef;
import jp.furplag.sandbox.stream.Streamr;

/**
 * a simply structure of the {@link org.seasar.doma.Entity} .
 *
 * @author furplag
 *
 */
public interface EntityOrigin extends Origin {

  /**
   * returns database columns defined in this entity .
   *
   * @param excludeFieldNames field name (s) which excludes from result
   * @return stream of database columns
   */
  private static Stream<ColumnDef<?>> filteredColumns(final List<ColumnDef<?>> columns, final Set<String> excludeFieldNames) {
    return Streamr.Filter.filtering(columns, (t) -> !excludeFieldNames.contains(t.getFieldName()));
  }

  /**
   * returns database columns defined in this entity .
   *
   * @return stream of database columns
   */
  default List<ColumnDef<?>> getColumns() {
    return inspector().getFields().values().stream().map((t) -> new ColumnDef<>(this, t))
      .flatMap(ColumnDef::flatternyze).sorted().collect(Collectors.toUnmodifiableList());
  }

  /** {@inheritDoc} */
  @Override
  default EntityInspector<?> inspector() {
    return new EntityInspector<>(getClass());
  }

  /**
   * returns database column names defined in this entity .
   *
   * @param excludeFieldNames field name (s) which excludes from result
   * @return comma-separated database column names
   */
  @Override
  default String selectColumnNames(String... excludeFieldNames) {
    return StringUtils.defaultIfBlank(filteredColumns(getColumns(), Streamr.collect(HashSet::new, excludeFieldNames)).map(ColumnDef::getColumnName).collect(Collectors.joining(", ")), Origin.super.selectColumnNames());
  }
}
