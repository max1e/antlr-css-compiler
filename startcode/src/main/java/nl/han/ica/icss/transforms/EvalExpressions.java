package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.transforms.exceptions.InvalidLiteralTypeException;
import nl.han.ica.icss.transforms.exceptions.NoSuchOperationTypeException;
import nl.han.ica.icss.transforms.exceptions.VariableNotDefinedException;

import java.util.HashMap;
import java.util.LinkedList;

public class EvalExpressions implements Transform {

    private LinkedList<HashMap<String, Literal>> variableValues;

    public EvalExpressions() {
        variableValues = new LinkedList<>();
    }

    @Override
    public void apply(AST ast) {
        transformStylesheet(ast.root);
    }

    private void transformStylesheet(Stylesheet node) {
        variableValues.addFirst(new HashMap<>());

        LinkedList<VariableAssignment> childrenToDelete = new LinkedList<>();

        for (var child : node.getChildren()) {
            if (child instanceof VariableAssignment) {
                transformVariableAssignment((VariableAssignment) child);

                childrenToDelete.add((VariableAssignment) child);
            }

            if (child instanceof Stylerule) {
                transformStylerule((Stylerule) child);
            }
        }

        while (!childrenToDelete.isEmpty()) {
            node.removeChild(childrenToDelete.getFirst());
            childrenToDelete.removeFirst();
        }

        variableValues.removeFirst();
    }

    private void transformVariableAssignment(VariableAssignment node) {
        var variableValue = transformExpression(node.expression);
        variableValues.getFirst().put(node.name.name, variableValue);
    }

    private void transformStylerule(Stylerule node) {
        transformDeclarationBlock(node);
    }

    private void transformIfClause(IfClause node) {
        node.conditionalExpression = transformExpression(node.conditionalExpression);

        transformDeclarationBlock(node);

        if (node.getElseClause() != null) {
            transformElseClause(node.getElseClause());
        }
    }

    private void transformElseClause(ElseClause node) {
        transformDeclarationBlock(node);
    }


    private void transformDeclarationBlock(ASTNode node) {
        variableValues.addFirst(new HashMap<>());

        for (ASTNode child : node.getChildren()) {
            if (child instanceof IfClause) {
                transformIfClause((IfClause) child);
            }
            else if (child instanceof Declaration) {
                transformDeclaration((Declaration) child);
            }
            else if (child instanceof VariableAssignment) {
                transformVariableAssignment((VariableAssignment) child);
            }
        }

        variableValues.removeFirst();
    }

    private void transformDeclaration(Declaration node) {
        node.expression = transformExpression(node.expression);
    }

    private Literal transformExpression(Expression node) {
        Literal expressionResult;

        if (node instanceof Operation) {
            expressionResult = transformOperation((Operation) node);
        }
        else if (node instanceof VariableReference) {
            expressionResult = transformVariableReference((VariableReference) node);
        }
        else {
            expressionResult = (Literal) node;
        }

        return expressionResult;
    }

    private Literal transformVariableReference(VariableReference node) {
        for (var variableValueCollection : variableValues) {
            if (variableValueCollection.containsKey(node.name)) {
                return variableValueCollection.get(node.name);
            }
        }

        throw new VariableNotDefinedException(node.name);
    }

    /*
     * Ik had dit liever met polymorfie opgelost, nu heb ik veel duplicate code.
     * Ik heb dit niet veranderd in de startcode omdat ik niet dacht dat dat binnen de
     * scope van deze opdracht lag en ik bang ben code te veranderen aangezien ik het niet
     * zelf heb geschreven.
     */
    private Literal transformOperation(Operation node) {
        Literal result;

        var operationType = node.getNodeLabel();

        var lhsValue = transformExpression(node.lhs);
        var rhsValue = transformExpression(node.rhs);

        var literalType = lhsValue.getType() != ExpressionType.SCALAR ? lhsValue.getType() : rhsValue.getType();

        int total;
        switch (operationType) {
            case "Multiply":
                if (literalType == ExpressionType.PIXEL) {
                    total = ((PixelLiteral) lhsValue).value * ((PixelLiteral) rhsValue).value;
                    result = new PixelLiteral(total);
                }
                else if (literalType == ExpressionType.PERCENTAGE) {
                    total = ((PercentageLiteral) lhsValue).value * ((PercentageLiteral) rhsValue).value;
                    result = new PercentageLiteral(total);
                }
                else {
                    throw new InvalidLiteralTypeException();
                }
                break;
            case "Add":
                if (literalType == ExpressionType.PIXEL) {
                    total = ((PixelLiteral) lhsValue).value + ((PixelLiteral) rhsValue).value;
                    result = new PixelLiteral(total);
                }
                else if (literalType == ExpressionType.PERCENTAGE) {
                    total = ((PercentageLiteral) lhsValue).value + ((PercentageLiteral) rhsValue).value;
                    result = new PercentageLiteral(total);
                }
                else {
                    throw new InvalidLiteralTypeException();
                }
                break;
            case "Subtract":
                if (literalType == ExpressionType.PIXEL) {
                    total = ((PixelLiteral) lhsValue).value - ((PixelLiteral) rhsValue).value;
                    result = new PixelLiteral(total);
                }
                else if (literalType == ExpressionType.PERCENTAGE) {
                    total = ((PercentageLiteral) lhsValue).value - ((PercentageLiteral) rhsValue).value;
                    result = new PercentageLiteral(total);
                }
                else {
                    throw new InvalidLiteralTypeException();
                }
                break;
            default:
                throw new NoSuchOperationTypeException();
                // break; // wordt intellij onrustig van
        }

        return result;
    }
}
