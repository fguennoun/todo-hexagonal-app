package ddd;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>
 * An Aggregate Root is the entry point of an Aggregate, which is a cluster of
 * domain objects that can be treated as a single unit for data changes.
 * </p>
 * <p>
 * All external access to the aggregate must go through the aggregate root.
 * This ensures the aggregate maintains its invariants.
 * </p>
 *
 * @see <a href=
 * "https://www.domainlanguage.com/wp-content/uploads/2016/05/DDD_Reference_2015-03.pdf">Domain-Driven Design Reference</a>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AggregateRoot {
}

