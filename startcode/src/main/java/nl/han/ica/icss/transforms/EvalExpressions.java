package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
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

    private Literal transformOperation(Operation node) {
        Literal operationResult;
        var operationType = node.getNodeLabel();

        switch (operationType) {
            case "Multiply":
                operationResult = transformMultiplyOperation((MultiplyOperation) node);
                break;
            case "Add":
                operationResult = transformAddOperation((AddOperation) node);
                break;
            case "Subtract":
                operationResult = transformSubtractOperation((SubtractOperation) node);
                break;
            default:
                throw new NoSuchOperationTypeException();
                // break; // wordt intellij onrustig van
        }

        return operationResult;
    }

    /* Was mooier geweest met een generic op literal zodat value in literal zit
     * en wieweet met calculation methoden in de operations zodat de operation methoden 1 methode hadden kunnen zijn
     * maar ik moest daar eng veel startcode voor aanpassen dus besloot ik dat het buiten de scope van dit project moest liggen
     */
    private Literal transformMultiplyOperation(MultiplyOperation node) {
        var lhsValue = transformExpression(node.lhs);
        var rhsValue = transformExpression(node.rhs);

        var operationType = lhsValue.getType() != ExpressionType.SCALAR ? lhsValue.getType() : rhsValue.getType();

        Literal result;
        int total;
        switch (operationType) {
            case PIXEL:
                total = ((PixelLiteral) lhsValue).value * ((PixelLiteral) rhsValue).value;
                result = new PixelLiteral(total);
                break;
            case PERCENTAGE:
                total = ((PercentageLiteral) lhsValue).value * ((PercentageLiteral) rhsValue).value;
                result = new PercentageLiteral(total);
                break;
            default:
                throw new InvalidLiteralTypeException();
                // break; // wordt intellij onrustig van
        }

        return result;
    }

    private Literal transformAddOperation(AddOperation node) {
        var lhsValue = transformExpression(node.lhs);
        var rhsValue = transformExpression(node.rhs);

        var operationType = lhsValue.getType();

        Literal result;
        int total;
        switch (operationType) {
            case PIXEL:
                total = ((PixelLiteral) lhsValue).value + ((PixelLiteral) rhsValue).value;
                result = new PixelLiteral(total);
                break;
            case PERCENTAGE:
                total = ((PercentageLiteral) lhsValue).value + ((PercentageLiteral) rhsValue).value;
                result = new PercentageLiteral(total);
                break;
            default:
                throw new InvalidLiteralTypeException();
                // break; // wordt intellij onrustig van
        }

        return result;
    }

    private Literal transformSubtractOperation(SubtractOperation node) {
        var lhsValue = transformExpression(node.lhs);
        var rhsValue = transformExpression(node.rhs);

        var operationType = lhsValue.getType();

        Literal result;
        int total;
        switch (operationType) {
            case PIXEL:
                total = ((PixelLiteral) lhsValue).value - ((PixelLiteral) rhsValue).value;
                result = new PixelLiteral(total);
                break;
            case PERCENTAGE:
                total = ((PercentageLiteral) lhsValue).value - ((PercentageLiteral) rhsValue).value;
                result = new PercentageLiteral(total);
                break;
            default:
                throw new InvalidLiteralTypeException();
                // break; // wordt intellij onrustig van
        }

        return result;
    }
}
