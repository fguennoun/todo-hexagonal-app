package ddd;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>
 * A Domain Event represents something that happened in the domain that domain
 * experts care about.
 * </p>
 * <p>
 * Domain Events are immutable and represent a fact that occurred in the past.
 * </p>
 *
 * @see <a href=
 * "https://www.domainlanguage.com/wp-content/uploads/2016/05/DDD_Reference_2015-03.pdf">Domain-Driven Design Reference</a>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainEvent {
}

