package edu.psu.geovista.app.spreadsheet.functions;

import edu.psu.geovista.app.spreadsheet.exception.NoReferenceException;
import edu.psu.geovista.app.spreadsheet.exception.ParserException;
import edu.psu.geovista.app.spreadsheet.formula.Node;

/*
 * Description:
 * Date: Apr 1, 2003
 * Time: 9:16:36 PM
 * @author Jin Chen
 */

public class FunctionAsin extends FunctionSP {

    protected Number doFun(Node node)throws ParserException,NoReferenceException {
        return new Float(Math.asin(getSingleParameter(node)));
    }

    public String getUsage() {
        return "ASIN(value)";
    }

    public String getDescription() {
        return "Returns the arcsine value of a number.";
    }
}