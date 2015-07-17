package org.teiid.translator.exec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.teiid.core.util.Assertion;
import org.teiid.core.util.StringUtil;
import org.teiid.language.ColumnReference;
import org.teiid.language.Comparison;
import org.teiid.language.Condition;
import org.teiid.language.Expression;
import org.teiid.language.In;
import org.teiid.language.Literal;
import org.teiid.language.visitor.HierarchyVisitor;
import org.teiid.metadata.Column;
import org.teiid.translator.TranslatorException;


/**
 */
public class ExecVisitor extends HierarchyVisitor {

    private Map whereClause = new HashMap();
    private TranslatorException exception;

    /**
     * 
     */
    public ExecVisitor() {
        super();        
    }

    public void reset() {
        whereClause = new HashMap();
    }

    public Map getWhereClause() {
        return this.whereClause;
    }
    
    public TranslatorException getException() {
        return this.exception;
    }

    
//    @Override
//    public void visit(In criterion) {
//		In obj = (In)criterion;
//		Assertion.assertTrue(!obj.isNegated());
//
//		List<Expression> rhsList = obj.getRightExpressions();
//
//		int i = 0;
//		for (Expression expr : rhsList) {
//			Literal literal = (Literal) expr;
//			
//			commands.add(i++, (String) literal.getValue());
//
//		}
//    }
    /* 
     * @see com.metamatrix.data.visitor.LanguageObjectVisitor#visit(com.metamatrix.data.language.ICompareCriteria)
     */
	@Override
	public void visit(Comparison obj) {
		Comparison.Operator op = obj.getOperator();

		Expression lhs = obj.getLeftExpression();
		Expression rhs = obj.getRightExpression();

		// joins between the objects in the same cache is not usable
		if ((lhs instanceof ColumnReference && rhs instanceof ColumnReference)
				|| (lhs instanceof Literal && rhs instanceof Literal)) {
			return;
		}

		Object value = null;
		Column mdIDElement = null;
		Literal literal = null;
		if (lhs instanceof ColumnReference) {

			mdIDElement = ((ColumnReference) lhs).getMetadataObject();
			literal = (Literal) rhs;
			value = literal.getValue();

		} else if (rhs instanceof ColumnReference ){
			mdIDElement = ((ColumnReference) rhs).getMetadataObject();
			literal = (Literal) lhs;
			value = literal.getValue();
		}

		if (value == null) {
			this.exception = new TranslatorException("Invalid null value for column " + mdIDElement.getName());
		} else {
              
            whereClause.put(mdIDElement.getName(), value);
//            System.out.println("SYMBOL: " + symbol.getMetadataID().getName() + " value: " + whereValue );//$NON-NLS-1$ //$NON-NLS-2$
		}
      
    }
    
//    public static Map getWhereClauseMap(Condition crit) throws TranslatorException {
//        ExecVisitor visitor = new ExecVisitor();
//        crit.acceptVisitor(visitor);
//        
//        if(visitor.getException() != null) { 
//            throw visitor.getException();
//        }
//        
//        return visitor.getWhereClause();
//    } 
    
    public static List<String> getWhereClauseCommands(Condition crit) throws TranslatorException {
        ExecVisitor visitor = new ExecVisitor();
        crit.acceptVisitor(visitor);
        
        if(visitor.getException() != null) { 
            throw visitor.getException();
        }
        
        List<String> commands = null;
        
        String args =  (String) visitor.getWhereClause().get(ExecMetadataProcessor.ARGUMENTS);
        String del =  (String) visitor.getWhereClause().get(ExecMetadataProcessor.DELIMITER);
        
        if (del == null) {
        	commands = new ArrayList<String>(1);
        	commands.add(args);
        } else {
        	commands = StringUtil.getTokens(args, del);
        }
        
        return commands;
    }        

}
