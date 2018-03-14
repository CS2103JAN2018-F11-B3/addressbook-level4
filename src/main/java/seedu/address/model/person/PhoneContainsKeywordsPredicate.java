package seedu.address.model.person;

import seedu.address.commons.util.StringUtil;

import java.util.List;
import java.util.function.Predicate;

/**
 * Tests that a {@code Person}'s {@code Phone} matches any of the keywords given.
 */

public class PhoneContainsKeywordsPredicate implements Predicate<Person> {

        private final List<String> keywords;

        public PhoneContainsKeywordsPredicate(List<String> keywords) {
            this.keywords = keywords;
        }

        @Override
        public boolean test(Person person) {
            return keywords.stream()
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getPhone().value, keyword));
        }

        @Override
        public boolean equals(Object other) {
            return other == this // short circuit if same object
                    || (other instanceof seedu.address.model.person.PhoneContainsKeywordsPredicate // instanceof handles nulls
                    && this.keywords.equals(((seedu.address.model.person.PhoneContainsKeywordsPredicate) other).keywords)); // state check
        }
}
