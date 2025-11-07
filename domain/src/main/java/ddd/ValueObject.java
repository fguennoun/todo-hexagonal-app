package ddd;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>
 * A Value Object is an immutable object that is defined only by its attributes.
 * Two value objects with the same attributes are considered equal.
 * </p>
 * <p>
 * Value Objects should be immutable and have no identity.
 * </p>
 *
 * @see <a href=
 * "https://www.domainlanguage.com/wp-content/uploads/2016/05/DDD_Reference_2015-03.pdf">Domain-Driven Design Reference</a>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueObject {
}

