package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOANER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_QUANTITY;

import java.util.stream.Stream;

import seedu.address.logic.commands.LoanListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.item.LoanerDescription;
import seedu.address.model.item.Name;
import seedu.address.model.item.Quantity;

//@@author ChewKinWhye

/**
 * Parses input arguments and creates a new LoanListCommand object
 */
public class LoanListCommandParser implements Parser<LoanListCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the LoanListCommand
     * and returns an LoanListCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */

    public LoanListCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_QUANTITY, PREFIX_LOANER);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_QUANTITY, PREFIX_LOANER)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, LoanListCommand.MESSAGE_USAGE));
        }
        Name itemName = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Name loanerName = ParserUtil.parseName(argMultimap.getValue(PREFIX_LOANER).get());
        Quantity quantity = ParserUtil.parseQuantity(argMultimap.getValue(PREFIX_QUANTITY).get());
        return new LoanListCommand(new LoanerDescription(itemName, loanerName, quantity));
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
