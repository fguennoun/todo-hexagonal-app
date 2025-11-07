package ddd;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>
 * A Factory is responsible for creating complex objects and Aggregates,
 * ensuring that all required invariants are satisfied.
 * </p>
 *
 * @see <a href=
 * "https://www.domainlanguage.com/wp-content/uploads/2016/05/DDD_Reference_2015-03.pdf">Domain-Driven Design Reference</a>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Factory {
}

