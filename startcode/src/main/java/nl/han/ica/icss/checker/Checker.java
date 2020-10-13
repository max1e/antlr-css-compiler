package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.HashMap;
import java.util.LinkedList;

public class Checker {

    private LinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        variableTypes = new LinkedList<>();
        checkStylesheet(ast.root);
    }

    private void checkStylesheet(Stylesheet stylesheet) {
        variableTypes.addFirst(new HashMap<>());

        for (var child : stylesheet.getChildren()) {
            if (child instanceof VariableAssignment) {
                checkVariableAssignment((VariableAssignment) child);
            }

            if (child instanceof Stylerule) {
                checkStylerule((Stylerule) child);
            }
        }

        variableTypes.removeFirst();
    }

    private void checkVariableAssignment(VariableAssignment node) {
        var variableType = getExpressionType(node.expression);

        if (variableType == null) {
            throw new UnexpectedSyntaxException();
        }

        if (variableType == ExpressionType.UNDEFINED) {
            node.setError("The variable type could not be found, there is either no such variable declared in the current scope or something failed declaring that variable");
        }

        variableTypes.getFirst().put(node.name.name, variableType);
    }

    private ExpressionType checkOperation(Operation node) {
        var lhsType = getExpressionType(node.lhs);
        var rhsType = getExpressionType(node.rhs);

        if (node instanceof MultiplyOperation) {
            checkMultiplyOperation(node, lhsType, rhsType);
        }
        else if (node instanceof AddOperation || node instanceof SubtractOperation) {
            checkAddSubtractOperation(node, lhsType, rhsType);
        }
        else {
            throw new UnexpectedSyntaxException();
        }

        if (lhsType == ExpressionType.COLOR || rhsType == ExpressionType.COLOR) {
            node.setError("Colors can not be used in operations");
        }

        return lhsType != ExpressionType.SCALAR ? lhsType : rhsType;
    }

    private void checkMultiplyOperation(Operation node, ExpressionType lhsType, ExpressionType rhsType) {
        if (lhsType != ExpressionType.SCALAR && rhsType != ExpressionType.SCALAR) {
            node.setError("Multiply operations must have at least one operand of scalar type");
        }

    }

    private void checkAddSubtractOperation(Operation node, ExpressionType lhsType, ExpressionType rhsType) {
        if (lhsType != rhsType) {
            node.setError("Add and subtract operations must have operands of equal types");
        }
    }

    private void checkStylerule(Stylerule node) {
        checkDeclarationBlock(node);
    }

    private void checkIfClause(IfClause node) {

        if (getExpressionType(node.conditionalExpression) != ExpressionType.BOOL) {
            node.setError("If statements require a conditional expression of type boolean");
        }

        checkDeclarationBlock(node);

        if (node.elseClause != null) {
            checkElseClause(node.elseClause);
        }
    }

    private void checkElseClause(ElseClause node) {
        checkDeclarationBlock(node);
    }

    private void checkDeclarationBlock(ASTNode node) {
        variableTypes.addFirst(new HashMap<>());

        for (ASTNode child : node.getChildren()) {
            if (child instanceof IfClause) {
                checkIfClause((IfClause) child);
            }
            else if (child instanceof Declaration) {
                checkDeclaration((Declaration) child);
            }
            else if (child instanceof VariableAssignment) {
                checkVariableAssignment((VariableAssignment) child);
            }
        }

        variableTypes.removeFirst();
    }

    private void checkDeclaration(Declaration node) {
        var variableType = getExpressionType(node.expression);

        if (variableType == ExpressionType.UNDEFINED) {
            node.setError("The variable type could not be found, there is either no such variable declared in the current scope or something failed declaring that variable");
        }
        else {
            switch (node.property.name) {
                case "color":
                    if (!(variableType == ExpressionType.COLOR)) {
                        node.setError("Property 'color' has invalid type");
                    }
                    break;
                case "background-color":
                    if (!(variableType == ExpressionType.COLOR)) {
                        node.setError("Property 'background-color' has invalid type");
                    }
                    break;
                case "width":
                    if (!(variableType == ExpressionType.PIXEL || variableType == ExpressionType.PERCENTAGE)) {
                        node.setError("Property 'width' has invalid type");
                    }
                    break;
                case "height":
                    if (!(variableType == ExpressionType.PIXEL || variableType == ExpressionType.PERCENTAGE)) {
                        node.setError("Property 'height' has invalid type");
                    }
                    break;
                default:
                    node.setError("Property does not exist");
                    break;
            }
        }
    }

    private ExpressionType getExpressionType(Expression expression) {
        ExpressionType expressionType;

        if (expression instanceof Operation) {
            expressionType = checkOperation((Operation) expression);
        }
        else if (expression instanceof VariableReference) {
            expressionType = getVariableType(((VariableReference) expression).name);
        }
        else if (expression instanceof Literal) {
            expressionType = ((Literal) expression).getType();
        }
        else {
            throw new UnexpectedSyntaxException();
        }

        return expressionType;
    }

    /**
     * Returnt het type van een variabele en returnt UNDEFINED als de variabele buiten de scope ligt
     * */
    private ExpressionType getVariableType(String variableName) {
        var variableType = ExpressionType.UNDEFINED;

        for (var variableTypeCollection : variableTypes) {
            for (var j = 0; j < variableTypeCollection.size(); j++) {
                if (variableTypeCollection.containsKey(variableName)) {
                    variableType = variableTypeCollection.get(variableName);
                }
            }
        }

        return variableType;
    }
}
