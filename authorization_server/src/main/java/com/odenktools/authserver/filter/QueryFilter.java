package com.odenktools.authserver.filter;

import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.PathBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.NullHandling;
import org.springframework.data.mapping.PropertyPath;

/**
 * Generic Query Filter.
 *
 * @param <MODELS> Nama Model Class.
 * @author Odenktools
 */
public abstract class QueryFilter<MODELS> {

	@SuppressWarnings({"rawtypes", "unchecked"})
	protected OrderSpecifier<?> toOrderSpecifier(Class<? extends MODELS> type,
												 PathMetadata<?> pathMetadata,
												 Sort.Order order) {

		PathBuilder<MODELS> builder = new PathBuilder<>(type, pathMetadata);
		return new OrderSpecifier(order.isAscending() ? com.mysema.query.types.Order.ASC
				: com.mysema.query.types.Order.DESC, this.buildOrderPropertyPathFrom(builder, order),
				this.toQueryDslNullHandling(order.getNullHandling()));
	}

	private OrderSpecifier.NullHandling toQueryDslNullHandling(NullHandling nullHandling) {

		switch (nullHandling) {
			case NULLS_FIRST:
				return OrderSpecifier.NullHandling.NullsFirst;
			case NULLS_LAST:
				return OrderSpecifier.NullHandling.NullsLast;
			case NATIVE:
			default:
				return OrderSpecifier.NullHandling.Default;
		}
	}

	private Expression<?> buildOrderPropertyPathFrom(PathBuilder<MODELS> pathBuilder, Sort.Order order) {

		PropertyPath path = PropertyPath.from(order.getProperty(), pathBuilder.getType());
		Expression<?> sortPropertyExpression = pathBuilder;
		while (path != null) {
			if (!path.hasNext() && order.isIgnoreCase()) {
				sortPropertyExpression = Expressions.stringPath((Path<?>) sortPropertyExpression,
						path.getSegment()).lower();
			} else {
				sortPropertyExpression = Expressions.path(path.getType(), (Path<?>) sortPropertyExpression,
						path.getSegment());
			}

			path = path.next();
		}
		return sortPropertyExpression;
	}

}
