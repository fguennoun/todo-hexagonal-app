package ddd;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>
 * A Repository is responsible for storing and retrieving Aggregates.
 * It provides the illusion of an in-memory collection of all objects of a certain type.
 * </p>
 * <p>
 * Repositories work with Aggregate Roots only, not with their internal entities.
 * </p>
 *
 * @see <a href=
 * "https://www.domainlanguage.com/wp-content/uploads/2016/05/DDD_Reference_2015-03.pdf">Domain-Driven Design Reference</a>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Repository {
}

